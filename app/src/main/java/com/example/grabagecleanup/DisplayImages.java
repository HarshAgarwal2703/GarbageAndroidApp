package com.example.grabagecleanup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.sip.SipSession;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DisplayImages extends AppCompatActivity {

    ImageView displayImage;

    TextView LatitudeText;
    TextView LongitudeText;
    FloatingActionButton FAB_SEND_ISSUE;
    String JSON_URL="http://bhavya17ahir.pythonanywhere.com/postAPI/";
    int numRequests = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);


        displayImage=findViewById(R.id.Display_Image);
        LatitudeText=findViewById(R.id.LatitudeText);
        LongitudeText=findViewById(R.id.LongitudeText);
        FAB_SEND_ISSUE=findViewById(R.id.floatingActionButton);




        String filePath=getIntent().getStringExtra("path");
        final String latitude=getIntent().getStringExtra("Latitude");
        final String longitude=getIntent().getStringExtra("Longitude");
        File file = new File(filePath);
        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        Bitmap bmp= rotateBitmap(bitmap);
        displayImage.setImageBitmap(bmp);

        if(latitude!=null && longitude!=null) {
            LatitudeText.setText(latitude);
            LongitudeText.setText(longitude);
        }

        final JSONObject jsonObject=new JSONObject();
        try {
//            jsonObject.put("displayImage", ImageToString(bitmap));
//            jsonObject.put("Latitude",latitude);
//            jsonObject.put("Longitude",longitude);
            jsonObject.put("title","Bhavya Mc KA BAcha");
            jsonObject.put("Description","7");
            jsonObject.put("latitude",latitude);
            jsonObject.put("longitude",longitude);
            jsonObject.put("created_date","2019-09-04T05:00:21.697870Z");
            jsonObject.put("published_date","2019-09-04T05:00:21.697870Z");
            jsonObject.put("author","7");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject t=new JSONObject();
        try {
            t.put("name","name");
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

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, JSON_URL,jsonObject,
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

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(DisplayImages.this).addToRequest(jsonObjectRequest);


            }

        });



    }

    private Bitmap rotateBitmap(Bitmap bitmap) {
        int h=bitmap.getHeight();
        int w=bitmap.getWidth();

        Matrix matrix=new Matrix();
        matrix.setRotate(90);

        return  Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true);
    }

    private  String ImageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

}
