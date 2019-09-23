package com.example.garbagecleanup.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.garbagecleanup.AppConstants;
import com.example.garbagecleanup.MySingleton;
import com.example.garbagecleanup.R;
import com.example.grabagecleanup.model.RegisterUser;
import com.example.grabagecleanup.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmailID, etPassword, etContactNumber;
    private Button btnRegister;
    private RequestQueue requestQueue;
    private Gson gson = new Gson();

    private static final String TAG = "RegisterActivity";
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
                RegisterUser registerUser = new RegisterUser();
                registerUser.setFirstName(etFirstName.getText().toString());
                registerUser.setLastName(etLastName.getText().toString());
                registerUser.setEmailId(etEmailID.getText().toString());
                registerUser.setPhoneNumber(Integer.parseInt(etContactNumber.getText().toString()));
                registerUser.setPassword(etPassword.getText().toString());
                String json = gson.toJson(registerUser);
                Log.i(TAG, "onClick: " + json);
                try {
                    final JSONObject object = new JSONObject(json);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.REGISTER_USER, object,
                                                                                new Response.Listener<JSONObject>() {
                                                                                    @Override
                                                                                    public void onResponse(JSONObject response) {
                                                                                        User user = new User();
                                                                                        try {
                                                                                            user.setEmailId(response.getString("email_id"));
                                                                                            user.setLastName(response.getString("last_name"));
                                                                                            user.setFirstName(response.getString("first_name"));
                                                                                            user.setPhoneNumber(response.getInt("phone_number"));
                                                                                            user.setUserId(response.getInt("id"));
                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        }

                                                                                    }
                                                                                },
                                                                                new Response.ErrorListener() {
                                                                                    @Override
                                                                                    public void onErrorResponse(VolleyError error) {

                                                                                        Log.e("DGFHdfh", error.toString());

                                                                                    }
                                                                                }
                    );

                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(RegisterActivity.this).addToRequest(jsonObjectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
