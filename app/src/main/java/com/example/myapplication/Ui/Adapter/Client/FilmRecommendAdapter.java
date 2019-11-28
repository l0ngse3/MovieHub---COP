package com.example.myapplication.Ui.Adapter.Client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.R;
import com.example.myapplication.Service.LoadDurationAsyncTask;
import com.example.myapplication.Ui.Activity.Client.FilmActivity;
import com.google.gson.Gson;

import java.util.List;

public class FilmRecommendAdapter extends RecyclerView.Adapter<FilmRecommendAdapter.ViewHolder> {

    List<Film> list;
    FilmActivity context;
    String username;

    public FilmRecommendAdapter(List<Film> list, FilmActivity context, String username) {
        this.list = list;
        this.context = context;
        this.username = username;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recommendFilm = LayoutInflater.from(context.getBaseContext()).inflate(R.layout.list_sub_item_fiml_of_genre, parent, false);
        return new ViewHolder(recommendFilm);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtDurationThumbnail, txtTitleThumbnail, txtDescriptionThumbnail, txtViewThumbnail;
        ProgressBar progressFilmWatchedProfile;

        int position;
        Film filmBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            txtDurationThumbnail = itemView.findViewById(R.id.txtDurationThumbnail);
            txtTitleThumbnail = itemView.findViewById(R.id.txtTitleThumbnail);
            txtDescriptionThumbnail = itemView.findViewById(R.id.txtDescriptionThumbnail);
            txtViewThumbnail = itemView.findViewById(R.id.txtViewThumbnail);
            progressFilmWatchedProfile = itemView.findViewById(R.id.progressFilmWatchedProfile);
        }

        @SuppressLint("StaticFieldLeak")
        public void onBind(int pos){
            position = pos;
             filmBinding = list.get(position);

            Glide.with(context).load(APIConnectorUltils.HOST_STORAGE + filmBinding.getThumbnail())
                    .centerCrop()
                    .apply(new RequestOptions().override(128, 72))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                    .into(imgThumbnail);


            txtDescriptionThumbnail.setText("Imdb: "+filmBinding.getRate_imdb());

            String videoPath = APIConnectorUltils.HOST_STORAGE + filmBinding.getFilm_url();

            new LoadDurationAsyncTask() {
                @Override
                public void onResponse(String response) {
                    txtDurationThumbnail.setText(response);
                }
            }.execute(videoPath);

            txtViewThumbnail.setText(filmBinding.getFilm_views() + " views");
            txtTitleThumbnail.setText(filmBinding.getTitle_film());
            //intent data and view activity watch film
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FilmActivity.class);
                    intent.putExtra("film", new Gson().toJson(list.get(position)));
                    intent.putExtra("username", username);
                    context.startActivity(intent);
                }
            });
        }
    }

}
