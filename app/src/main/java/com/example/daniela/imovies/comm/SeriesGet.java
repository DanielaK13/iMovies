package com.example.daniela.imovies.comm;

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

public class SeriesGet implements Runnable{
    private List<Serie> series;
    public SeriesGet(){

        new Thread(this).start();

    }

    public void run(){
        int porta = 11000;

        try{
            InetAddress address = InetAddress.getByName("192.168.43.12");

            String mensagem = new String("Series<EOF>");

            Socket s = new Socket(address, porta);
            ObjectOutputStream objesc = new ObjectOutputStream(s.getOutputStream());
            objesc.writeObject(mensagem);
            objesc.flush();

            BufferedReader recv;

            recv = new BufferedReader(new InputStreamReader(s.getInputStream()));

            //JSONObject json =
            JSONObject item;
            JSONArray seriesJson = new JSONArray(recv.readLine());
            List<Serie> lista = new ArrayList<Serie>();
            for(int i = 0; i < seriesJson.length(); i++){
                item = seriesJson.optJSONObject(i);
                lista.add(new Serie(item.getString("Name"), item.getInt("Seasons"), item.getString("Synopsis")));
            }
            series = lista;
            s.close();

        }catch(Exception e){
            e.getMessage();
        }
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }
}

