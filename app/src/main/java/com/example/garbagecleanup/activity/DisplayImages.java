package com.example.garbagecleanup.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.garbagecleanup.PrefManager;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.fragments.TAB_2;
import com.example.garbagecleanup.helper.APIInterface;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.Draft;
import com.example.garbagecleanup.model.Issue_Model_Class;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class DisplayImages extends AppCompatActivity {

    private static final String TAG = "DisplayImages";
    ImageView displayImage;
    double latitudeArray;
    double longitudeArray;
    int radius = 100;
    TextView AreaTextView;
    TextView titleEditText, descriptionEditText;
    FloatingActionButton FAB_SEND_ISSUE;
    Button SavePostInGalleryButton, UpdatePostInGalleryButton;
    SharedPreferences sharedPreferences;
    int numRequests = 0;
    Bitmap bitmap;
    private ArrayList<Issue_Model_Class> IssueList= new ArrayList<Issue_Model_Class>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    //    String latitude;
//    String longitude;
    private String title, description, latitude, longitude, timestamp, filePath, AreaName;
    private long mLastClickTime = 0;

    public static String parseTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(new Date());

    }

    public static Intent makeIntent(Context context, Draft draft) {
        Intent intent = new Intent(context, DisplayImages.class);

        if (context instanceof ViewDrafts) {
            intent.putExtra("draft", new Gson().toJson(draft));
            intent.putExtra("CallingActivity", ViewDrafts.class.toString());
        } else {
            intent.putExtra("CallingActivity", MainActivity.class.toString());
        }
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);

        displayImage = findViewById(R.id.Display_Image);
        AreaTextView = findViewById(R.id.AreaText);
        FAB_SEND_ISSUE = findViewById(R.id.floatingActionButton);
        SavePostInGalleryButton = findViewById(R.id.SaveInGalleryButton);
        titleEditText = findViewById(R.id.TitleEditText);
        descriptionEditText = findViewById(R.id.DescriptionEditText);
        UpdatePostInGalleryButton = findViewById(R.id.UpdatePostInGalleryButton);

        sharedPreferences = MySingleton.getInstance().getSharedPreferences();

        if (getIntent().getStringExtra("CallingActivity").equals(MainActivity.class.toString())) {
            filePath = getIntent().getStringExtra("path");
            latitude = getIntent().getStringExtra("Latitude");
            longitude = getIntent().getStringExtra("Longitude");
            timestamp = parseTime();
            AreaName = getIntent().getStringExtra("Area Name");
            final File file = new File(filePath);
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            displayImage.setImageBitmap(bitmap);

            if (latitude != null && longitude != null) {
                AreaTextView.setText(AreaName);
            }
        } else {
            Draft draft = new Gson().fromJson(getIntent().getStringExtra("draft"), Draft.class);
            byte[] byteArray = draft.getImage();
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            filePath = TAB_2.tempFileImage(this, bitmap, "image");
            latitude = draft.getLatitude();
            longitude = draft.getLongitude();
            timestamp = draft.getTimestamp();
            AreaName = MySingleton.getAdress(Double.parseDouble(latitude), Double.parseDouble(longitude));
            title = draft.getTitle();
            description = draft.getDescription();
            titleEditText.setText(title);
            descriptionEditText.setText(description);
            displayImage.setImageBitmap(bitmap);
            AreaTextView.setText(AreaName);
            SavePostInGalleryButton.setVisibility(View.GONE);
            UpdatePostInGalleryButton.setVisibility(View.VISIBLE);

        }
        FAB_SEND_ISSUE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                checkIfExists();

            }

        });

        SavePostInGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }

                title = titleEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                Draft draft = new Draft(
                        PrefManager.getUser().getUserId(),
                        title,
                        description,
                        latitude,
                        longitude,
                        timestamp,
                        BitmapToByte(bitmap),
                        AreaName
                );
                new InsertDraft(DisplayImages.this).execute(draft);

            }
        });

        UpdatePostInGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                Draft draft = new Draft(
                        PrefManager.getUser().getUserId(),
                        title,
                        description,
                        latitude,
                        longitude,
                        timestamp,
                        BitmapToByte(bitmap),
                        AreaName
                );
                new UpdateDraft(DisplayImages.this).execute(draft);

            }
        });

    }

    private void  sendPost(){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        title = titleEditText.getText().toString();
        description = descriptionEditText.getText().toString();
        if (title.equals("")) {
            title = "Garbage";
        }
        if (description.equals("")) {
            description = "I found garbage in my area";
        }
        File file1 = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        final MultipartBody.Part body = MultipartBody.Part.createFormData("image", file1.getName(), requestBody);
        final RequestBody title1 = RequestBody.create(MediaType.parse("multipart/form-data"), title);
        final RequestBody desc = RequestBody.create(MediaType.parse("multipart/form-data"), description);
//        RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), latitude);
//        RequestBody longi = RequestBody.create(MediaType.parse("multipart/form-data"), longitude);
        final RequestBody cre = RequestBody.create(MediaType.parse("multipart/form-data"), timestamp);
        final RequestBody pub = RequestBody.create(MediaType.parse("multipart/form-data"), parseTime());
//                RequestBody author = RequestBody.create(MediaType.parse("multipart/form-data"), 1);
        Retrofit retrofit = MySingleton.getInstance().getRetrofitInstance();

        final APIInterface apiInterface = retrofit.create(APIInterface.class);
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Log.e(TAG, "run: " + latitude);
                    Log.e(TAG, "run: " + longitude);
                    Log.e(TAG, "run: " + pub);
                    Log.e(TAG, "run: " + cre);
                    Log.e(TAG, "run: " + title1);
                    Log.e(TAG, "run: " + body);
                    Log.e(TAG, "run: " + PrefManager.getUser().getUserId());
                    apiInterface.uploadImage(body, title1, desc, Double.parseDouble(latitude), Double.parseDouble(longitude), cre, pub, PrefManager.getUser().getUserId()).execute();
                    finish();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    private void checkIfExists() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        latitudeArray = location.getLatitude();
                        longitudeArray = location.getLongitude();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("latitude", Double.parseDouble(latitude));
                            jsonObject.put("longitude", Double.parseDouble(longitude));
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

    private void showPreviousPost(final Issue_Model_Class response) {

        Log.e(TAG, "showPreviousPost: " + response );
        final Dialog yourDialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.check_exist_dialog, (ViewGroup) yourDialog.findViewById(R.id.checkIfExistsDialog));
        Button btnLike = layout.findViewById(R.id.dialogButtonLike);
        Button btnCancel = layout.findViewById(R.id.dialogButtonCancel);
        Button btnSendPost = layout.findViewById(R.id.dialogButtonPost);

        TextView TitleTextView = layout.findViewById(R.id.TitleTextView);
        TextView descriptionTexView = layout.findViewById(R.id.DescriptionTextView);
        TextView UpvotesTextView = layout.findViewById(R.id.UpvotesTextView);
        ImageView imageView = layout.findViewById(R.id.PostImageView);
        TextView areaTexView = layout.findViewById(R.id.AreaTextView);
        TextView tvStatus = layout.findViewById(R.id.tvStatus);

        TitleTextView.setText(response.getTitle());
        descriptionTexView.setText(response.getDescription());
        UpvotesTextView.setText(String.valueOf(response.getVotes()));
        areaTexView.setText(getAddress(Double.parseDouble(response.getLatitude()),Double.parseDouble(response.getLongitude())));
        Glide.with(this).load(response.getImageUrl()).into(imageView);
        tvStatus.setText(response.getStatus());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yourDialog.dismiss();
            }
        });

        btnSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPost();
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost(response.getId());
                yourDialog.dismiss();
                finish();
            }
        });
        yourDialog.setContentView(layout);
        yourDialog.show();
    }

    private void likePost(int PostId) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", PrefManager.getUser().getUserId());
            jsonObject.put("post_id", PostId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.UPVOTE_POST, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "onResponse: " + response.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void getData(JSONArray jsonArray) {


        Log.e(TAG, "getData: " + jsonArray );
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, AppConstants.FILTER_POST, jsonArray,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e(TAG, "onResponse: " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                jsonObject.put("image", "https://bhavya17ahir.pythonanywhere.com" + jsonObject.get("image"));
                                Issue_Model_Class issueModelClass = new Gson().fromJson(jsonObject.toString(), Issue_Model_Class.class);
                                IssueList.add(issueModelClass);

                                Log.e(TAG, "onResponse: " + IssueList );


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (IssueList.size() != 0 ) {
                            Log.e(TAG, "onResponse: " + "post exists" );
                            Log.e(TAG, "FDBDFBFBBGBG: " + IssueList.get(0) );
                            showPreviousPost(IssueList.get(0));

                        } else {
                            Log.e(TAG, "onResponse: " + "to send post" );
                            sendPost();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.toString() );

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);


    }

    public String getAddress(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(DisplayImages.this, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getAddressLine(0));

                result = sb.toString();

            }
        } catch (IOException e) {
        }

        return result;
    }

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    private byte[] BitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        return byteArray;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class InsertDraft extends AsyncTask<Draft, Void, Void> {
        private Context context;

        public InsertDraft(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Draft... drafts) {
            MySingleton.getInstance().getAppDatabase().draftDAO().insert(drafts[0]);
            Log.d(TAG, "doInBackground: " + "inserted");
//            Toast.makeText(context,"INSERTED TO DATABASE",Toast.LENGTH_LONG).show();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SavePostInGalleryButton.setEnabled(false);
            SavePostInGalleryButton.setText("Saved to Draft");
            titleEditText.setEnabled(false);
            descriptionEditText.setEnabled(false);
            Toast.makeText(context, "Saved To Drafts", Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateDraft extends AsyncTask<Draft, Void, Void> {
        private Context context;

        public UpdateDraft(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Draft... drafts) {
            MySingleton.getInstance().getAppDatabase().draftDAO().update(drafts[0].getDescription(), drafts[0].getTitle(), drafts[0].getTimestamp());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Updated Draft", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

