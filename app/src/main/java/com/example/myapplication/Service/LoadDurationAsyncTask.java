package com.example.myapplication.Service;

import android.os.AsyncTask;

import com.example.myapplication.Model.VideoUltils;

public abstract class LoadDurationAsyncTask extends AsyncTask<String, Void, String> {

    public abstract void onResponse(String response);

    // two params if first null so it's get second param
    @Override
    protected String doInBackground(String... strings) {
        if(strings[0].isEmpty()) {
            return new VideoUltils().getVideoDurationSecond(strings[1])+"";
        }
        else{
            return new VideoUltils().getVideoDuration(strings[0]);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onResponse(s);
    }
}
