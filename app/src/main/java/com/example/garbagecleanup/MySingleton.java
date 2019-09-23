package com.example.garbagecleanup;

import android.content.Context;

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

    public MySingleton(Context context)
    {
        AppDatabase db = Room.databaseBuilder(context,
                                              AppDatabase.class, "database-name"
        ).build();
        this.appDatabase = db;
        this.context=context;
        this.requestQueue=getRequestQueue();
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
