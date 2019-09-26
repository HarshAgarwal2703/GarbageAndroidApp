package com.example.garbagecleanup.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garbagecleanup.PrefManager;
import com.example.garbagecleanup.R;
import com.example.garbagecleanup.fragments.TAB_2;
import com.example.garbagecleanup.helper.APIInterface;
import com.example.garbagecleanup.helper.MySingleton;
import com.example.garbagecleanup.model.Draft;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class DisplayImages extends AppCompatActivity {

    private static final String TAG = "DisplayImages";
    ImageView displayImage;

    TextView LatitudeText;
    TextView LongitudeText, titleEditText, descriptionEditText;
    FloatingActionButton FAB_SEND_ISSUE;
    Button SavePostInGalleryButton, UpdatePostInGalleryButton;
    SharedPreferences sharedPreferences;
    int numRequests = 0;
    Bitmap bitmap;
    //    String latitude;
//    String longitude;
    private String title, description, latitude, longitude, timestamp, filePath;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);

        displayImage = findViewById(R.id.Display_Image);
        LatitudeText = findViewById(R.id.LatitudeText);
        LongitudeText = findViewById(R.id.LongitudeText);
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
            timestamp = getIntent().getStringExtra("timestamp");
            final File file = new File(filePath);
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            displayImage.setImageBitmap(bitmap);

            if (latitude != null && longitude != null) {
                LatitudeText.setText(latitude);
                LongitudeText.setText(longitude);
            }
        } else {
            Draft draft = new Gson().fromJson(getIntent().getStringExtra("draft"), Draft.class);
            byte[] byteArray = draft.getImage();
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            filePath = TAB_2.tempFileImage(this, bitmap, "image");
            latitude = draft.getLatitude();
            longitude = draft.getLongitude();
            timestamp = draft.getTimestamp();
            title = draft.getTitle();
            description = draft.getDescription();
            titleEditText.setText(title);
            descriptionEditText.setText(description);
            displayImage.setImageBitmap(bitmap);
            LongitudeText.setText(longitude);
            LatitudeText.setText(latitude);
            SavePostInGalleryButton.setVisibility(View.GONE);
            UpdatePostInGalleryButton.setVisibility(View.VISIBLE);

        }
        FAB_SEND_ISSUE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), latitude);
                RequestBody longi = RequestBody.create(MediaType.parse("multipart/form-data"), longitude);
                final RequestBody cre = RequestBody.create(MediaType.parse("multipart/form-data"), "2019-09-04T05:00:21.697870Z");
                final RequestBody pub = RequestBody.create(MediaType.parse("multipart/form-data"), "2019-09-04T05:00:21.697870Z");
//                RequestBody author = RequestBody.create(MediaType.parse("multipart/form-data"), 1);
                Retrofit retrofit = MySingleton.getInstance().getRetrofitInstance();

                final APIInterface apiInterface = retrofit.create(APIInterface.class);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            apiInterface.uploadImage(body, title1, desc, Double.parseDouble(latitude), Double.parseDouble(longitude), cre, pub, 1).execute();
                            finish();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }

        });

        SavePostInGalleryButton.setOnClickListener(new View.OnClickListener() {
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
                        BitmapToByte(bitmap)
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
                        BitmapToByte(bitmap)
                );
                new UpdateDraft(DisplayImages.this).execute(draft);

            }
        });

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

    private static class InsertDraft extends AsyncTask<Draft, Void, Void> {
        private Context context;

        @Override
        protected Void doInBackground(Draft... drafts) {
            MySingleton.getInstance().getAppDatabase().draftDAO().insert(drafts[0]);
            Log.d(TAG, "doInBackground: " + "inserted");
//            Toast.makeText(context,"INSERTED TO DATABASE",Toast.LENGTH_LONG).show();
            return null;
        }

        public InsertDraft(Context context) {
            this.context = context;
        }
    }

    private static class UpdateDraft extends AsyncTask<Draft, Void, Void> {
        private Context context;

        @Override
        protected Void doInBackground(Draft... drafts) {
            MySingleton.getInstance().getAppDatabase().draftDAO().update(drafts[0].getDescription(), drafts[0].getTitle(), drafts[0].getTimestamp());
            return null;
        }

        public UpdateDraft(Context context) {
            this.context = context;
        }
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

}
