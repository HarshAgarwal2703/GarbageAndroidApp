package com.example.garbagecleanup.click_2_send;

import android.content.SharedPreferences;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.garbagecleanup.AppConstants;
import com.example.garbagecleanup.MySingleton;
import com.example.garbagecleanup.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;


public class DisplayImages extends AppCompatActivity {

    private static final String TAG = "DisplayImages";
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
        final String filePath = getIntent().getStringExtra("path");
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
//            jsonObject.put("Latitude",latitude);
//            jsonObject.put("Longitude",longitude);
            //jsonObject.put("Image", file);
            jsonObject.put("Title", "Bhavya Mc KA BAcha");
            jsonObject.put("Description", "7");
            jsonObject.put("Latitude", latitude);
            jsonObject.put("Longitude", longitude);
            jsonObject.put("Created_date", "2019-09-04T05:00:21.697870Z");
            jsonObject.put("Published_date", "2019-09-04T05:00:21.697870Z");
            jsonObject.put("Author", "7");


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

                SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, AppConstants.POST_IMAGES_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);
                                Log.e(TAG, "onResponse: " + response.toString() );

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.error.VolleyError error) {

                        Log.e(TAG, "onErrorResponse: " + error.getMessage());

                    }
                });

                //smr.addFile("image", filePath);
                //Log.i(TAG, "onClick: "+filePath);
                smr.addStringParam("title", "Bhavya Mc KA BAcha");
                smr.addStringParam("Description", "7");
                smr.addStringParam("latitude", latitude);
                smr.addStringParam("longitude", longitude);
                smr.addStringParam("created_date", "2019-09-04T05:00:21.697870Z");
                smr.addStringParam("published_date", "2019-09-04T05:00:21.697870Z");
                smr.addStringParam("author", "7");

                smr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(DisplayImages.this).addToRequest(smr);
                finish();


            }

        });

        SavePostInGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Draft draft = new Draft(
//                        1,
//                        title,
//                        description,
//                        latitude,
//                        longitude,
//                        timestamp,
//                        BitmapToByte(bitmap)
//                );
//                MySingleton.getInstance(DisplayImages.this).getAppDatabase().draftDAO().insert(draft);

            }
        });

    }

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

    private byte[] BitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        return byteArray;
    }
}
