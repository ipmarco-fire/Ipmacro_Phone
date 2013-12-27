package com.ipmacro.model;

import java.util.ArrayList;
import java.util.List;

public class Vod {
    
    int id;
    String name;
    String logo;
    String actors;
    String director;
    String region;
    String language;
    boolean isDeductFees;
    List<Episode> episodeList;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getActors() {
        return actors;
    }
    public void setActors(String actors) {
        this.actors = actors;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public boolean isDeductFees() {
        return isDeductFees;
    }
    public void setDeductFees(boolean isDeductFees) {
        this.isDeductFees = isDeductFees;
    }
    public List<Episode> getEpisodeList() {
        return episodeList;
    }
    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }
    
    public void addEpisode(Episode e){
        if(this.episodeList == null){
            this.episodeList = new ArrayList<Episode>();
        }
        episodeList.add(e);
    }
}
