package com.example.qi.biubiunews.models;

/**
 * Created by qi on 17-4-26.
 */

public class Site {

    /**
     * id : 2
     * name : 环球新闻
     */

    private int id;
    private String name;

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

    @Override
    public String toString() {
        return "Site{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
