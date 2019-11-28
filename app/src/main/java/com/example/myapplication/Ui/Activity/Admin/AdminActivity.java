package com.example.myapplication.Ui.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.Ui.Activity.MainActivity;
import com.example.myapplication.Ui.Fragment.Admin.ManageAccountFragment;
import com.example.myapplication.Ui.Fragment.Admin.ManageCommentFragment;
import com.example.myapplication.Ui.Fragment.Admin.ManageFilmFragment;
import com.example.myapplication.Ui.Fragment.Admin.StatisticFragment;
import com.example.myapplication.Ui.Fragment.Client.HomeFragment;
import com.example.myapplication.Ui.Fragment.Client.ProfileFragment;
import com.example.myapplication.Ui.Fragment.Client.SavedFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view_admin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ManageFilmFragment()).addToBackStack(null).commit();
        toolbar.setTitle("Management Films");
        navigationView.setCheckedItem(R.id.nav_films);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_films:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ManageFilmFragment()).addToBackStack(null).commit();
                toolbar.setTitle("Management Films");
                navigationView.setCheckedItem(R.id.nav_films);
                break;
            case R.id.nav_comments:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ManageCommentFragment()).addToBackStack(null).commit();
                toolbar.setTitle("Management Comments");
                navigationView.setCheckedItem(R.id.nav_comments);
                break;
            case R.id.nav_accounts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ManageAccountFragment()).addToBackStack(null).commit();
                toolbar.setTitle("Management Accounts");
                navigationView.setCheckedItem(R.id.nav_accounts);
                break;
            case R.id.nav_logout:
                this.startActivity(new Intent(AdminActivity.this, MainActivity.class));
                this.finish();
                navigationView.setCheckedItem(R.id.nav_logout);
                break;
            case R.id.nav_settings:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_admin, new StatisticFragment())
                        .addToBackStack(null).commit();
                toolbar.setTitle("Statistic");
                navigationView.setCheckedItem(R.id.nav_settings);
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
