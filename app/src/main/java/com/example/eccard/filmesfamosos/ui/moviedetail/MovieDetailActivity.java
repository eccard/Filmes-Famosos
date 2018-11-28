package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.database.AppDatabase;
import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieTrailersReviewResponse;
import com.squareup.picasso.Picasso;

import java.net.URL;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private MovieResult movieResult;

    static public Intent newIntent(Context context){
        return new Intent(context, MovieDetailActivity.class);
    }

    AppDatabase mDb;

    private boolean alreadInFavoritos(int movieId){
        boolean alreadeInFavoritos = false;
        if (mDb.movieDao().getMovieResult(movieId) != null){
            alreadeInFavoritos = true;
        }
        return alreadeInFavoritos;
    }

    ImageView btnBookMark;
    boolean movieIsBookmarked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView imgPoster = findViewById(R.id.img_view_detail_activity);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvRate = findViewById(R.id.tv_rate);
        TextView tvLauchDate = findViewById(R.id.tv_lauch_date);
        TextView tvOverView = findViewById(R.id.tv_over_view);
        btnBookMark = findViewById(R.id.btn_bookmark);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();

        if (intent.hasExtra(MovieResult.class.getSimpleName())) {
            movieResult = intent.getParcelableExtra(MovieResult.class.getSimpleName());

            movieIsBookmarked = alreadInFavoritos(movieResult.getId());
            updateBookmarkedState(movieIsBookmarked);

            URL posterUrl = AppApiHelper.getInstance()
                    .generatePosterPath(movieResult.getPosterPath());

            Picasso.get().load(posterUrl.toString()).into(imgPoster);
            tvTitle.setText(movieResult.getOriginalTitle());
            tvRate.setText(String.valueOf(movieResult.getVoteAverage()));
            tvLauchDate.setText(movieResult.getReleaseDate());
            tvOverView.setText(movieResult.getOverview());

            getMovieTrailler(movieResult.getId(),1);

        }

        btnBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movieResult != null) {
                    if (!movieIsBookmarked) {
                        mDb.movieDao().insertMovie(movieResult);
                        movieIsBookmarked = true;
                    } else {
                        mDb.movieDao().deleteMovie(movieResult);
                        movieIsBookmarked = false;

                    }
                    updateBookmarkedState(movieIsBookmarked);
                } else {
                    Log.d(TAG, "btnbookMark onClick movieResult == null");
                }
            }
        });

    }

    private void updateBookmarkedState(boolean isBookmarked){
        Drawable draw;
        if (isBookmarked){
            draw = ResourcesCompat.getDrawable(getResources(),
                    android.R.drawable.btn_star_big_on,
                    null);

        } else {
            draw = ResourcesCompat.getDrawable(getResources(),
                    android.R.drawable.btn_star_big_off,
                    null);

        }
        btnBookMark.setImageDrawable(draw);
    }

    private void getMovieReviews(int movieId, int page){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(AppApiHelper.getInstance()
                .doGetReviewsFromMovieApiCall(movieId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieReviewResponse>() {
                               @Override
                               public void accept(MovieReviewResponse movieResponse) throws Exception {
                                   Log.d(TAG,movieResponse.toString());
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG,throwable.getLocalizedMessage());
                            }
                        }
                )
        );
    }

    private void getMovieTrailler(int movieId,int page){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(AppApiHelper.getInstance()
                .doGetTrailersFromMovieApiCall(movieId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieTrailersReviewResponse>() {
                               @Override
                               public void accept(MovieTrailersReviewResponse movieResponse) throws Exception {
                                   Log.d(TAG,movieResponse.toString());
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG,throwable.getLocalizedMessage());
                            }
                        }
                )
        );
    }
}
