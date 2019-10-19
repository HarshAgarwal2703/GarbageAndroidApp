package com.example.garbagecleanup.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.LoginUser;
import com.example.garbagecleanup.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegister, btnLogin;
    private EditText etEmailID, etPassword;
    private static final String TAG = "LoginActivity";

    public static int PERMISSION_ALL = 1;
    public static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        etEmailID = findViewById(R.id.etEmailID);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, com.example.garbagecleanup.activity.RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etEmailID.getText())) {
                    etEmailID.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText())) {
                    etPassword.setError("Required");
                    return;
                }
                if (isNetworkAvailable()) {
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

                                    JSONObject response = (JSONObject) response1.get(0);
                                    if (!response.getBoolean("error")) {
                                        String message = response.getString("message");
                                        User user = new User();
                                        JSONObject userObj = response.getJSONObject("user");
                                        user.setEmailId(userObj.getString("email_id"));
                                        user.setLastName(userObj.getString("last_name"));
                                        user.setFirstName(userObj.getString("first_name"));
                                        user.setPhoneNumber(userObj.getInt("phone_number"));
                                        user.setUserId(userObj.getInt("id"));

                                        SharedPreferences sharedPreferences = MySingleton.getInstance().getSharedPreferences();
                                        sharedPreferences.edit().putBoolean(AppConstants.SP_LOGGED_IN, true).commit();
                                        sharedPreferences.edit().putString(AppConstants.SP_GET_USER, new Gson().toJson(user)).commit();
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                        startActivity(MainActivity.makeIntent(LoginActivity.this));
                                        finish();
                                    } else {
                                        String message = response.getString("message");
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e(TAG, "onErrorResponse: " + error.toString());

                            }
                        });
                        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        MySingleton.getInstance().addToRequest(jsonArrayRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
