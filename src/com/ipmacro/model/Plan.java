package com.ipmacro.model;

public class Plan {
    String liveChannelIds;
    String name;
    double price;
    String dateOfExpiry;
    public String getLiveChannelIds() {
        return liveChannelIds;
    }
    public void setLiveChannelIds(String liveChannelIds) {
        this.liveChannelIds = liveChannelIds;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getDateOfExpiry() {
        return dateOfExpiry;
    }
    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }
    
    
}
