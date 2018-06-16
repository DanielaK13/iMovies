package com.example.daniela.imovies.entity;

import java.util.Date;

public class MyList {

    private String description;
    private String name;
    private String season;
    private String episodio;
    private boolean done;
    private String id;
    private String date;

    public MyList() {
    }

    public MyList(String description, String name, String season, String episodio, String date, boolean done) {
        this.description = description;
        this.name = name;
        this.done = done;
        this.season = season;
        this.episodio = episodio;
        this.date = date;
    }

    public MyList(String name, boolean done) {
        this.name = name;
        this.done = done;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEpisodio() {
        return episodio;
    }

    public String getName() {
        return name;
    }

    public String getSeason() {
        return season;
    }

    public void setEpisodio(String episodio) {
        this.episodio = episodio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public boolean getDone() { return done; }

    public void setDone(boolean done) { this.done = done; }
}
