package com.example.garbagecleanup.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.garbagecleanup.PrefManager;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.adapter.viewLikedPostsAdapter;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.model.Issue_Model_Class;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class viewLikedPosts extends AppCompatActivity {

    private static final String TAG = "viewIssueLikedPosts";
    private RecyclerView rvMyIssues;
    private viewLikedPostsAdapter mviewLikedPostsAdapter;
    private ProgressBar progressBar;
    private ArrayList<Issue_Model_Class> IssueList;
    ArrayList<Issue_Model_Class> likedArrayList = new ArrayList<Issue_Model_Class>();


    public static Intent makeIntent(Context context) {
        return new Intent(context, viewLikedPosts.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_liked_posts);
        setTitle("View Liked Posts");
        setTitleColor(getColor(R.color.white));
        IssueList = new ArrayList<Issue_Model_Class>();

        rvMyIssues = findViewById(R.id.rvViewLikedIssues);
        rvMyIssues.setHasFixedSize(true);
        LinearLayoutManager linearVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMyIssues.setLayoutManager(linearVertical);
        mviewLikedPostsAdapter = new viewLikedPostsAdapter(viewLikedPosts.this,likedArrayList);

        rvMyIssues.setAdapter(mviewLikedPostsAdapter);
        progressBar = findViewById(R.id.progressMyIssues);

        progressBar.setVisibility(View.VISIBLE);

        getLikedPosts();
    }

    private void getLikedPosts() {
        if (isNetworkAvailable()) {

            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressBar.setVisibility(View.VISIBLE);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, AppConstants.GET_POSTS, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Issue_Model_Class issueModelClass = new Gson().fromJson(jsonObject.toString(), Issue_Model_Class.class);
                            IssueList.add(issueModelClass);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);

                        }
                    }

                    String Url = String.format(AppConstants.UPVOTE_LIST + PrefManager.getUser().getUserId() + "/", PrefManager.getUser().getUserId());
                    final ArrayList<Integer> postArrayList = new ArrayList<>();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONArray t = new JSONArray(response);
                                for (int i = 0; i < t.length(); i++) {
                                    JSONObject object = t.getJSONObject(i);
                                    postArrayList.add(object.getInt("post_id"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            for (Issue_Model_Class IssueObject : IssueList) {
                                for (int i : postArrayList) {
                                    if (IssueObject.getId() == i) {
                                        IssueObject.setCheckLiked(true);
                                        likedArrayList.add(IssueObject);
                                    }
                                }
                            }

                            mviewLikedPostsAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.toString());
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(viewLikedPosts.this);
                    stringRequest.setShouldCache(false);
                    requestQueue.add(stringRequest);
                    progressBar.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley ", error.toString());
                    progressDialog.dismiss();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(viewLikedPosts.this);
            jsonArrayRequest.setShouldCache(false);
            requestQueue.add(jsonArrayRequest);
        } else {

            progressBar.setVisibility(View.GONE);

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
