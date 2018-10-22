package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView imgPoster = findViewById(R.id.img_view_detail_activity);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvRate = findViewById(R.id.tv_rate);
        TextView tvLauchDate = findViewById(R.id.tv_lauch_date);
        TextView tvOverView = findViewById(R.id.tv_over_view);


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

        }
    }
}
