package com.example.garbagecleanup.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.garbagecleanup.database.AppDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.room.Room;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MySingleton {

    private static MySingleton mySingleton;
    private RequestQueue requestQueue;
    private Context context;
    private AppDatabase appDatabase;
    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public MySingleton(Context context) {
        AppDatabase db = Room.databaseBuilder(context,
                                              AppDatabase.class, "database-name"
        ).build();
        this.appDatabase = db;
        this.context = context;
        this.requestQueue = getRequestQueue();
        this.sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized void init(Context context) {
        if (mySingleton == null) {
            mySingleton = new MySingleton(context);
        }
    }

    public Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)//at the end
                    .build();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(AppConstants.ServerURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static MySingleton getInstance() {
        return mySingleton;
    }

    public <T> void addToRequest(Request<T> request) {
        getRequestQueue().add(request);
    }

    public static String getAdress(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(MySingleton.getInstance().context, Locale.getDefault());
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


}
