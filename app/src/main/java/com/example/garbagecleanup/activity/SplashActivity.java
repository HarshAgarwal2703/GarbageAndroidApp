package com.example.garbagecleanup.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.garbagecleanup.AppConstants;
import com.example.garbagecleanup.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(AppConstants.SP_LOGGED_IN, false)) {
            startActivity(MainActivity.makeIntent(this));
        } else {
            startActivity(LoginActivity.makeIntent(this));
        }

        finish();
    }
}
