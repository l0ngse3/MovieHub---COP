package com.example.myapplication.Ui.Fragment.Admin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.cache.NoCache;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.FilmSaved;
import com.example.myapplication.R;
import com.example.myapplication.Service.RequestQueueUltil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StatisticFragment extends Fragment {

    Spinner spinner;
    List<Film> listFilmLove;
    Set set;

    AnyChartView anyChartView;
    Cartesian cartesian;
    View view;

    public StatisticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        process();
    }

    private void process() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String chartMode = adapterView.getItemAtPosition(i).toString();

                switch (chartMode){
                    case "The most film loved" : createChartFilmLove(); break;
                    case "The most genres loved" : createChartGenreLove(); break;
                    case "The most genres view" : createChartGenreView(); break;
                    case "Account watched the most" : createChartAccountWatchedMost(); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void init(View view) {
        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));
        spinner = view.findViewById(R.id.spinner);
        this.view = view;

        ArrayAdapter<CharSequence> adapterOrderBy = ArrayAdapter.createFromResource(view.getContext(),
                R.array.statistic_array, R.layout.spinner_item);
        adapterOrderBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterOrderBy);


        JsonArrayRequest request = new JsonArrayRequest(APIConnectorUltils.HOST_NAME + "Statistic/FilmSavedMost", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Film[] arr = new Gson().fromJson(response.toString(), Film[].class);

                cartesian = AnyChart.column();
                List<DataEntry> data1 = new ArrayList<>();
                for(Film f : arr){
                    data1.add(new ValueDataEntry(f.getTitle_film(), Integer.parseInt(f.getFilm_url())));
                }
                set = Set.instantiate();
                Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

                Column column = cartesian.column(series1Data);

                column.tooltip()
                        .titleFormat("{%X}")
                        .position(Position.CENTER_BOTTOM)
                        .anchor(Anchor.CENTER_BOTTOM)
                        .offsetX(0d)
                        .offsetY(10d)
                        .format("{%Value}{groupsSeparator: }");

                cartesian.animation(true);
                cartesian.title("Top 5 " + spinner.getSelectedItem().toString());
                cartesian.yScale().minimum(0d);
                cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
                cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                cartesian.interactivity().hoverMode(HoverMode.BY_X);
                cartesian.xAxis(0).title("Film");
                cartesian.yAxis(0).title("Times");

                anyChartView.setChart(cartesian);
            }
        }, null);
        request.setShouldCache(false);
        RequestQueueUltil.getInstance(getContext()).addToRequestQueue(request);
    }


///////////
    private void createChartFilmLove() {
        JsonArrayRequest request = new JsonArrayRequest(APIConnectorUltils.HOST_NAME + "Statistic/FilmSavedMost", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Film[] arr = new Gson().fromJson(response.toString(), Film[].class);

                List<DataEntry> data2 = new ArrayList<>();
                for(Film f : arr){
                    data2.add(new ValueDataEntry(f.getTitle_film(), Integer.parseInt(f.getFilm_url())));
                }
                set.data(data2);
                cartesian.title("Top 5 " + spinner.getSelectedItem().toString());
                cartesian.xAxis(0).title("Film");
                cartesian.yAxis(0).title("Times");
            }
        }, null);
        request.setShouldCache(false);
        RequestQueueUltil.getInstance(getContext()).addToRequestQueue(request);
    }
////////////
    private void createChartGenreLove() {
        JsonArrayRequest request = new JsonArrayRequest(APIConnectorUltils.HOST_NAME + "Statistic/GenreSavedMost", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Film[] arr = new Gson().fromJson(response.toString(), Film[].class);

                List<DataEntry> data2 = new ArrayList<>();
                for(Film f : arr){
                    data2.add(new ValueDataEntry(f.getGenre(), Integer.parseInt(f.getFilm_views())));
                }
                set.data(data2);
                cartesian.title("Top 5 " + spinner.getSelectedItem().toString());
                cartesian.xAxis(0).title("Genre");
                cartesian.yAxis(0).title("Times");
            }
        }, null);
        request.setShouldCache(false);
        RequestQueueUltil.getInstance(getContext()).addToRequestQueue(request);
    }
///////////
    private void createChartAccountWatchedMost() {
        JsonArrayRequest request = new JsonArrayRequest(APIConnectorUltils.HOST_NAME + "Statistic/AccountWatchedMost", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                FilmSaved[] arr = new Gson().fromJson(response.toString(), FilmSaved[].class);

                List<DataEntry> data2 = new ArrayList<>();
                for(FilmSaved f : arr){
                    data2.add(new ValueDataEntry(f.getUsername(), Integer.parseInt(f.getIdFilm())));
                }
                set.data(data2);
                cartesian.title("Top 5 " + spinner.getSelectedItem().toString());
                cartesian.xAxis(0).title("Username");
                cartesian.yAxis(0).title("Number of Films");
            }
        }, null);
        request.setShouldCache(false);
        RequestQueueUltil.getInstance(getContext()).addToRequestQueue(request);
    }

    private void createChartGenreView() {
        JsonArrayRequest request = new JsonArrayRequest(APIConnectorUltils.HOST_NAME + "Statistic/GenreViewMost", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Film[] arr = new Gson().fromJson(response.toString(), Film[].class);

                List<DataEntry> data2 = new ArrayList<>();
                for(Film f : arr){
                    data2.add(new ValueDataEntry(f.getGenre(), Integer.parseInt(f.getFilm_views())));
                }
                set.data(data2);
                cartesian.title("Top 5 " + spinner.getSelectedItem().toString());
                cartesian.xAxis(0).title("Genre");
                cartesian.yAxis(0).title("Views");
            }
        }, null);
        request.setShouldCache(false);
        RequestQueueUltil.getInstance(getContext()).addToRequestQueue(request);
    }
}
