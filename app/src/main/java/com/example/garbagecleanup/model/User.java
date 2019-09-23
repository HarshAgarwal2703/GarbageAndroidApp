package com.example.garbagecleanup.model;

import com.google.gson.annotations.SerializedName;

/**
 * User: Aman
 * Date: 10-09-2019
 * Time: 02:30 AM
 */
public class User {
    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email_id")
    private String emailId;

    @SerializedName("id")
    private int userId;

    @SerializedName("phone_number")
    private int phoneNumber;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", userId=" + userId +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
