package com.example.myapplication.Ui.Activity.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.VideoUltils;
import com.example.myapplication.R;
import com.example.myapplication.Service.AdminService;
import com.example.myapplication.Service.TaskDone;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class AddFilmActivity extends AppCompatActivity {

    EditText edtFilmTitle, edtFilmDescription, edtIMDB, edtViews;
    ImageView imgThumbnail, imgTrailer, imgPlay;
    ImageButton btnBack;
    TextView txtUploadFilm;
    Spinner spinnerGenre;
    Bitmap bitmap;
    Film filmAdd;
    ProgressBar progress_horizontal;

    Context context;
    ArrayAdapter<CharSequence> adapter;

    int REQUEST_CODE_PHOTO = 1000;
    int REQUEST_CODE_VIDEO_TRAILER = 2000;
    int REQUEST_CODE_VIDEO_PLAY = 3000;

    String uriPhoto = "";
    String uriTrailer = "";
    String uriFilm = "";

    int countUpload = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);

        init();
        process();
    }

    private void process() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtFilmTitle.getText().toString().isEmpty()) {
                    showButton();
                    Intent photoPicker = new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("image/*");
                    startActivityForResult(photoPicker, REQUEST_CODE_PHOTO);
                }
                else{
                    Toast.makeText(context, "Please, fill in the Film Title", Toast.LENGTH_LONG).show();
                    edtFilmTitle.requestFocus();
                }

            }
        });

        imgTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtFilmTitle.getText().toString().isEmpty()) {
                    showButton();
                    Intent photoPicker = new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("video/*");
                    startActivityForResult(photoPicker, REQUEST_CODE_VIDEO_TRAILER);
                }
                else{
                    Toast.makeText(context, "Please, fill in the Film Title", Toast.LENGTH_LONG).show();
                    edtFilmTitle.requestFocus();
                }

            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtFilmTitle.getText().toString().isEmpty()) {
                    showButton();
                    Intent photoPicker = new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("video/*");
                    startActivityForResult(photoPicker, REQUEST_CODE_VIDEO_PLAY);
                }
                else{
                    Toast.makeText(context, "Please, fill in the Film Title", Toast.LENGTH_LONG).show();
                    edtFilmTitle.requestFocus();
                }
            }
        });

        txtUploadFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addFilm();
                }
                catch (Exception ex){
                    Toast.makeText(context, "Failure when add film!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dataChange();
    }

    private void dataChange() {
        edtFilmTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtFilmDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtIMDB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init() {
        edtFilmTitle = findViewById(R.id.edtFilmTitle);
        edtFilmDescription = findViewById(R.id.edtFilmDescription);
        edtIMDB = findViewById(R.id.edtIMDB);
        edtViews = findViewById(R.id.edtViews);

        imgThumbnail = findViewById(R.id.imgThumbnail);
        imgTrailer = findViewById(R.id.imgTrailer);
        imgPlay = findViewById(R.id.imgPlay);
        btnBack = findViewById(R.id.btnBack);
        txtUploadFilm = findViewById(R.id.txtUploadFilm);
        progress_horizontal = findViewById(R.id.progress_horizontal);

        filmAdd = new Film();
        context = this;
        spinnerGenre = findViewById(R.id.spinnerGenre);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.genres_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(adapter);
        hideButton();
    }

    private void hideButton() {
        countUpload = 0;
        txtUploadFilm.setVisibility(View.GONE);
    }

    private void showButton(){
        countUpload = 0;
        txtUploadFilm.setVisibility(View.VISIBLE);
    }

    private void addFilm(){
        String title = edtFilmTitle.getText().toString();
        String genre = spinnerGenre.getSelectedItem().toString();
        String des = edtFilmDescription.getText().toString();
        String imdb = edtIMDB.getText().toString();
        String view = edtViews.getText().toString();

        if(title.isEmpty()){
            edtFilmTitle.setError("Title Required");
        }
        else if(genre.equals("All genres")) {
            ((TextView) spinnerGenre.getSelectedView()).setError("Genre Required");
        }
        else if(uriPhoto.isEmpty()){
            Toast.makeText(context, "Thumbnail is invalid!", Toast.LENGTH_LONG).show();
        }
        else if(uriTrailer.isEmpty()){
            Toast.makeText(context, "Trailer is invalid!", Toast.LENGTH_LONG).show();
        }
        else if(uriFilm.isEmpty()){
            Toast.makeText(context, "Film is invalid!", Toast.LENGTH_LONG).show();
        }
        else if(des.isEmpty()) {
            edtFilmDescription.setError("Required");
        }
        else if(imdb.isEmpty()) {
            edtIMDB.setError("Required");
        }
        else if(view.isEmpty()) {
            edtIMDB.setError("Required");
        }
        else {
            filmAdd.setTitle_film(title);
            filmAdd.setGenre(genre);
            filmAdd.setFilm_description(des);
            filmAdd.setRate_imdb(imdb);
            filmAdd.setFilm_views(view);

            Log.d("Mine film add", "addFilm: "+ filmAdd.getTrailer_url() + " " + filmAdd.getFilm_url());
            AdminService service = new AdminService(context);
            txtUploadFilm.setVisibility(View.GONE);
            progress_horizontal.setVisibility(View.VISIBLE);

            String prefix_name = getSlug(filmAdd.getTitle_film());
            //upload image
            service.uploadFile(uriPhoto, prefix_name + "-thumbnail.jpg", new TaskDone() {
                @Override
                public void done(String result) {
                    Log.d("Mine thumbnail upload", "done: "+result);
                    countUpload++;
                    if(countUpload==3)
                    {
                        Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT).show();
                        progress_horizontal.setVisibility(View.GONE);
                    }
                }
            });
            // upload film trailer
            service.uploadFile(uriTrailer, prefix_name + "-trailer.mp4", new TaskDone() {
                @Override
                public void done(String result) {
                    Log.d("Mine trailer upload", "done: "+result);
                    countUpload++;
                    if(countUpload==3)
                    {
                        Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT).show();
                        progress_horizontal.setVisibility(View.GONE);
                    }
                }
            });
            // upload film play
            service.uploadFile(uriFilm, prefix_name + ".mp4", new TaskDone() {
                @Override
                public void done(String result) {
                    Log.d("Mine film upload", "done: "+result);
                    countUpload++;
                    if(countUpload==3)
                    {
                        Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT).show();
                        progress_horizontal.setVisibility(View.GONE);
                    }
                }
            });

            //change trailer url and film url from URI -> name
            filmAdd.setThumbnail(prefix_name+"-thumbnail.jpg");
            filmAdd.setTrailer_url(prefix_name+"-trailer.mp4");
            filmAdd.setFilm_url(prefix_name+".mp4");
            // add film to DB
            Log.e("Mine bitmap", "addFilm: "+filmAdd.getThumbnail());
            service.postFilmAdd(filmAdd, new TaskDone() {
                @Override
                public void done(String result) {
//                    JsonObject object = new JsonParser().parse(result).getAsJsonObject();
//                    progress_horizontal.setVisibility(View.GONE);
                }
            });
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            showButton();
            if (requestCode == REQUEST_CODE_PHOTO) {
                Uri dataPath = null;
                if (data != null) {
                    dataPath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), dataPath);
                        Glide.with(context).load(bitmap)
                                .placeholder(R.drawable.saitama)
                                .centerCrop()
                                .apply(new RequestOptions().override(128, 72))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(imgThumbnail);
                        uriPhoto = VideoUltils.getRealPathFromUriImage(dataPath, context);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(requestCode == REQUEST_CODE_VIDEO_PLAY)
            {
                Uri uri;
                if(data!=null)
                {
                    uri = data.getData();
                    String realPath = VideoUltils.getRealPathFromUriVideo(uri, context);
                    Bitmap bm = VideoUltils.getFrameOfVideo(realPath, 20000);
                    imgPlay.setImageBitmap(bm);
                    uriFilm = realPath;
                }
            }
            else if(requestCode == REQUEST_CODE_VIDEO_TRAILER)
            {
                Uri uri;
                if(data!=null)
                {
                    uri = data.getData();
                    String realPath = VideoUltils.getRealPathFromUriVideo(uri, context);
                    Bitmap bm = VideoUltils.getFrameOfVideo(realPath, 20000);
                    imgTrailer.setImageBitmap(bm);
                    uriTrailer = realPath;
                }

            }

        }
    }

    private String getSlug(String s){
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
    }
    //***********************************************************************
}
