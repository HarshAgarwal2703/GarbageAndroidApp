package com.example.garbagecleanup.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.garbagecleanup.PrefManager;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.adapter.FeedRecyclerAdapter;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.model.Issue_Model_Class;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TAB_1 extends Fragment {

    private static final String TAG = "TAB_1";
    int radius = 50;
    private double latitude;
    private double longitude;
    private RecyclerView recyclerView;
    private FeedRecyclerAdapter feedRecyclerAdapter;
    private List<Issue_Model_Class> IssueList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton btnFilter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView tvNoInternet;
    private ProgressBar progressBar;
    private TextView textViewFilter;

    public static TAB_1 newInstance() {
        TAB_1 fragment = new TAB_1();
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_1, container, false);
        IssueList = new ArrayList<Issue_Model_Class>();
        recyclerView = view.findViewById(R.id.RecyclerTab1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearVertical);

        tvNoInternet = view.findViewById(R.id.tvNoInternet);
        progressBar = view.findViewById(R.id.progressTab1);
        feedRecyclerAdapter = new FeedRecyclerAdapter(getContext(), IssueList);
        recyclerView.setAdapter(feedRecyclerAdapter);
        btnFilter = view.findViewById(R.id.FilterButton);

        swipeRefreshLayout = view.findViewById(R.id.SwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getSampleData();
                getObjectArray();
//                swipeRefreshLayout.setRefreshing(false);

            }
        });


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });

        return view;


    }

    private void showDialog() {
        Toast.makeText(getActivity(), "APPLY FILTER ", Toast.LENGTH_SHORT).show();
        final Dialog yourDialog = new Dialog(getContext());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.filter_dialog, (ViewGroup) yourDialog.findViewById(R.id.filterDialog));
        final SeekBar seekBar = layout.findViewById(R.id.filterSeekBar);
        Button btnApply = layout.findViewById(R.id.dialogButtonApply);
        Button btnCancel = layout.findViewById(R.id.dialogButtonCancel);
        textViewFilter = layout.findViewById(R.id.FilterTextView);

        seekBar.setMax(1000);
        seekBar.setProgress(5);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewFilter.setText(progress * 10 + " M");
                TAB_1.this.radius = progress*10;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getObjectArray();
                yourDialog.dismiss();


            }


        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yourDialog.dismiss();
            }
        });
        yourDialog.setContentView(layout);
        yourDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getObjectArray();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getObjectArray() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        TAB_1.this.latitude = location.getLatitude();
                        TAB_1.this.longitude = location.getLongitude();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("latitude", latitude);
                            jsonObject.put("longitude", longitude);
                            jsonObject.put("radius", radius);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        getData(jsonArray);


                    }
                });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void getData(JSONArray jsonArray) {
        if (isNetworkAvailable()) {
            swipeRefreshLayout.setRefreshing(true);
            tvNoInternet.setVisibility(View.GONE);
            IssueList.clear();
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());

            progressBar.setVisibility(View.VISIBLE);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, AppConstants.FILTER_POST, jsonArray, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    swipeRefreshLayout.setRefreshing(false);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            jsonObject.put("image", "https://bhavya17ahir.pythonanywhere.com" +jsonObject.get("image") );
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
                                    }
                                }
                            }

                            feedRecyclerAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.toString());
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    stringRequest.setShouldCache(false);
                    requestQueue.add(stringRequest);
                    feedRecyclerAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley ", error.toString());
                    progressDialog.dismiss();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            jsonArrayRequest.setShouldCache(false);
            requestQueue.add(jsonArrayRequest);
        } else {

            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            tvNoInternet.setVisibility(View.VISIBLE);

        }

    }

}
