package com.example.limb;
public class Categories {
    private String name;
    private int сategoryResource;
    public Categories(String name, int сategory){
        this.name=name;
        this.сategoryResource=сategory;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getFlagResource() {
        return this.сategoryResource;
    }
    public void setFlagResource(int сategoryResource) {
        this.сategoryResource = сategoryResource;
    }
}
