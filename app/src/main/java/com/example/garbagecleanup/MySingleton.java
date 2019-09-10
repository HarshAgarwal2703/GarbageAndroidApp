package com.example.garbagecleanup;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton mySingleton;
    private RequestQueue requestQueue;
    private static Context context;


    private MySingleton(Context context)
    {
        this.context=context;
        this.requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
         if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
    return requestQueue;
    }

    protected static synchronized  MySingleton getInstance(Context context)
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
