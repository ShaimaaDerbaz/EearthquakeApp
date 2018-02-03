package com.example.shaimaaderbaz.earthquake.models;

/**
 * Created by Shaimaa Derbaz on 2/3/2018.
 */

public class Earthquake {
    double magnitude;
    String location;
    long time;
    String url;

    public Earthquake(double magnitude, String location , long time,String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.url = url;
        this.time = time;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
