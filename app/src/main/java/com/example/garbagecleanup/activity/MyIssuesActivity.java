package com.example.garbagecleanup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.example.garbagecleanup.PrefManager;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.adapter.MyIssuesRecyclerAdapter;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.Issue_Model_Class;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyIssuesActivity extends AppCompatActivity {

    private static final String TAG = "MyIssuesActivity";
    private RecyclerView rvMyIssues;
    private MyIssuesRecyclerAdapter myIssuesRecyclerAdapter;
    private ArrayList<Issue_Model_Class> issueModelClassArrayList;

    public static Intent makeIntent(Context context) {
        return new Intent(context, MyIssuesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_issues);
        rvMyIssues = findViewById(R.id.rvMyIssues);
        rvMyIssues.setHasFixedSize(true);
        LinearLayoutManager linearVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMyIssues.setLayoutManager(linearVertical);

        issueModelClassArrayList = new ArrayList<>();
        myIssuesRecyclerAdapter = new MyIssuesRecyclerAdapter(this, issueModelClassArrayList);
        rvMyIssues.setAdapter(myIssuesRecyclerAdapter);

        String uri = AppConstants.MY_ISSUES + PrefManager.getUser().getUserId() + "/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Issue_Model_Class issueModelClass = new Gson().fromJson(object.toString(), Issue_Model_Class.class);
                        Log.d(TAG, "onResponse: " + issueModelClass);
                        issueModelClassArrayList.add(issueModelClass);
                    }
                    myIssuesRecyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        stringRequest.setShouldCache(false);
        MySingleton.getInstance().addToRequest(stringRequest);
    }
}
