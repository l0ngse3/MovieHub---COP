package com.example.myapplication.Ui.Adapter.Client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.VideoUltils;
import com.example.myapplication.R;
import com.example.myapplication.Service.LoadDurationAsyncTask;
import com.example.myapplication.Ui.Activity.Client.FilmActivity;
import com.example.myapplication.Ui.Activity.Client.SearchActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilmSearchAdapter  extends RecyclerView.Adapter<FilmSearchAdapter.ViewHolder> implements Filterable {


    private List<Film> list;
    private SearchActivity searchActivity;

    Film recentlyFilm;
    String username;
    List<Film> fullList;

    public FilmSearchAdapter(List<Film> list, SearchActivity searchActivity) {
        this.list = list;
        this.searchActivity = searchActivity;

        fullList = new ArrayList<>(list);
    }


    @NonNull
    @Override
    public FilmSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.list_item_film_saved, parent, false);
        return new FilmSearchAdapter.ViewHolder(view);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Film film = list.get(position);
        Log.e("Mine error", "onBindViewHolder: "+ APIConnectorUltils.HOST_STORAGE + film.getThumbnail() );
        Glide.with(searchActivity).load(APIConnectorUltils.HOST_STORAGE + film.getThumbnail())
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
        username = searchActivity.getIntent().getStringExtra("username");
        recentlyFilm = new Film();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchActivity, FilmActivity.class);
                intent.putExtra("film", new Gson().toJson(list.get(position)));
                intent.putExtra("username", username);
                searchActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Context getContext() {
        return searchActivity;
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

    @Override
    public Filter getFilter() {
        return filmFilter;
    }

    private Filter filmFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Film> filterList = new ArrayList<>();

            if(charSequence.length()==0 || charSequence==null)
            {
                filterList.addAll(fullList);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Film item : fullList)
                {
                    if(item.getTitle_film().toLowerCase().contains(filterPattern))
                    {
                        filterList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll( (List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
