package com.example.garbagecleanup.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.garbagecleanup.R;
import com.example.garbagecleanup.activity.DisplayImages;
import com.example.garbagecleanup.activity.MainActivity;
import com.example.garbagecleanup.helper.AppConstants;
import com.example.garbagecleanup.helper.GpsUtils;
import com.example.garbagecleanup.helper.MySingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class TAB_2 extends Fragment implements SurfaceHolder.Callback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "TAB_2";
    Camera camera;
    FloatingActionButton FAB_click_photo;

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback pictureCallback;
    GoogleApiClient googleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    String latitude;
    String longitude;
    String locationAddress;

    private boolean isGPS = false;


    public static TAB_2 newInstance()
    {
        TAB_2 fragment = new TAB_2();
        return fragment;

    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_2 , container , false);
        surfaceView=view.findViewById(R.id.SurfaceView);
        surfaceHolder= surfaceView.getHolder();
        FAB_click_photo=(FloatingActionButton)view.findViewById(R.id.FAB_clickpicture);
        FAB_click_photo.setVisibility(View.VISIBLE);


        googleApiClient= new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());



        FAB_click_photo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                camera.takePicture(null,null,pictureCallback);
                FAB_click_photo.setVisibility(View.GONE);



            }
        });

        pictureCallback= new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                final Intent intent = new Intent(getActivity(), DisplayImages.class);
                Bitmap bmp=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                String filePath = tempFileImage(getContext(), rotateBitmap(bmp), "image");
                intent.putExtra("path", filePath);
                intent.putExtra("Longitude",longitude);
                intent.putExtra("Latitude",latitude);
                intent.putExtra("Area Name",locationAddress);
                intent.putExtra("CallingActivity", MainActivity.class.toString());
                Log.i(TAG, "onPictureTaken: " + System.currentTimeMillis());
                intent.putExtra("timestamp", String.valueOf(System.currentTimeMillis()));

                startActivity(intent);
                camera.release();

            }

        };

        return view;
    }

    public static String tempFileImage(Context context, Bitmap bitmap, String name) {

        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
    }

    private File getOutputMediaFile(int mediaTypeImage) {
    return null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        camera=Camera.open();

        Camera.Parameters parameters;

        parameters = camera.getParameters();
        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(60);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }


        camera.startPreview();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case AppConstants.CAMERA_REQUEST_CODE :

                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    surfaceHolder.addCallback(this);
                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                }else
                    Toast.makeText(getContext(),"NEED PERMISSION",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, AppConstants.CAMERA_REQUEST_CODE);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

    }

    @SuppressLint("RestrictedApi")
    public void onStart() {
        super.onStart();

        new GpsUtils(getContext()).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
                googleApiClient.connect();

            }
        });

        FAB_click_photo.setVisibility(View.VISIBLE);

    }

    public void onStop()
    {
      if(googleApiClient.isConnected())
        googleApiClient.disconnect();
      super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       if(ActivityCompat.checkSelfPermission(getContext(),ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           requestPermission();
       }else{
           fusedLocationProviderClient.getLastLocation()
                   .addOnSuccessListener(new OnSuccessListener<Location>() {
                       @Override
                       public void onSuccess(Location location) {
                           latitude = String.valueOf(location.getLatitude());
                           longitude = String.valueOf(location.getLongitude());

                           locationAddress= MySingleton.getAdress(location.getLatitude(),location.getLongitude());
                       }
                   });

       }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},AppConstants.RequestPermissionCode);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap) {
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }



}
