package com.example.myapplication.Ui.Fragment.Client;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Service.RequestQueueUltil;
import com.example.myapplication.Ui.Adapter.Client.GenreHomeAdapter;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.Genre;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    RecyclerView rcyGenreHome;
    List<Genre> genreList = new ArrayList<>();
    GenreHomeAdapter genreHomeAdapter;
    String textQuery;


    Context context = getContext();
    HomeFragment fragment = this;
    ShareViewModel viewModel;
    String username;
    SwipeRefreshLayout swipeRefreshHome;
    View viewHome;
    ShimmerFrameLayout mShimmerViewContainer;


    public HomeFragment(String textQuery) {
//        this.genreHomeAdapter.getFilter().filter(textQuery);
        this.textQuery = textQuery;
    }

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        process();
    }

    private void process() {
        swipeRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                genreList.clear();
                genreHomeAdapter.notifyDataSetChanged();
                init(viewHome);
            }
        });


    }

    private void init(View view) {
        viewHome = view;
        rcyGenreHome = view.findViewById(R.id.rcyGenreHome);
        swipeRefreshHome = view.findViewById(R.id.swipeRefreshHome);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();

        final List<String> genreListRequest = new ArrayList<>();
        genreListRequest.add("Action");//3
        genreListRequest.add("Adventure");//2
        genreListRequest.add("Comedy");//1
        genreListRequest.add("Cartoon");//
        genreListRequest.add("Horror");//5
        genreListRequest.add("Romance");//
        genreListRequest.add("Science & Fiction");//6

        viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        username = viewModel.getUsername().getValue();

        for (final String genre : genreListRequest) {
            String url = APIConnectorUltils.HOST_STORAGE_FILM + "Genre/";
            Log.d("Mine films", url);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Genre gen = new Genre();
                                try {
                                    JSONObject object = new JSONObject(response);
                                    gen.setNameGenre(object.getString("genre"));
                                    Log.d("Mine films", gen.getNameGenre());
                                    Type type = new TypeToken<List<Film>>() {
                                    }.getType();
                                    gen.setList((List<Film>) new Gson().fromJson(object.getString("list"), type));

                                    if (gen.getList().size() > 0) {
                                        genreList.add(gen);

                                        if (genreHomeAdapter == null) {
                                            Log.d("Mine films", "Create new");
                                            genreHomeAdapter = new GenreHomeAdapter(genreList, fragment);
                                            rcyGenreHome.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                            rcyGenreHome.setAdapter(genreHomeAdapter);
                                        } else {
                                            Log.d("Mine films", "reuse");
                                            genreHomeAdapter.notifyItemInserted(genreList.size());
                                            mShimmerViewContainer.stopShimmer();
                                            mShimmerViewContainer.setVisibility(View.GONE);
                                        }
//                                    Log.d("Mine film genre", response);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                swipeRefreshHome.setRefreshing(false);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mShimmerViewContainer.stopShimmer();
                                mShimmerViewContainer.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Missing data film.", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("genre", genre);
                        return params;
                    }
                };

                RequestQueueUltil requestQueueUltil = RequestQueueUltil.getInstance(getContext());
                requestQueueUltil.addToRequestQueue(request);
            }


        //
    }

}
