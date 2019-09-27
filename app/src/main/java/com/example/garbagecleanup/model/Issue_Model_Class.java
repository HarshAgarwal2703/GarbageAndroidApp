package com.example.garbagecleanup.model;

import com.google.gson.annotations.SerializedName;

public class Issue_Model_Class {

    @SerializedName("id")
    private int id;

    @SerializedName("latitude")
    private String Latitude;
    @SerializedName("longitude")
    private String Longitude;
    @SerializedName("vote_count")
    private int rating;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("published_date")
    private String published_Date;
    private boolean checkLiked;
    @SerializedName("title")
    private String title;
    @SerializedName("Description")
    private String description;
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
        return checkLiked;
    }

    public void setCheckLiked(boolean checkLiked) {
        this.checkLiked = checkLiked;
    }

    public Issue_Model_Class(int id, String Latitude, String Longitude, int rating, String imageUrl, String published_Date) {
        this.id = id;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
                ", rating=" + rating +
                ", imageUrl='" + imageUrl + '\'' +
                ", published_Date='" + published_Date + '\'' +
                ", checkLiked=" + checkLiked +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
