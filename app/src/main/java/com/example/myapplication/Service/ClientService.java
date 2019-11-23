package com.example.myapplication.Service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Account;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.FilmWatched;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Ui.Activity.Admin.AdminActivity;
import com.example.myapplication.Ui.Activity.Client.FilmPlayActivity;
import com.example.myapplication.Ui.Activity.Client.HomePageActivity;
import com.example.myapplication.Ui.Activity.MainActivity;
import com.example.myapplication.Ui.Adapter.Client.FilmSavedAdapter;
import com.example.myapplication.Ui.Adapter.Client.SwipeToDeleteCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ClientService {
    RequestQueueUltil requestQueueUltil;
    Profile profile;


    public ClientService(Context context) {
        requestQueueUltil = RequestQueueUltil.getInstance(context);
    }

    //simple poster
    public void simplePoster(String url, Object json_data, final TaskDone taskDone)
    {
        try {
            JSONObject object = new JSONObject(new Gson().toJson(json_data));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            taskDone.done(response.toString());
                        }
                    },
                    null);
            requestQueueUltil.addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //simple getter
    public void simpleGetter(String url, final TaskDone taskDone)
    {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        taskDone.done(response);
                        Log.d("Mine film watched", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Mine film id watched", "onErrorResponse: "+error.toString() );
                    }
                });
        request.setShouldCache(false);
        requestQueueUltil.addToRequestQueue(request);
    }
    //get profle
    public void getInfoProfile(String username, final TaskDone taskDone)
    {
        String url = APIConnectorUltils.HOST_NAME + "Profile/" + username;
        Log.d("Mine profile", "loadInforAccount: "+url);
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        taskDone.done(response);
                        Log.d("Mine Profile 2", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Mine profile", "onErrorResponse: "+error.toString() );
                    }
                });
        request.setShouldCache(false);
        requestQueueUltil.addToRequestQueue(request);
    }

    //get film watched
    public void getIdFilmWatched(String username, final TaskDone taskDone)
    {
        String url = APIConnectorUltils.HOST_STORAGE_FILM + "Watched/" + username;
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        taskDone.done(response);
                        Log.d("Mine film watched", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Mine film id watched", "onErrorResponse: "+error.toString() );
                    }
                });
        request.setShouldCache(false);
        requestQueueUltil.addToRequestQueue(request);
    }

    //get current prohress of film
    public void getCurrentProgress(String username, String film_id, final TaskDone taskDone)
    {
        String url = APIConnectorUltils.HOST_STORAGE_FILM + "Watched/GetCurrentProgress?username=" + username + "&" + "filmId=" + film_id;
        Log.d("Mine Current progress", "process: " + url);
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        taskDone.done(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Mine current progress", "onErrorResponse: "+error.toString() );
                    }
                });
        request.setShouldCache(false);
        requestQueueUltil.addToRequestQueue(request);
    }

    //get film by id
    public void getFilmById(String id, final TaskDone taskDone)
    {
        String url = APIConnectorUltils.HOST_STORAGE_FILM + "id/" + id;
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Mine film id infor", response);
                        taskDone.done(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Mine film with id", "onErrorResponse: "+error.toString() );
                    }
                });
        request.setShouldCache(false);
        requestQueueUltil.addToRequestQueue(request);
    }

    //get film saved
    public void getFilmSaved(String username, final TaskDone taskDone)
    {
        String url = APIConnectorUltils.HOST_STORAGE_FILM + "Saved/" + username;
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        taskDone.done(response);
                        Log.d("Mine Film saved", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Mine saved film", "onErrorResponse: "+error.toString());
                    }
                });
        request.setShouldCache(false);
        requestQueueUltil.addToRequestQueue(request);
    }

    // login service
    public void postLogin(final String username, String password, final MainActivity context) {

//        queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = APIConnectorUltils.HOST_NAME + "AuthorService/authors";
//        Log.d("Mine Request", "onRequest" + url);
        JSONObject object = new JSONObject();
        try {
            object.put("username", username);
            object.put("password", password);

//            Log.d("Mine Response", "data : " + object);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean result = Boolean.parseBoolean(response.getString("result"));
                                int role = Integer.parseInt(response.getString("role"));
                                if (result && role==0) {
                                    Toast.makeText(context, "Login successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, HomePageActivity.class);
                                    intent.putExtra("username", username);
                                    context.startActivity(intent);
                                    context.finish();
                                }
                                else if(result && role==1){
//                                    Toast.makeText(context, "Login failure!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, AdminActivity.class);
                                    intent.putExtra("username", username);
                                    context.startActivity(intent);
                                    context.finish();
                                }
                                else {
                                    Toast.makeText(context, "Login failure!", Toast.LENGTH_SHORT).show();
                                }
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
            requestQueueUltil.addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {
            Log.d("Mine ERROR", "Exeption: " + e.toString());
        }
    }

    //register service***************************************************
    public void postRegister(Account account, final MainActivity context) {
//        queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = APIConnectorUltils.HOST_NAME + "AuthorService/register";
//        Log.d("Mine Request", "onRequest" + url);

        final String username = account.getUsername();
        String password = account.getPassword();

        JSONObject object = new JSONObject();
        try {
            object.put("username", username);
            object.put("password", password);
            object.put("role", account.getRole());

//            Log.d("Mine Response", "data : " + object);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Mine Response", "Response : " + response);
                            try {
                                if (Boolean.parseBoolean(response.getString("result"))) {
                                    Toast.makeText(context, "Register successfully!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, HomePageActivity.class);
                                    intent.putExtra("username", username);
                                    context.startActivity(intent);
                                    context.finish();
                                } else {
                                    Toast.makeText(context, response.getString("announce"), Toast.LENGTH_SHORT).show();
                                }
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

//            queue.start();
//            queue.add(request);
            requestQueueUltil.addToRequestQueue(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Mine JSONException", "Exeption: " + e.toString());
        }

    }

    //Update info account*****************************************************
    public void postInfoAccount(Profile profile, final Context context) {
        String url = APIConnectorUltils.HOST_NAME + "Profile/updateInfo";
        JSONObject object = new JSONObject();
        try {
            object.put("username", profile.getUsername());
            object.put("fistName", profile.getFistName());
            object.put("lastName", profile.getLastName());
            object.put("image", profile.getImage());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Mine Response", "Response : " + response);
                            try {
                                Toast.makeText(context, response.getString("announce"), Toast.LENGTH_SHORT).show();
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

//            queue.start();
//            queue.add(request);
            requestQueueUltil.addToRequestQueue(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Mine ERROR", "Exeption: " + e.toString());
        }

    }

    public void postUploadImage(String filePath, String fileName, final TaskDone taskDone){
            String url = APIConnectorUltils.HOST_NAME + "Profile/upload";
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
    // put info film *******************************************************************

    //update film views
    public void updateFilmViews(Film film, final Context context)
    {
//        queue = Volley.newRequestQueue(context);
        String url = APIConnectorUltils.HOST_STORAGE_FILM + "Update/" + film.getId_film();
        Log.d("Mine update views ", film.getFilm_views());
        StringRequest request = new StringRequest(Request.Method.PUT, url, null, null);
//        queue.start();
//        queue.add(request);
        requestQueueUltil.addToRequestQueue(request);
    }

    //update current progress of film
    public void updateFilmWatched(final FilmWatched filmWatched, final FilmPlayActivity context)
    {
//        queue = Volley.newRequestQueue(context);
        String url = APIConnectorUltils.HOST_STORAGE_FILM + "UpdateWatchedFilm/";
        Log.d("Mine Current progress", url);

        try {
            JSONObject object = new JSONObject(new Gson().toJson(filmWatched));
            Log.d("Mine updateFilmWatched", filmWatched.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    context.finish();
                }
            }, null);
//            queue.start();
//            queue.add(request);
            requestQueueUltil.addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //****************************************************


}
