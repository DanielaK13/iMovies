package com.example.daniela.imovies.entity;

public class Serie {
    private String name;
    private Integer seasons;
    private String synopsis;

    public Serie(String name, Integer seasons, String synopsis){
        this.name = name;
        this.seasons = seasons;
        this.synopsis = synopsis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeasons() {
        return seasons;
    }

    public void setSeasons(Integer seasons) {
        this.seasons = seasons;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
