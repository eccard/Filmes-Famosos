package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.database.AppDatabase;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class FrgSummary extends Fragment {

    public static final String TAG = FrgSummary.class.getSimpleName();


    ImageView btnBookMark;


    AppDatabase mDb;
    boolean movieIsBookmarked = false;

    private MovieResult movieResult;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_summary,container,false);

        ImageView imgPoster = view.findViewById(R.id.img_view_detail_activity);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvRate = view.findViewById(R.id.tv_rate);
        TextView tvLauchDate = view.findViewById(R.id.tv_lauch_date);
        TextView tvOverView = view.findViewById(R.id.tv_over_view);
        btnBookMark = view.findViewById(R.id.btn_bookmark);

        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        Intent intent = getActivity().getIntent();

        if (intent.hasExtra(MovieResult.class.getSimpleName())) {
            movieResult = intent.getParcelableExtra(MovieResult.class.getSimpleName());

            getActivity().setTitle(movieResult.getTitle());

            movieIsBookmarked = alreadInFavoritos(movieResult.getId());
            updateBookmarkedState(movieIsBookmarked);

            URL posterUrl = AppApiHelper.getInstance()
                    .generatePosterPath(movieResult.getPosterPath());

            Picasso.get().load(posterUrl.toString()).into(imgPoster);
            tvTitle.setText(movieResult.getOriginalTitle());
            tvRate.setText(String.valueOf(movieResult.getVoteAverage()));
            tvLauchDate.setText(movieResult.getReleaseDate());
            tvOverView.setText(movieResult.getOverview());



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
        return view;
    }



    private boolean alreadInFavoritos(int movieId){
        boolean alreadeInFavoritos = false;
        if (mDb.movieDao().getMovieResult(movieId) != null){
            alreadeInFavoritos = true;
        }
        return alreadeInFavoritos;
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




}
