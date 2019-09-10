package com.example.garbagecleanup;

public class Issue_Model_Class {

    private String id;
    private String Latitude;
    private String Longitude;
    private double rating;
    private byte[] img;
    private String Date;

    public Issue_Model_Class()
    {

    }

    public Issue_Model_Class(String id, String Latitude, String Longitude, double rating, byte[] img) {
        this.id = id;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.rating = rating;
        this.img = img;
    }

    public Issue_Model_Class(String id, String Latitude, String Longitude, double rating, byte[] img, String Date) {
        this.id = id;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.rating = rating;
        this.img = img;
        this.Date=Date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Issue_Model_Class{" +
                "id=" + id +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", rating=" + rating +
                ", img=" + img +
                ", Date='" + Date + '\'' +
                '}';
    }
}
