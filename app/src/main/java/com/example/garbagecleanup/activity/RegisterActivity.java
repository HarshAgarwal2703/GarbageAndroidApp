package com.example.garbagecleanup.activity;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.RegisterUser;
import com.example.garbagecleanup.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmailID, etPassword, etContactNumber;
    private Button btnRegister;
    private RequestQueue requestQueue;
    private Gson gson = new Gson();

    private static final String TAG = "RegisterActivity";

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmailID = findViewById(R.id.etEmailID);
        etContactNumber = findViewById(R.id.etContactNumber);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etFirstName.getText())) {
                    etFirstName.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(etLastName.getText())) {
                    etLastName.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(etEmailID.getText())) {
                    etEmailID.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(etContactNumber.getText())) {
                    etContactNumber.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText())) {
                    etPassword.setError("Required");
                    return;
                }
                if (isNetworkAvailable()) {
                    RegisterUser registerUser = new RegisterUser();
                    registerUser.setFirstName(etFirstName.getText().toString());
                    registerUser.setLastName(etLastName.getText().toString());
                    final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

                    Pattern pattern = Pattern.compile(EMAIL_REGEX);
                    Matcher matcher = pattern.matcher(etEmailID.getText().toString());

                    if(matcher.matches()){
                        registerUser.setEmailId(etEmailID.getText().toString());
                    }else{
                        Toast.makeText(RegisterActivity.this, "INVALID EMAIL", Toast.LENGTH_SHORT).show();
                    }
                    try{
                        registerUser.setPhoneNumber(Integer.parseInt(etContactNumber.getText().toString()));
                    }
                    catch (Exception e){
                        Toast.makeText(RegisterActivity.this, "INVALID NUMBER", Toast.LENGTH_SHORT).show();
                    }
                    registerUser.setPassword(etPassword.getText().toString());
                    String json = gson.toJson(registerUser);
                    Log.i(TAG, "onClick: " + json);
                    try {
                        final JSONObject object = new JSONObject(json);
                        JsonObjectRequest jsonObjectRequest =
                                new JsonObjectRequest(
                                        Request.Method.POST,
                                        AppConstants.REGISTER_USER,
                                        object,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.i(TAG, "onResponse: " + response);
                                                try {
                                                    if (!response.getBoolean("error")) {
                                                        String message = response.getString("message");
                                                        User user = new User();
                                                        JSONObject userObj = response.getJSONObject("user");
                                                        user.setEmailId(userObj.getString("email_id"));
                                                        user.setLastName(userObj.getString("last_name"));
                                                        user.setFirstName(userObj.getString("first_name"));
                                                        user.setPhoneNumber(userObj.getInt("phone_number"));
                                                        user.setUserId(userObj.getInt("id"));

                                                        SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                                                        sharedPreferences.edit().putBoolean(AppConstants.SP_LOGGED_IN, true).commit();
                                                        sharedPreferences.edit().putString(AppConstants.SP_GET_USER, new Gson().toJson(user)).commit();
                                                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                                                        startActivity(MainActivity.makeIntent(RegisterActivity.this));
                                                        finishAffinity();
                                                    } else {
                                                        String message = response.getString("message");
                                                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                Log.e(TAG, "onErrorResponse: " + error);

                                            }
                                        }
                                );

                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        MySingleton.getInstance().addToRequest(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
