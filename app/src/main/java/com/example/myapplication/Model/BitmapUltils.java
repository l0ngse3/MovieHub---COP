package com.example.myapplication.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;

public class BitmapUltils {
    public static String bitmapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, arrayOutputStream);
        byte[] imageByte = arrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    public static void loadCircleImageInto(Context context, String url, ImageView view)
    {
        Glide.with(context).load(url)
                .centerCrop()
                .apply(new RequestOptions().circleCrop())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(view);
    }

    public static void loadRectangleImageInto(Context context, int width, int height, String url, ImageView view)
    {
        Glide.with(context).load(url)
                .centerCrop()
                .apply(new RequestOptions().override(width, height))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(view);
    }

    public static void loadCircleBitmapInto(Context context, Bitmap bm, ImageView view)
    {
        Glide.with(context).load(bm)
                .centerCrop()
                .apply(new RequestOptions().circleCrop())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(view);
    }
}
