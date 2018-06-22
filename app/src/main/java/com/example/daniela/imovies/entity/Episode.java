package com.example.daniela.imovies.entity;

public class Episode {
    private Integer epTot;
    private Integer season;
    private String fkSerie;
    private Integer ep;
    private String name;
    private String synopsis;

    public Integer getEpTot() {
        return epTot;
    }

    public void setEpTot(Integer epTot) {
        this.epTot = epTot;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getFkSerie() {
        return fkSerie;
    }

    public void setFkSerie(String fkSerie) {
        this.fkSerie = fkSerie;
    }

    public Integer getEp() {
        return ep;
    }

    public void setEp(Integer ep) {
        this.ep = ep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
