package com.example.grabagecleanup;

import android.graphics.Bitmap;
import android.location.Location;

public class Model {

    private int id;
    private String Latitude;
    private String Longitude;
    private double rating;
    private int img;

    public Model(int id, String Latitude,String Longitude, double rating, int img) {
        this.id = id;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.rating = rating;
        this.img = img;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }
    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
