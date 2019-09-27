package com.example.garbagecleanup.model;

public class Issue_Model_Class {

    private int id;
    private String Latitude;
    private String Longitude;
    private int votes;
    private String imageUrl;
    private String published_Date;
    private boolean checkLiked;
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Issue_Model_Class() {
        super();
    }

    public boolean isCheckLiked() {
        return this.checkLiked;
    }

    public void setCheckLiked(boolean checkLiked) {
        this.checkLiked = checkLiked;
    }

    public Issue_Model_Class(int id, String Latitude, String Longitude, int rating, String imageUrl, String published_Date) {
        this.id = id;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.votes = rating;
        this.imageUrl = imageUrl;
        this.published_Date = published_Date;
    }

    public String getPublished_Date() {
        return published_Date;
    }

    public void setPublished_Date(String published_Date) {
        this.published_Date = published_Date;
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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int rating) {
        this.votes = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Issue_Model_Class{" +
                "id=" + id +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", votes=" + votes +
                ", imageUrl='" + imageUrl + '\'' +
                ", published_Date='" + published_Date + '\'' +
                ", checkLiked=" + checkLiked +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
