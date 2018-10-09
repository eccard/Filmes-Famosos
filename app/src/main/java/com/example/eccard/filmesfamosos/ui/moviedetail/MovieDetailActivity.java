package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    static public Intent newIntent(Context context){
        return new Intent(context, MovieDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        if ( intent.hasExtra(MovieResult.class.getSimpleName())){
            MovieResult movieResult = intent.getParcelableExtra(MovieResult.class.getSimpleName());

            Log.d(TAG, " chegou na activity 2" + movieResult.toString());
        }
    }
}
