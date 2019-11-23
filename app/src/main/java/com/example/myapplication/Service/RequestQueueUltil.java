package com.example.myapplication.Service;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueUltil extends Application {
    private static RequestQueueUltil instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private RequestQueueUltil(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestQueueUltil getInstance(Context context) {
        if (instance == null) {
            instance = new RequestQueueUltil(context);
            Log.d("RequestQueueUltil", "getInstance: new ins");
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
