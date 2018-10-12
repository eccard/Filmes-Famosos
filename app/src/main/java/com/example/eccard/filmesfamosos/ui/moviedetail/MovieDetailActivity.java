package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    static public Intent newIntent(Context context){
        return new Intent(context, MovieDetailActivity.class);
    }

    private ImageView imgPoster;
    private TextView tvTitle;
    private TextView tvRate;
    private TextView tvLauchDate;
    private TextView tvOverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        imgPoster = findViewById(R.id.img_view_detail_activity);
        tvTitle = findViewById(R.id.tv_title);
        tvRate = findViewById(R.id.tv_rate);
        tvLauchDate = findViewById(R.id.tv_lauch_date);
        tvOverView = findViewById(R.id.tv_over_view);


        Intent intent = getIntent();

        if ( intent.hasExtra(MovieResult.class.getSimpleName())){
            MovieResult movieResult = intent.getParcelableExtra(MovieResult.class.getSimpleName());



            URL posterUrl = AppApiHelper.getInstance()
                    .generatePosterPath(movieResult.getPosterPath());

            Picasso.get().load(posterUrl.toString()).into(imgPoster);
            tvTitle.setText(movieResult.getTitle());
            tvRate.setText(String.valueOf(movieResult.getVoteAverage()));
            tvLauchDate.setText(movieResult.getReleaseDate());
            tvOverView.setText(movieResult.getOverview());

            Log.d(TAG, " chegou na activity 2" + movieResult.toString());
        }
    }
}
