package com.example.grabagecleanup.click_2_send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.grabagecleanup.R;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayImages2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images2);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, DisplayImages2.class);
    }
}
