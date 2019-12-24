package com.example.movieproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.movieproject.Helpers.MovieDbService;
import com.example.movieproject.Helpers.ViewPagerAdapter;
import com.example.movieproject.Fragments.FavouritesFragment;
import com.example.movieproject.Fragments.HomeFragment;
import com.example.movieproject.Fragments.NowPlayingFragment;
import com.example.movieproject.Fragments.ProfileFragment;
import com.example.movieproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter adapter;
    private MenuItem selectedMenuItem;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeVariables();
        setupViewPager();
    }

    private void initializeVariables() {
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);

        currentUserEmail = getIntent().getStringExtra("email");
    }

    private void setupViewPager(){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new FavouritesFragment());
        adapter.addFragment(new NowPlayingFragment());
        adapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
    }

    public void setNavigationItem(int position){
        if (selectedMenuItem != null){
            selectedMenuItem.setChecked(false);
        }
        else {
            bottomNavigationView.getMenu().getItem(0).setChecked(false);
        }
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        selectedMenuItem = bottomNavigationView.getMenu().getItem(position);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_home:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.menu_favourites:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.menu_nowPlaying:
                viewPager.setCurrentItem(2);
                return true;
            case R.id.menu_profile:
                viewPager.setCurrentItem(3);
                return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }
}
