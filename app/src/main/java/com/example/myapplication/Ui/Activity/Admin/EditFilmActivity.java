package com.example.myapplication.Ui.Activity.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.VideoUltils;
import com.example.myapplication.R;
import com.example.myapplication.Service.AdminService;
import com.example.myapplication.Service.TaskDone;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;

public class EditFilmActivity extends AppCompatActivity {

    EditText edtFilmTitle, edtFilmDescription, edtIMDB, edtViews;
    ImageView imgThumbnail;
    ImageButton btnBack;
    TextView txtSaveFilm;
    Spinner spinnerGenre;
    Bitmap bitmap;
    Film filmChange, filmSource;
    ProgressBar progress_horizontal;
    Button btnTrailerBrow, btnFilmBrow;
    VideoView videoViewTrailer, videoViewFilm;
    ScrollView layoutScroll;
    MediaController controllerTrailer, controllerFilm;
    AdminService service;

    EditFilmActivity context;
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
        setContentView(R.layout.activity_edit_film);
        init();
        process();
    }

    private void process() {
        filmSource = new Gson().fromJson(getIntent().getStringExtra("film"), Film.class);
        edtFilmTitle.setText(filmSource.getTitle_film());
        edtIMDB.setText(filmSource.getRate_imdb());
        edtFilmDescription.setText(filmSource.getFilm_description());
        Glide.with(this).load(APIConnectorUltils.HOST_STORAGE + filmSource.getThumbnail())
                .centerCrop()
                .apply(new RequestOptions().override(128, 72))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgThumbnail);
        spinnerGenre.setSelection(adapter.getPosition(filmSource.getGenre()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtSaveFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFilm();
            }
        });

        dataChange();

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

        ///////INIT controller video/////////////////////////////////
        controllerTrailer = new MediaController(this);
        controllerFilm = new MediaController(this);
        controllerTrailer.setAnchorView(videoViewTrailer);
        controllerFilm.setAnchorView(videoViewFilm);

        videoViewFilm.setMediaController(controllerFilm);
        videoViewTrailer.setMediaController(controllerTrailer);

        videoViewFilm.setVideoPath(APIConnectorUltils.HOST_STORAGE + filmSource.getFilm_url());
        videoViewTrailer.setVideoPath(APIConnectorUltils.HOST_STORAGE + filmSource.getTrailer_url());

        layoutScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                controllerFilm.hide();
                controllerTrailer.hide();
            }
        });

        /////// handle get video to edit//////////////////////////////
        btnTrailerBrow.setOnClickListener(new View.OnClickListener() {
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

        btnFilmBrow.setOnClickListener(new View.OnClickListener() {
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
    }

    private void saveFilm() {
        filmChange = filmSource;

        String title = edtFilmTitle.getText().toString();
        String genre = spinnerGenre.getSelectedItem().toString();
        String des = edtFilmDescription.getText().toString();
        String imdb = edtIMDB.getText().toString();
        String view = edtViews.getText().toString();


        if(!uriPhoto.isEmpty()){
            //upload image
            txtSaveFilm.setVisibility(View.GONE);
            progress_horizontal.setVisibility(View.VISIBLE);
            service.uploadFile(uriPhoto, filmSource.getThumbnail(), new TaskDone() {
                @Override
                public void done(String result) {
                    Log.d("Mine thumbnail upload", "done: " + result);
                    progress_horizontal.setVisibility(View.GONE);
                }
            });
        }
        if(!uriTrailer.isEmpty()){
            // upload film trailer
            txtSaveFilm.setVisibility(View.GONE);
            progress_horizontal.setVisibility(View.VISIBLE);
            service.uploadFile(uriTrailer, filmSource.getTrailer_url(), new TaskDone() {
                @Override
                public void done(String result) {
                    Log.d("Mine trailer upload", "done: " + result);
                    progress_horizontal.setVisibility(View.GONE);
                }
            });
        }
        if(!uriFilm.isEmpty()){
            // upload film play
            txtSaveFilm.setVisibility(View.GONE);
            progress_horizontal.setVisibility(View.VISIBLE);
            service.uploadFile(uriFilm, filmSource.getFilm_url(), new TaskDone() {
                @Override
                public void done(String result) {
                    Log.d("Mine film upload", "done: " + result);
                    progress_horizontal.setVisibility(View.GONE);

                }
            });
        }

        if(title.isEmpty()){
            edtFilmTitle.setError("Title Required");
        }
        else if(genre.equals("All genres")) {
            ((TextView) spinnerGenre.getSelectedView()).setError("Genre Required");
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
            filmChange.setTitle_film(title);
            filmChange.setGenre(genre);
            filmChange.setFilm_description(des);
            filmChange.setRate_imdb(imdb);
            filmChange.setFilm_views(view);

            Log.d("Mine film add", "addFilm: " + filmChange.getTrailer_url() + " " + filmChange.getFilm_url());
            txtSaveFilm.setVisibility(View.GONE);
            progress_horizontal.setVisibility(View.VISIBLE);

            // add film to DB
//            Log.e("Mine bitmap", "addFilm: " + filmChange.getThumbnail());
            service.postFilmUpdate(filmChange, context,new TaskDone() {
                @Override
                public void done(String result) {
                    JsonObject object = new JsonParser().parse(result).getAsJsonObject();
                    Toast.makeText(context, "Upload success:" + object.get("result"), Toast.LENGTH_SHORT).show();
                    progress_horizontal.setVisibility(View.GONE);
                }
            });
        }


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
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterView.getSelectedItem().toString().equals(filmSource.getGenre()))
                    showButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void init() {
        edtFilmTitle = findViewById(R.id.edtFilmTitle);
        edtFilmDescription = findViewById(R.id.edtFilmDescription);
        edtIMDB = findViewById(R.id.edtIMDB);
        imgThumbnail = findViewById(R.id.imgThumbnail);
        btnBack = findViewById(R.id.btnBack);
        txtSaveFilm = findViewById(R.id.txtSaveFilm);
        progress_horizontal = findViewById(R.id.progress_horizontal);
        btnFilmBrow = findViewById(R.id.btnFilmBrow);
        btnTrailerBrow = findViewById(R.id.btnTrailerBrow);
        videoViewFilm = findViewById(R.id.videoViewFilm);
        videoViewTrailer = findViewById(R.id.videoViewTrailer);
        layoutScroll = findViewById(R.id.layoutScroll);
        edtViews = findViewById(R.id.edtViews);

        filmChange = new Film();
        context = this;
        service = new AdminService(context);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.genres_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(adapter);
        hideButton();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PHOTO) {
                showButton();
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
                    videoViewFilm.setVideoPath(realPath);
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
                    videoViewTrailer.setVideoPath(realPath);
                    uriTrailer = realPath;
                }

            }


        }
    }

    private void hideButton(){
        countUpload = 0;
        txtSaveFilm.setVisibility(View.GONE);
    }

    private void showButton(){
        countUpload = 0;
        txtSaveFilm.setVisibility(View.VISIBLE);
    }

}
