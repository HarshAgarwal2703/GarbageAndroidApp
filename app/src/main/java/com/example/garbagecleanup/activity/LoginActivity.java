package com.example.garbagecleanup.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.request.JsonArrayRequest;
import com.example.garbagecleanup.AppConstants;
import com.example.garbagecleanup.MySingleton;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.model.LoginUser;
import com.example.garbagecleanup.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegister, btnLogin;
    private EditText etEmailID, etPassword;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailID = findViewById(R.id.etEmailID);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser loginUser = new LoginUser();
                loginUser.setEmailId(etEmailID.getText().toString());
                loginUser.setPassword(etPassword.getText().toString());
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(loginUser));
                    final JSONArray jsonArray = new JSONArray();
                    jsonArray.put(jsonObject);
                    Log.i(TAG, "onClick: " + jsonArray);
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, AppConstants.LOGIN_USER, jsonArray, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response1) {
                            Log.i(TAG, "onResponse: " + response1.toString());
                            try {
                                JSONObject response = response1.getJSONObject(0);
                                User user = new User();
                                user.setEmailId(response.getString("email_id"));
                                user.setLastName(response.getString("last_name"));
                                user.setFirstName(response.getString("first_name"));
                                user.setPhoneNumber(response.getInt("phone_number"));
                                user.setUserId(response.getInt("id"));

                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(com.android.volley.error.VolleyError error) {
                            Log.e("hbhb", error.toString());

                        }

                    });
                    jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    MySingleton.getInstance(LoginActivity.this).addToRequest(jsonArrayRequest);

//                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.LOGIN_USER, jsonObject,
//                                                                                new Response.Listener<JSONObject>() {
//                                                                                    @Override
//                                                                                    public void onResponse(JSONObject response) {
//                                                                                        Log.e("response: ", response.toString());
//
//                                                                                    }
//                                                                                },
//                                                                                new Response.ErrorListener() {
//                                                                                    @Override
//                                                                                    public void onErrorResponse(VolleyError error) {
//
//                                                                                        Log.e("DGFHdfh", error.toString());
//
//                                                                                    }
//                                                                                }
//                    );
//
//                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
