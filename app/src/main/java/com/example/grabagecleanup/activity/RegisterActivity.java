package com.example.grabagecleanup.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.grabagecleanup.R;
import com.example.grabagecleanup.model.User;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText tvFirstName,tvLastName,tvEmailID,tvPassword,tvContactNumber;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user=new User();
                user.setFirstName(tvFirstName.getText().toString());
                user.setLastName(tvLastName.getText().toString());
                user.setEmailId(tvEmailID.getText().toString());
                user.setPhoneNumber(Integer.parseInt(tvContactNumber.getText().toString()));


            }
        });
    }
}
