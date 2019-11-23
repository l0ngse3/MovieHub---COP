package com.example.myapplication.Ui.Fragment.Admin;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.Genre;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.R;
import com.example.myapplication.Service.RequestQueueUltil;
import com.example.myapplication.Ui.Adapter.Admin.ManageCommentAdapter;
import com.example.myapplication.Ui.Adapter.Admin.ManageFilmAdapter;
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


public class ManageCommentFragment extends Fragment {

    private RecyclerView rcyAdminFilm;
    private Spinner spinner;
    private Context context;
    private ManageCommentFragment fragment;
    private ManageCommentAdapter manageCommentAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    RequestQueueUltil requestQueueUltil;
    SwipeRefreshLayout swipeRefreshHome;
    View viewHome;
    ImageView btnUploadFilm;

    private List<Genre> genreList;
    private List<Film> filmList;
    private ShareViewModel viewModel;

    public ManageCommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        process();
    }

    private void process() {
        swipeRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                genreList.clear();
                manageCommentAdapter.notifyDataSetChanged();
                init(viewHome);
            }
        });
        //////////////////////////////////
    }


    private void init(View view) {
        context = view.getContext();
        spinner = view.findViewById(R.id.spinnerGenre);
        rcyAdminFilm = view.findViewById(R.id.listAdminFilm);
        rcyAdminFilm.setVisibility(View.GONE);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();
        swipeRefreshHome = view.findViewById(R.id.swipeRefreshHome);
        viewModel = ViewModelProviders.of(this).get(ShareViewModel.class);

        genreList = new ArrayList<>();
        fragment = this;
        filmList = new ArrayList<>();
        requestQueueUltil = RequestQueueUltil.getInstance(getContext());
        viewHome = view;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.genres_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //////////////////////////////////////GET DATA////////////////////////////////
        final List<String> genreListRequest = new ArrayList<>();
        genreListRequest.add("Action");//3
        genreListRequest.add("Adventure");//2
        genreListRequest.add("Comedy");//1
        genreListRequest.add("Cartoon");//
        genreListRequest.add("Horror");//5
        genreListRequest.add("Romance");//
        genreListRequest.add("Science & Fiction");//6


        for (final String genre : genreListRequest)
        {
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
                                    filmList.addAll(gen.getList());
                                    if (manageCommentAdapter == null) {
                                        Log.d("Mine films", "Create new");
                                        manageCommentAdapter = new ManageCommentAdapter(filmList , fragment);
                                        rcyAdminFilm.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                        rcyAdminFilm.setAdapter(manageCommentAdapter);
                                    } else {
                                        Log.d("Mine films", "reuse");
//                                        manageFilmAdapter.notifyItemInserted(genreList.size());
//                                        manageFilmAdapter.addFilm(gen.getList());
                                        manageCommentAdapter.notifyDataSetChanged();

                                        mShimmerViewContainer.stopShimmer();
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        rcyAdminFilm.setVisibility(View.VISIBLE);
                                        viewModel.setAllFilmInGenre(genreList);
                                        swipeRefreshHome.setRefreshing(false);

                                    }
//                                    Log.d("Mine film genre", response);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
            request.setShouldCache(false);
            requestQueueUltil.addToRequestQueue(request);
        }

        //////////////////////////////////////GET DATA////////////////////////////////
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if(selectedItem.equals("All genres"))
                {
                    filmList.clear();
                    for (Genre g : genreList)
                    {
                        filmList.addAll(g.getList());
                        manageCommentAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    filmList.clear();
                    for(Genre g : genreList)
                    {
                        if(g.getNameGenre().equals(selectedItem))
                        {
                            filmList.addAll(g.getList());
                            manageCommentAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    ////////////////////////////////
}
