package com.example.garbagecleanup.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

                                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
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

                    MySingleton.getInstance(LoginActivity.this).addToRequest(jsonArrayRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
