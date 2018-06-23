package com.example.daniela.imovies.comm;

import com.example.daniela.imovies.entity.Episode;
import com.example.daniela.imovies.entity.Serie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class EpisodesGet implements Runnable {
    private List<Episode> episodes;
    private String message;
    public EpisodesGet(){

        new Thread(this).start();

    }

    public void run(){
        int porta = 11500;

        try{
            InetAddress address = InetAddress.getByName("192.168.43.12");

            String mensagem = getMessage() + "<EOF>";

            Socket s = new Socket(address, porta);
            ObjectOutputStream objesc = new ObjectOutputStream(s.getOutputStream());
            objesc.writeObject(mensagem);
            objesc.flush();

            BufferedReader recv;

            recv = new BufferedReader(new InputStreamReader(s.getInputStream()));

            JSONObject item;
            JSONArray episodesJson = new JSONArray(recv.readLine());
            List<Episode> lista = new ArrayList<>();
            for(int i = 0; i < episodesJson.length(); i++){
                item = episodesJson.optJSONObject(i);
                lista.add(new Episode
                        (item.getInt("Season"),
                         item.getString("FkSerie"),
                         item.getInt("Ep"),
                         item.getString("Name"),
                         item.getString("Synopsis")));
            }
            episodes = lista;
            s.close();

        }catch(Exception e){
            e.getMessage();
        }
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
