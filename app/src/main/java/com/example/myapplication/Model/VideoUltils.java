package com.example.myapplication.Model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import java.util.HashMap;

public class VideoUltils {

    public String getVideoDuration(String videoPath){
        Log.d("Mine duration", videoPath);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath, new HashMap<String, String>());
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        long timeMillisec = Long.parseLong(time);
        long duration = timeMillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);
        return hours <= 0 ?
                (minutes <= 0 ? seconds + "" : minutes + ":" + seconds) : (hours + ":" + minutes + ":" + seconds);
    }

    public int getVideoDurationSecond(String videoPath){
        Log.d("Mine duration", videoPath);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath, new HashMap<String, String>());
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        int timeMillisec = Integer.parseInt(time);
        int duration = timeMillisec / 1000;
        return duration;
    }

    public static Bitmap getFrameOfVideo(String videoPath, int atTime)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap frameAtTime = retriever.getFrameAtTime(atTime, MediaMetadataRetriever.OPTION_CLOSEST);
        retriever.release();
        return frameAtTime;
    }

    public static String getRealPathFromUriVideo(Uri contentUri, Context context) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Video.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getRealPathFromUriImage(Uri contentUri, Context context) {
        Cursor cursor = null;
        try {
            String proj[] = {MediaStore.Images.Media.DATA} ;
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
