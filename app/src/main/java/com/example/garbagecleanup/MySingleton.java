package com.example.garbagecleanup;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.garbagecleanup.database.AppDatabase;

import androidx.room.Room;

public class MySingleton {

    private static MySingleton mySingleton;
    private RequestQueue requestQueue;
    private static Context context;
    private AppDatabase appDatabase;
    private SharedPreferences sharedPreferences;

    public MySingleton(Context context)
    {
        AppDatabase db = Room.databaseBuilder(context,
                                              AppDatabase.class, "database-name"
        ).build();
        this.appDatabase = db;
        this.context=context;
        this.requestQueue=getRequestQueue();
        this.sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    private RequestQueue getRequestQueue() {
         if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
    return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context)
    {
        if(mySingleton==null){
            mySingleton=new MySingleton(context);
        }
            return mySingleton;

    }

    public <T> void addToRequest(Request<T> request)
    {
        getRequestQueue().add(request);
    }

}
