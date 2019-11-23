package com.example.myapplication.Ui.Adapter.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.R;
import com.example.myapplication.Service.AdminService;
import com.example.myapplication.Service.LoadDurationAsyncTask;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Activity.Admin.EditFilmActivity;
import com.google.gson.Gson;
import java.util.List;

public class ManageFilmAdapter extends RecyclerView.Adapter<ManageFilmAdapter.ViewHolder> {
    private List<Film> list;
    private Fragment fragment;
    private ManageFilmAdapter adapter;

    Film recentlyFilm;
    String username;
    AdminService service;

    public ManageFilmAdapter(List<Film> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
        service = new AdminService(fragment.getContext());
        adapter = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_film_manage, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Film film = list.get(position);
        holder.onBind(film, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addFilm(List<Film> films)
    {
        list.addAll(films);
        notifyItemInserted(list.size()-1);
    }

    public Context getContext() {
        return fragment.getContext();
    }

    public void deleteItem(int position) {
        recentlyFilm = list.get(position);
        final Film filmDel = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(fragment.getContext(), "Deleted film "+recentlyFilm.getTitle_film(), Toast.LENGTH_SHORT).show();
        service.deleteFilm(filmDel, new TaskDone() {
            @Override
            public void done(String result) {
                Toast.makeText(fragment.getContext(), "Deleted film "+filmDel.getTitle_film(), Toast.LENGTH_SHORT).show();
            }
        });
    }


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
        }

        @SuppressLint("StaticFieldLeak")
        public void onBind(Film f, int pos){
            film = f;
            position = pos;
            Glide.with(fragment).load(APIConnectorUltils.HOST_STORAGE + f.getThumbnail())
                    .centerCrop()
                    .apply(new RequestOptions().override(128, 72))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgSaved);

            String videoPath = APIConnectorUltils.HOST_STORAGE + f.getFilm_url();

            new LoadDurationAsyncTask() {
                @Override
                public void onResponse(String response) {
                    txtDurationSaved.setText(response);
                }
            }.execute(videoPath);

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

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
                    builder.setTitle("Delete Film");
                    builder.setMessage("Do you want to delete this film?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes, delete it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.deleteItem(position);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });


        }
    }

}
