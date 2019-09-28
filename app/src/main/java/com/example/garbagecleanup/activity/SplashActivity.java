package com.example.garbagecleanup.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.garbagecleanup.R;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = MySingleton.getInstance().getSharedPreferences();
        Log.i(TAG, "onCreate: " + sharedPreferences.getBoolean(AppConstants.SP_LOGGED_IN, false));
        if (sharedPreferences.getBoolean(AppConstants.SP_LOGGED_IN, false)) {
            startActivity(MainActivity.makeIntent(this));
        } else {
            startActivity(LoginActivity.makeIntent(this));
        }

        finish();
    }
}
