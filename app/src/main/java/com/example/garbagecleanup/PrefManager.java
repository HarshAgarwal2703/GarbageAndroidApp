package com.example.garbagecleanup;

import android.content.SharedPreferences;

import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.User;
import com.google.gson.Gson;

/**
 * User: Aman
 * Date: 24-09-2019
 * Time: 03:22 PM
 */
public class PrefManager {

    private static SharedPreferences sharedPreferences;

    public static void init() {
        sharedPreferences = MySingleton.getInstance().getSharedPreferences();
    }

    public static User getUser() {
        User user = new Gson().fromJson(sharedPreferences.getString(AppConstants.SP_GET_USER, ""), User.class);
        return user;
    }
}
