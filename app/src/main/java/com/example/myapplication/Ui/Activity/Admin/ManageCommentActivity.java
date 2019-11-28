package com.example.myapplication.Ui.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Comment;
import com.example.myapplication.Model.Film;
import com.example.myapplication.R;
import com.example.myapplication.Service.ClientService;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Adapter.Admin.ManageCommentAdapter;
import com.example.myapplication.Ui.Adapter.Client.CommentAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageCommentActivity extends AppCompatActivity {

    EditText edtSearch;
    TextView txtNoContent;
    ImageButton btnBack;

    RecyclerView rcyComment;
    List<Comment> list;
    ManageCommentAdapter adapter;

    ClientService service;
    ManageCommentActivity context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_comment);

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

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init() {
        txtNoContent = findViewById(R.id.txtNoContent);
        edtSearch = findViewById(R.id.edtSearch);
        rcyComment = findViewById(R.id.rcyComment);
        btnBack = findViewById(R.id.btnBack);
        list = new ArrayList<>();
        context = this;

        Film f = new Gson().fromJson(getIntent().getStringExtra("film"), Film.class);
        service = new ClientService(getBaseContext());

        String urlCmt = APIConnectorUltils.HOST_STORAGE_COMMENT + "GetComment/" + f.getId_film();
        service.simpleGetter(urlCmt, new TaskDone() {
            @Override
            public void done(String result) {
                Comment[] comments = new Gson().fromJson(result, Comment[].class);
                list.addAll(Arrays.asList(comments));
                if(list.size()>0)
                {
                    adapter = new ManageCommentAdapter(list, context);
                    rcyComment.setAdapter(adapter);
                    rcyComment.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                }
                else{
                    txtNoContent.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}
