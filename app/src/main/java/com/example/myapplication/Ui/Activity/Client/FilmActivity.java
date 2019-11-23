package com.example.myapplication.Ui.Activity.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.BitmapUltils;
import com.example.myapplication.Service.ClientService;
import com.example.myapplication.Service.RequestQueueUltil;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Adapter.Client.CommentAdapter;
import com.example.myapplication.Model.Comment;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.R;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilmActivity extends AppCompatActivity {

    TextView txtTitleFilm, txtViewFilm, txtRateFilm, txtDescriptionFilm, txtPlayMode;
    Button btnTrailerFilm, btnWatchFilm;
    ImageView btnLoveFilm, imgThumbnailFilm, imgAvaCmtText, imgSendCmt;
    VideoView videoViewFilm;
    EditText txtCmtText;

    List<Comment> commentList;
    CommentAdapter commentAdapter;
    RecyclerView rcyComment;

    LinearLayout layoutComment;
    ScrollView layoutScroll;
    Film film;
    FilmActivity context;
    int current_progress = 0;
    String username;
    boolean isLoved;
    Profile profile;
    Comment comment;
    ClientService service;

    int flagEdit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        init();
        process();
    }

    private void process() {

        //////////////////////////////////////////////////////////////
        //video   init                                              //
        //////////////////////////////////////////////////////////////
        final MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoViewFilm);

        //get current progress of this video
        //if it's not exist response return 0
        service.getCurrentProgress(username, film.getId_film(), new TaskDone() {
            @Override
            public void done(String result) {
                current_progress = new Gson().fromJson(result, Integer.class);
                current_progress *= 1000;
            }
        });


        videoViewFilm.setVideoPath(APIConnectorUltils.HOST_STORAGE + film.getFilm_url());
        videoViewFilm.setMediaController(mediaController);
        videoViewFilm.start();

        btnTrailerFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPlayMode.setText("Trailer");
                videoViewFilm.setVideoPath(APIConnectorUltils.HOST_STORAGE + film.getFilm_url());
                videoViewFilm.setMediaController(mediaController);
                videoViewFilm.start();
            }
        });

        btnWatchFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilmPlayActivity.class);
                intent.putExtra("film", new Gson().toJson(film));
                intent.putExtra("username", username);
                intent.putExtra("currentTime", current_progress);
                context.startActivity(intent);
            }
        });

        btnLoveFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoved) {
                    btnLoveFilm.setImageResource(R.drawable.icon_like);
                    isLoved = false;
                } else {
                    btnLoveFilm.setImageResource(R.drawable.icon_like_selected);
                    isLoved = true;
                }

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = APIConnectorUltils.HOST_STORAGE_FILM + "Save/Loved?filmId=" + film.getId_film() + "&username=" + username;
                Log.d("Mine is saved ", film.getFilm_views());
                StringRequest request = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (Boolean.parseBoolean(response)) {
                                    btnLoveFilm.setImageResource(R.drawable.icon_like_selected);
                                    isLoved = true;
                                } else {
                                    btnLoveFilm.setImageResource(R.drawable.icon_like);
                                    isLoved = false;
                                }
                            }
                        }, null);
                queue.start();
                queue.add(request);
            }
        });

        //////////////////////////////////////////////////////////////
        //comment init                                              //
        //////////////////////////////////////////////////////////////
        final String urlAvatar = APIConnectorUltils.HOST_NAME + "Profile/" + username;
        service.simpleGetter(urlAvatar, new TaskDone() {
            @Override
            public void done(String result) {
                profile = new Gson().fromJson(result, Profile.class);
                String urlImg = "";
                if (profile.getImage().equals("null")) {
                    urlImg = APIConnectorUltils.HOST_STORAGE_IMAGE + "saitama.png";
                } else {
                    urlImg = APIConnectorUltils.HOST_STORAGE_IMAGE + profile.getImage();
                }
                BitmapUltils.loadCircleImageInto(context, urlImg, imgAvaCmtText);
            }
        });


        //add comment to comment list
        String urlCmt = APIConnectorUltils.HOST_STORAGE_COMMENT + "GetComment/" + film.getId_film();
        service.simpleGetter(urlCmt, new TaskDone() {
            @Override
            public void done(String result) {
                Comment[] comments = new Gson().fromJson(result, Comment[].class);
                commentList.addAll(Arrays.asList(comments));

                commentAdapter = new CommentAdapter(commentList, context, username);
                rcyComment.setAdapter(commentAdapter);
                rcyComment.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            }
        });


        imgSendCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if flagEdit==0 request add new comment else edit old comment
                RequestQueue queue = Volley.newRequestQueue(context);
                if (flagEdit == 0) {
                    String urlAddComment = APIConnectorUltils.HOST_STORAGE_COMMENT + "AddNewComment";
                    comment = new Comment();
                    comment.setCommentId("CMX");
                    comment.setFilmId(film.getId_film());
                    comment.setContent(txtCmtText.getText().toString());
                    comment.setUsername(username);

                    txtCmtText.setText("");

                    if (comment.getContent().length() == 0 || comment.getContent() == null) {
                        Toast.makeText(FilmActivity.this, "Your comment have no content", Toast.LENGTH_SHORT).show();
                        txtCmtText.setError("Required content");
                    } else {
                        try {
                            commentList.add(0, comment);
                            rcyComment.setHasFixedSize(true);
                            commentAdapter.notifyItemInserted(0);

                            JSONObject object = new JSONObject(new Gson().toJson(comment));
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlAddComment, object,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                commentList.get(0).setCommentId(response.getString("id_comment"));
                                                commentAdapter.notifyDataSetChanged();
                                                Log.d("Mine Add Cmt", "onResponse: " + response.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    },
                                    null);

                            queue.start();
                            queue.add(request);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    flagEdit = 0;
                    String urlUpdateComment = APIConnectorUltils.HOST_STORAGE_COMMENT + "UpdateComment";

                    comment.setContent(txtCmtText.getText().toString());
                    txtCmtText.setText("");
                    if (comment.getContent().length() == 0 || comment.getContent() == null) {
                        Toast.makeText(FilmActivity.this, "Your comment have no content", Toast.LENGTH_SHORT).show();
                        txtCmtText.setError("Required content");
                    } else {
                        try {
                            rcyComment.setHasFixedSize(true);
                            commentAdapter.notifyDataSetChanged();
                            JSONObject object = new JSONObject(new Gson().toJson(comment));
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlUpdateComment, object,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                Log.d("Mine Update Cmt", "onResponse: " + response.getString("result"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    null);

                            queue.start();
                            queue.add(request);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //
            }
        });

        // hide media player when scroll
        layoutScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mediaController.hide();
            }
        });
    }

    private void init() {
        txtTitleFilm = findViewById(R.id.txtTitleFilm);
        txtViewFilm = findViewById(R.id.txtViewFilm);
        txtRateFilm = findViewById(R.id.txtRateFilm);
        txtDescriptionFilm = findViewById(R.id.txtDescriptionFilm);
        txtPlayMode = findViewById(R.id.txtPlayMode);
        btnTrailerFilm = findViewById(R.id.btnTrailerFilm);
        btnWatchFilm = findViewById(R.id.btnWatchFilm);
        btnLoveFilm = findViewById(R.id.btnLoveFilm);
        imgThumbnailFilm = findViewById(R.id.imgThumbnailFilm);
        videoViewFilm = findViewById(R.id.videoViewFilm);
        rcyComment = findViewById(R.id.rcyComment);
        txtCmtText = findViewById(R.id.txtCmtText);
        imgAvaCmtText = findViewById(R.id.imgAvaCmtText);
        layoutComment = findViewById(R.id.layoutComment);
        layoutScroll = findViewById(R.id.layoutScroll);
        imgSendCmt = findViewById(R.id.imgSendCmt);
        context = this;
        isLoved = false;

        film = new Gson().fromJson(getIntent().getStringExtra("film"), Film.class);
        username = getIntent().getStringExtra("username");
        txtTitleFilm.setText(film.getTitle_film());
        txtViewFilm.setText(film.getFilm_views() + " views");
        txtRateFilm.setText("Imdb: " + film.getRate_imdb());
        txtDescriptionFilm.setText(film.getFilm_description());
        commentList = new ArrayList<>();
        service = new ClientService(context);

        BitmapUltils.loadRectangleImageInto(this,
                128, 72,
                APIConnectorUltils.HOST_STORAGE + film.getThumbnail(), imgThumbnailFilm);

        //request to check loved

        String url = APIConnectorUltils.HOST_STORAGE_FILM + "Save/IsSaved?filmId=" + film.getId_film() + "&username=" + username;
        Log.d("Mine is saved ", film.getFilm_views());
        service.simpleGetter(url, new TaskDone() {
            @Override
            public void done(String result) {
                if (Boolean.parseBoolean(result)) {
                    btnLoveFilm.setImageResource(R.drawable.icon_like_selected);
                    isLoved = true;
                } else {
                    btnLoveFilm.setImageResource(R.drawable.icon_like);
                    isLoved = false;
                }
            }
        });


    }

    public boolean deleteComment(int pos) {
        Comment deleteCmt = commentList.get(pos);
        commentList.remove(pos);
        commentAdapter.notifyDataSetChanged();

        txtCmtText.setText("");

        String urlDeleteComment = APIConnectorUltils.HOST_STORAGE_COMMENT + "DeleteComment";
        Log.d("Mine del comment", "onMenuItemClick:" + pos + " id: " + new Gson().toJson(deleteCmt));

        //request delete comment
        service.simplePoster(urlDeleteComment, deleteCmt, new TaskDone() {
            @Override
            public void done(String result) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }

    public EditText getTxtCmtText() {
        return txtCmtText;
    }

    public void setFlagEdit(int flagEdit) {
        this.flagEdit = flagEdit;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
