package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.eccard.filmesfamosos.R;
import com.google.android.material.tabs.TabLayout;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    static public Intent newIntent(Context context){
        return new Intent(context, MovieDetailActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ViewPager mViewPager = findViewById(R.id.pager);

        MoviePageAdapter moviePageAdapter = new MoviePageAdapter(getSupportFragmentManager(), this);

        mViewPager.setAdapter(moviePageAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

    }


}
