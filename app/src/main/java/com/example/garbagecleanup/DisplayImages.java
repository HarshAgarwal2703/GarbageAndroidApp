package com.example.garbagecleanup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class DisplayImages extends AppCompatActivity {

    ImageView displayImage;

    TextView LatitudeText;
    TextView LongitudeText;
    FloatingActionButton FAB_SEND_ISSUE;
    Button SavePostInGalleryButton;
    SharedPreferences sharedPreferences;
    int numRequests = 0;
    Bitmap bitmap;
    String latitude;
    String longitude;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);


        displayImage = findViewById(R.id.Display_Image);
        LatitudeText = findViewById(R.id.LatitudeText);
        LongitudeText = findViewById(R.id.LongitudeText);
        FAB_SEND_ISSUE = findViewById(R.id.floatingActionButton);
        SavePostInGalleryButton = findViewById(R.id.SaveInGalleryButton);


        sharedPreferences = getPreferences(MODE_PRIVATE);
        String filePath = getIntent().getStringExtra("path");
          latitude = getIntent().getStringExtra("Latitude");
         longitude = getIntent().getStringExtra("Longitude");
        File file = new File(filePath);
         bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        Bitmap bmp = rotateBitmap(bitmap);
        displayImage.setImageBitmap(bmp);

        if (latitude != null && longitude != null) {
            LatitudeText.setText(latitude);
            LongitudeText.setText(longitude);
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("displayImage", ImageToString(bitmap));
//            jsonObject.put("Latitude",latitude);
//            jsonObject.put("Longitude",longitude);
            jsonObject.put("title", "Bhavya Mc KA BAcha");
            jsonObject.put("Description", "7");
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("created_date", "2019-09-04T05:00:21.697870Z");
            jsonObject.put("published_date", "2019-09-04T05:00:21.697870Z");
            jsonObject.put("author", "7");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject t = new JSONObject();
        try {
            t.put("name", "name");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d("DGFHdfh", jsonObject.toString());
        Log.e("DGFHdfh", jsonObject.toString());


        FAB_SEND_ISSUE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB_SEND_ISSUE.setVisibility(View.GONE);
                Log.d("DGFHdfh", jsonObject.toString());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.POST_IMAGES_URL, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("DGFHdfh", jsonObject.toString());
                                Log.e("DGFHdfh", response.toString());


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("DGFHdfh", error.toString());

                            }
                        });

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(DisplayImages.this).addToRequest(jsonObjectRequest);

                finish();

            }

        });

        SavePostInGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //saveToDB();

            }
        });




    }
//    private void saveToDB(){
//        byte[] ImageBytes = BitmapToByte(bitmap);
//        SQLiteDatabase database = new SampleSQLiteDBHelper(DisplayImages.this).getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(SampleSQLiteDBHelper.IMAGE,ImageBytes);
//        values.put(SampleSQLiteDBHelper.DATE, System.currentTimeMillis());
//        values.put(SampleSQLiteDBHelper.LATITUDE, latitude);
//        values.put(SampleSQLiteDBHelper.LONGITUDE, longitude);
//
//        long newRowId = database.insert(SampleSQLiteDBHelper.DATABASE_NAME, null, values);
//
//        Toast.makeText(DisplayImages.this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
//    }

    private Bitmap rotateBitmap(Bitmap bitmap) {
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    private byte[] BitmapToByte(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        return byteArray;
    }
}
