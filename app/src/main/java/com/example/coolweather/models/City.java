package com.example.coolweather.models;

import java.io.Serializable;

public class City implements Serializable, Comparable<City>{
    private String name;
    private String country;

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return name + ", " + country;
    }

    @Override
    public int compareTo(City o) {
        return name.compareTo(o.name);
    }
}
