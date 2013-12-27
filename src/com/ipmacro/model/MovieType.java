package com.ipmacro.model;

public class MovieType {
    int id;
    String name;
    
    public MovieType(){
        
    }
    
    public MovieType(int id,String name){
        this.id = id;
        this.name = name;
    }
    
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
    
    
}
