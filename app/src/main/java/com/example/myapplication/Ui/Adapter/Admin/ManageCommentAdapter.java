package com.example.myapplication.Ui.Adapter.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.R;
import com.example.myapplication.Service.AdminService;
import com.example.myapplication.Ui.Activity.Admin.EditFilmActivity;
import com.google.gson.Gson;

import java.util.List;

public class ManageCommentAdapter extends RecyclerView.Adapter<ManageCommentAdapter.ViewHolder> {

    private List<Film> list;
    private Fragment fragment;

    Film recentlyFilm;
    String username;
    AdminService service;
    /////////////////////////////////////////////////////

    public ManageCommentAdapter(List<Film> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
        service = new AdminService(fragment.getContext());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_film_saved, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film film = list.get(position);
        holder.onBind(film, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Context getContext() {
        return fragment.getContext();
    }

    /////////////////////////////////////////////////////////////////////////////////
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSaved, imgDelete;
        TextView txtDurationSaved, txtTitleSaved, txtDescriptionSaved, txtViewSaved;
        ProgressBar progressFilmWatched;
        Film film;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSaved = itemView.findViewById(R.id.imgSaved);
            txtDurationSaved = itemView.findViewById(R.id.txtDurationSaved);
            txtTitleSaved = itemView.findViewById(R.id.txtTitleSaved);
            txtDescriptionSaved = itemView.findViewById(R.id.txtDescriptionSaved);
            txtViewSaved = itemView.findViewById(R.id.txtViewSaved);
            progressFilmWatched = itemView.findViewById(R.id.progressFilmWatchedProfile);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            txtDurationSaved.setVisibility(View.GONE);
        }


        public void onBind(Film f, int pos){
            film = f;
            position = pos;
            Glide.with(fragment).load(APIConnectorUltils.HOST_STORAGE + f.getThumbnail())
                    .centerCrop()
                    .apply(new RequestOptions().override(128, 72))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgSaved);


            txtDescriptionSaved.setText("Imdb: " + f.getRate_imdb());
            txtViewSaved.setText(f.getFilm_views() + " views");
            txtTitleSaved.setText(f.getTitle_film());
            username = fragment.getActivity().getIntent().getStringExtra("username");
            recentlyFilm = new Film();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(fragment.getActivity(), EditFilmActivity.class);
                    intent.putExtra("film", new Gson().toJson(film));
                    fragment.getActivity().startActivity(intent);
                }
            });

        }
    }

}
