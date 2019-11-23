package com.example.myapplication.Ui.Adapter.Client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.Model.VideoUltils;
import com.example.myapplication.R;
import com.example.myapplication.Service.LoadDurationAsyncTask;
import com.example.myapplication.Ui.Activity.Client.FilmActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class FilmHomeAdapter extends RecyclerView.Adapter<FilmHomeAdapter.ViewHolder> {

    private List<Film> list;
    private Fragment fragment;

    private List<Film> listFull;
    private String username;
    ShareViewModel viewModel;

    public FilmHomeAdapter(List<Film> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;

        listFull = new ArrayList<>();
        listFull.addAll(list);
    }

    @NonNull
    @Override
    public FilmHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View homeFilm = inflater.inflate(R.layout.list_sub_item_fiml_of_genre, parent, false);
        ViewHolder viewHolder = new ViewHolder(homeFilm);
        return viewHolder;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final FilmHomeAdapter.ViewHolder holder, final int position) {
        final Film film = list.get(position);
        Glide.with(fragment).load(APIConnectorUltils.HOST_STORAGE + film.getThumbnail())
                .centerCrop()
                .apply(new RequestOptions().override(128, 72))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into(holder.imgThumbnail);


        holder.txtDescriptionThumbnail.setText("Imdb: "+film.getRate_imdb());

        String videoPath = APIConnectorUltils.HOST_STORAGE + film.getFilm_url();

        new LoadDurationAsyncTask() {
            @Override
            public void onResponse(String response) {
                holder.txtDurationThumbnail.setText(response);
            }
        }.execute(videoPath);

        holder.txtViewThumbnail.setText(film.getFilm_views() + " views");
        holder.txtTitleThumbnail.setText(film.getTitle_film());
        //intent data and view activity watch film
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragment.getActivity(), FilmActivity.class);
                viewModel =  ViewModelProviders.of(fragment.getActivity()).get(ShareViewModel.class);
                username = viewModel.getUsername().getValue();
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtDurationThumbnail, txtTitleThumbnail, txtDescriptionThumbnail, txtViewThumbnail;
        ProgressBar progressFilmWatchedProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            txtDurationThumbnail = itemView.findViewById(R.id.txtDurationThumbnail);
            txtTitleThumbnail = itemView.findViewById(R.id.txtTitleThumbnail);
            txtDescriptionThumbnail = itemView.findViewById(R.id.txtDescriptionThumbnail);
            txtViewThumbnail = itemView.findViewById(R.id.txtViewThumbnail);
            progressFilmWatchedProfile = itemView.findViewById(R.id.progressFilmWatchedProfile);
        }
    }

}
