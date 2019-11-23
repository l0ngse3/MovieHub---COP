package com.example.myapplication.Ui.Adapter.Client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
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
import com.example.myapplication.Model.FilmWatched;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.Model.VideoUltils;
import com.example.myapplication.R;
import com.example.myapplication.Service.LoadDurationAsyncTask;
import com.example.myapplication.Ui.Activity.Client.FilmActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class FilmProfileAdapter extends RecyclerView.Adapter<FilmProfileAdapter.ViewHolder> {

    private List<Film> list;
    private Fragment fragment;
    private List<FilmWatched> watchedList;

    private int timeMax;

    public FilmProfileAdapter(List<Film> list, List<FilmWatched> watchedList, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
        this.watchedList = watchedList;

    }

    @NonNull
    @Override
    public FilmProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View homeFilm = inflater.inflate(R.layout.list_sub_item_fiml_of_genre, parent, false);
        ViewHolder viewHolder = new ViewHolder(homeFilm);
        return viewHolder;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Film film = list.get(position);

        Glide.with(fragment).load(APIConnectorUltils.HOST_STORAGE + film.getThumbnail())
                .centerCrop()
                .apply(new RequestOptions().override(128, 72))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into(holder.imgThumbnail);


        holder.txtDescriptionThumbnail.setText("Imdb: "+film.getRate_imdb());

        new LoadDurationAsyncTask() {
            @Override
            public void onResponse(String response) {
                holder.txtDurationThumbnail.setText(response);
            }
        }.execute(APIConnectorUltils.HOST_STORAGE + film.getFilm_url());

        holder.txtViewThumbnail.setText(film.getFilm_views() + " views");
        holder.txtTitleThumbnail.setText(film.getTitle_film());
        holder.progressFilmWatchedProfile.setVisibility(View.VISIBLE);


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

        Log.d("Mine time max", timeMax+"");

        for(FilmWatched filmWatched : watchedList)
        {
            if(film.getId_film().equals(filmWatched.getIdFilm()))
            {
                holder.progressFilmWatchedProfile.setVisibility(View.VISIBLE);
                holder.progressFilmWatchedProfile.setProgress(filmWatched.getCurProgress());
                new LoadDurationAsyncTask() {
                    @Override
                    public void onResponse(String response) {
                        holder.progressFilmWatchedProfile.setMax(Integer.parseInt(response));
                    }
                }.execute("",APIConnectorUltils.HOST_STORAGE + film.getFilm_url());

                Log.d("Mine time current", filmWatched.getCurProgress()+"");
                break;
            }
        }
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
