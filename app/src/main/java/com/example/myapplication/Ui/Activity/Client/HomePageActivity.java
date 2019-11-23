package com.example.myapplication.Ui.Activity.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.BitmapUltils;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.R;
import com.example.myapplication.Service.ClientService;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Activity.MainActivity;
import com.example.myapplication.Ui.Fragment.Client.HomeFragment;
import com.example.myapplication.Ui.Fragment.Client.ProfileFragment;
import com.example.myapplication.Ui.Fragment.Client.SavedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;



public class HomePageActivity extends AppCompatActivity {

    private TextView txtTitle;
    private BottomNavigationView navigationView;
    ImageView imgProfileAva, imgSearch;
    ShareViewModel viewModel;

    Profile profile;
    Context context;
    PopupMenu popup;
    HomePageActivity homePageActivity = this;
    ClientService service;
    MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        init();
        process();
    }


    /******************************************************/
    private void init() {
        txtTitle = findViewById(R.id.txtTitle);
        navigationView = findViewById(R.id.bottom_nav);
        imgProfileAva = findViewById(R.id.imgProfileAva);
        imgSearch = findViewById(R.id.imgSearch);
        context = HomePageActivity.this;
        service = new ClientService(context);

        viewModel = ViewModelProviders.of(this).get(ShareViewModel.class);
        viewModel.setUsername(getIntent().getStringExtra("username"));

        txtTitle.setText("Home");

        service.getInfoProfile(viewModel.getUsername().getValue(), new TaskDone() {
            @Override
            public void done(String result) {
                profile = new Gson().fromJson(result, Profile.class);
                viewModel.setProfile(result);
                viewModel.getProfile().observe((LifecycleOwner) context, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        String urlImg = APIConnectorUltils.HOST_STORAGE_IMAGE + new Gson().fromJson(s, Profile.class).getImage();
                        BitmapUltils.loadCircleImageInto(context, urlImg, imgProfileAva);
                        Log.d("Mine update image", "onChanged: Update image imgProfileAva");
                    }
                });

                Log.d("Mine Profile 1", result);
            }
        });

        navigationView.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new HomeFragment()).commit();
    }

    /******************************************************/
    private void process() {

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menuItem = item;
                FragmentManager manager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        manager.beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "HOME").addToBackStack(null).commit();
                        txtTitle.setText("Home");
                        imgSearch.setVisibility(View.VISIBLE);
                        return true;

                    case R.id.nav_saved:
                        manager.beginTransaction().replace(R.id.fragment_container, new SavedFragment(), "SAVED").addToBackStack(null).commit();
                        txtTitle.setText("Saved");
                        imgSearch.setVisibility(View.GONE);
                        return true;

                    case R.id.nav_profile:
                        manager.beginTransaction().replace(R.id.fragment_container, new ProfileFragment(), "PROFILE").addToBackStack(null).commit();
                        txtTitle.setText("Profile");
                        imgSearch.setVisibility(View.GONE);
                        return true;
                }

                return false;
            }
        });

        //show popup menu when click to image avatar
        imgProfileAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup = new PopupMenu(context,view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.action_log_out:
                                context.startActivity(new Intent(context, MainActivity.class));
                                homePageActivity.finish();
                                return true;

                            case R.id.action_settings:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                                txtTitle.setText("Profile");
                                navigationView.setSelectedItemId(R.id.nav_profile);
                                return true;
                        }
                        return false;
                    }
                });// to implement on click event on items of menu
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.home, popup.getMenu());
                popup.show();
            }
        });

        //show search activity
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, SearchActivity.class);
                intent.putExtra("username", viewModel.getUsername().getValue());
                startActivity(intent);
            }
        });
    }
    /******************************************************/
}
