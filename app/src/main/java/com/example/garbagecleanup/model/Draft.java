package com.example.garbagecleanup.model;

import java.util.Arrays;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * User: Aman
 * Date: 22-09-2019
 * Time: 09:27 PM
 */
@Entity(tableName = "DraftTable")
public class Draft {
    @PrimaryKey(autoGenerate = true)
    public int draft_id;

    @ColumnInfo(name = "user_id")
    public int userID;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "latitude")
    public String latitude;

    @ColumnInfo(name = "longitude")
    public String longitude;

    @ColumnInfo(name = "area name")
    public String AreaName;

    @ColumnInfo(name = "timestamp")
    public String timestamp;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Draft(int userID, String title, String description, String latitude, String longitude, String timestamp, byte[] image, String AreaName) {
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.image = image;
        this.AreaName = AreaName;
    }

    public int getDraft_id() {
        return draft_id;
    }

    public int getUserID() {
        return userID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public byte[] getImage() {
        return image;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    @Override
    public String toString() {
        return "Draft{" +
                "draft_id=" + draft_id +
                ", userID=" + userID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", AreaName='" + AreaName + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}

