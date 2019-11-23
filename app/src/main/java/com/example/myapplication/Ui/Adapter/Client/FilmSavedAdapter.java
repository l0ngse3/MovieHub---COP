package com.example.myapplication.Ui.Adapter.Client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.Model.VideoUltils;
import com.example.myapplication.R;
import com.example.myapplication.Service.LoadDurationAsyncTask;
import com.example.myapplication.Service.RequestQueueUltil;
import com.example.myapplication.Ui.Activity.Client.FilmActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import java.util.List;

public class FilmSavedAdapter extends RecyclerView.Adapter<FilmSavedAdapter.ViewHolder> {

    private List<Film> list;
    private Fragment fragment;

    Film recentlyFilm;
    int recentlyFilmPosition;
    String username;

    public FilmSavedAdapter(List<Film> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.list_item_film_saved, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Film film = list.get(position);
        Log.e("Mine error", "onBindViewHolder: "+ APIConnectorUltils.HOST_STORAGE + film.getThumbnail() );
        Glide.with(fragment).load(APIConnectorUltils.HOST_STORAGE + film.getThumbnail())
                .centerCrop()
                .apply(new RequestOptions().override(128, 72))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into(holder.imgSaved);

        holder.txtDescriptionSaved.setText("Imdb: "+film.getRate_imdb());
//        String duration = VideoUltils.getVideoDuration(APIConnectorUltils.HOST_STORAGE + film.getFilm_url());
//        holder.txtDurationSaved.setText(duration);

        new LoadDurationAsyncTask() {
            @Override
            public void onResponse(String response) {
                holder.txtDurationSaved.setText(response);
            }
        }.execute(APIConnectorUltils.HOST_STORAGE + film.getFilm_url());

        holder.txtViewSaved.setText(film.getFilm_views() + " views");
        holder.txtTitleSaved.setText(film.getTitle_film());
        username = fragment.getActivity().getIntent().getStringExtra("username");
        recentlyFilm = new Film();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragment.getActivity(), FilmActivity.class);
                String username = ViewModelProviders.of(fragment.getActivity()).get(ShareViewModel.class).getUsername().getValue();
                intent.putExtra("film", new Gson().toJson(list.get(position)));
                intent.putExtra("username", username);
                fragment.getActivity().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public Context getContext() {
        return fragment.getContext();
    }

    public void deleteItem(int position) {
        recentlyFilm = list.get(position);
        recentlyFilmPosition = position;
        list.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
        //delete item from server
//        RequestQueue queue = Volley.newRequestQueue(fragment.getContext());
        String url = APIConnectorUltils.HOST_STORAGE_FILM + "Save/Loved?filmId=" + recentlyFilm.getId_film()+"&username="+username;
        StringRequest request = new StringRequest(Request.Method.PUT, url, null, null);
        RequestQueueUltil requestQueueUltil = RequestQueueUltil.getInstance(fragment.getContext());
        requestQueueUltil.addToRequestQueue(request);
    }

    private void showUndoSnackbar() {
        View view = fragment.getActivity().findViewById(R.id.fragment_container);
        Snackbar snackbar = Snackbar.make(view, "Restore",
                Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#00BCD4"));
        snackbar.setAction("Restore", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add(recentlyFilmPosition, recentlyFilm);
                notifyItemInserted(recentlyFilmPosition);
                //restore item from server
//                RequestQueue queue = Volley.newRequestQueue(fragment.getContext());
                String url = APIConnectorUltils.HOST_STORAGE_FILM + "Save/Loved?filmId=" + recentlyFilm.getId_film()+"&username="+username;
                StringRequest request = new StringRequest(Request.Method.PUT, url, null, null);
                RequestQueueUltil requestQueueUltil = RequestQueueUltil.getInstance(fragment.getContext());
                requestQueueUltil.addToRequestQueue(request);
            }
        });
        snackbar.show();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSaved;
        TextView txtDurationSaved, txtTitleSaved, txtDescriptionSaved, txtViewSaved;
        ProgressBar progressFilmWatched;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSaved = itemView.findViewById(R.id.imgSaved);
            txtDurationSaved = itemView.findViewById(R.id.txtDurationSaved);
            txtTitleSaved = itemView.findViewById(R.id.txtTitleSaved);
            txtDescriptionSaved = itemView.findViewById(R.id.txtDescriptionSaved);
            txtViewSaved = itemView.findViewById(R.id.txtViewSaved);
            progressFilmWatched = itemView.findViewById(R.id.progressFilmWatchedProfile);
        }
    }


}
