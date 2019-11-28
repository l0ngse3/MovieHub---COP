package com.example.myapplication.Ui.Fragment.Client;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Service.ClientService;
import com.example.myapplication.Service.RequestQueueUltil;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Adapter.Client.FilmSavedAdapter;
import com.example.myapplication.Ui.Adapter.Client.SwipeToDeleteCallback;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SavedFragment extends Fragment {

    RecyclerView rcyFilmSaved;
    List<Film> filmList;
    FilmSavedAdapter adapter;
    TextView txtNoContent;

    ShareViewModel viewModel;
    Fragment fragment;
    SwipeRefreshLayout swipeRefreshSaved;
    View viewSaved;

    ItemTouchHelper itemTouchHelper;
    ShimmerFrameLayout mShimmerViewContainer;
    ClientService service;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        process();
    }

    private void process() {
        swipeRefreshSaved.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                filmList.clear();
                init(viewSaved);
            }
        });
    }


    private void init(View view) {
        viewSaved = view;
        swipeRefreshSaved = view.findViewById(R.id.swipeRefreshSaved);
        rcyFilmSaved = view.findViewById(R.id.rcyFilmSaved);
        txtNoContent = view.findViewById(R.id.txtNoContent);
        txtNoContent.setVisibility(View.GONE);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();

        fragment = this;

        if (filmList == null)
            filmList = new ArrayList<>();

        viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        service = new ClientService(getContext());

        service.getFilmSaved(viewModel.getUsername().getValue(), new TaskDone() {
            @Override
            public void done(String result) {
                swipeRefreshSaved.setRefreshing(false);
                Film[] films = new Gson().fromJson(result, Film[].class);

                filmList.addAll(Arrays.asList(films));

                if (adapter == null) {
                    adapter = new FilmSavedAdapter(filmList, fragment);
                    rcyFilmSaved.setAdapter(adapter);
                    rcyFilmSaved.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
                    itemTouchHelper.attachToRecyclerView(rcyFilmSaved);
                } else {
                    adapter.notifyDataSetChanged();
                }
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

                if(filmList.size()==0){
                    txtNoContent.setVisibility(View.VISIBLE);
                }
                else {
                    txtNoContent.setVisibility(View.GONE);
                }

                Log.d("Mine Film saved", result);
            }
        });

    }
}
