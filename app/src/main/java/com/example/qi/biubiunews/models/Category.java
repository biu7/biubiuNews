package com.example.qi.biubiunews.models;

/**
 * Created by qi on 17-4-26.
 */

public class Category {

    /**
     * id : 2
     * name : 国际
     */

    private int id;
    private String name;
    private int site_id;

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

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", site_id=" + site_id +
                '}';
    }
}
