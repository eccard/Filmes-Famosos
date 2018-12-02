package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.eccard.filmesfamosos.R;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    static public Intent newIntent(Context context){
        return new Intent(context, MovieDetailActivity.class);
    }


    ViewPager mViewPager;



    private MoviePageAdapter moviePageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        moviePageAdapter = new MoviePageAdapter( getSupportFragmentManager() , this);

        mViewPager.setAdapter(moviePageAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

    }


}
