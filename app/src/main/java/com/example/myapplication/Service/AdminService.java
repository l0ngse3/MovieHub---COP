package com.example.myapplication.Service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.MultiPartRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.Genre;
import com.example.myapplication.Model.ShareViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AdminService {
    private RequestQueueUltil requestQueueUltil;
    private Context context;
    private ShareViewModel viewModel;

    private int count = 0;
    private List<Genre> genreList = new ArrayList<>();

    public AdminService(Context context) {
        this.context = context;
        requestQueueUltil = RequestQueueUltil.getInstance(context);

    }

    public void postFilmUpdate(Film f, final Context context, final TaskDone taskDone) {
//        queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = APIConnectorUltils.HOST_NAME + "Film/update";
//        Log.d("Mine Request", "onRequest" + url);
        try {
            JSONObject object = new JSONObject(new Gson().toJson(f));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Mine Response", "Response : " + response);
                            try {
                                Toast.makeText(context, response.getString("announce"), Toast.LENGTH_SHORT).show();
                                taskDone.done(response.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Mine ERROR", "onErrorResponse: " + error.toString());
                            Toast.makeText(context, "Connection to server have problems!", Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueueUltil.addToRequestQueue(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Mine ERROR", "Exeption: " + e.toString());
        }
    }

    public void postFilmAdd(Film f, final TaskDone taskDone) {
        String url = APIConnectorUltils.HOST_NAME + "Film/Add";
        try {
            JSONObject object = new JSONObject(new Gson().toJson(f));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    taskDone.done(response.toString());
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Mine add film", "onErrorResponse: "+error.toString());
                }
            });
            requestQueueUltil.addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteFilm(Film f, final TaskDone taskDone)
    {
        String url = APIConnectorUltils.HOST_NAME + "Film/Delete";
        try {
            JSONObject object = new JSONObject(new Gson().toJson(f));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    taskDone.done(response.toString());
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Mine add film", "onErrorResponse: "+error.toString());
                }
            });
            requestQueueUltil.addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String filePath, String fileName, final TaskDone taskDone){
        String url = APIConnectorUltils.HOST_NAME + "Film/upload";
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                taskDone.done(response);
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Mine upload", "onErrorResponse: "+error.toString());
            }
        });

        request.addFile("file", filePath);
        request.addStringParam("name", fileName);
//        request.setFixedStreamingMode(true);
        requestQueueUltil.addToRequestQueue(request);
    }
//******************************************************
}
