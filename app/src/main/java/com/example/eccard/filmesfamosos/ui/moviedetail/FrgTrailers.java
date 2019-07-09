package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.example.eccard.filmesfamosos.data.network.model.MovieTrailersReviewResponse;

import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.ACTION_VIEW;
import static com.example.eccard.filmesfamosos.ui.moviedetail.FrgSummary.TAG;

public class FrgTrailers extends Fragment implements TrailerAdapter.OnViewClicked {

    private TrailerAdapter trailerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_trailers,container,false);


        RecyclerView recyclerView = view.findViewById(R.id.rv_trailers);
        trailerAdapter = new TrailerAdapter(getContext(),this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trailerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable((ContextCompat.getDrawable(getContext(), R.drawable.divider)));

        recyclerView.addItemDecoration(dividerItemDecoration);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(MovieResult.class.getSimpleName())) {
            MovieResult movieResult = intent.getParcelableExtra(MovieResult.class.getSimpleName());


            getMovieTrailer(movieResult.getId(),1);
        }

        return view;
    }


    private void getMovieTrailer(final int movieId, int page){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(AppApiHelper.getInstance()
                .doGetTrailersFromMovieApiCall(movieId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieTrailersReviewResponse>() {
                               @Override
                               public void accept(MovieTrailersReviewResponse movieResponse) throws Exception {
                                   Log.d(TAG,movieResponse.toString());
                                   trailerAdapter.setTrailerResults(movieResponse.getResults());
                                   trailerAdapter.notifyDataSetChanged();
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

    @Override
    public void onVideoClicked(String videoKey) {
        Intent videoIntent = new Intent();
        videoIntent.setAction(ACTION_VIEW);
        videoIntent.setData(buildVideoUri(videoKey));

        if(videoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(videoIntent);
        }
    }



    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    private static Uri buildVideoUri(String videoKey) {
        return Uri.parse(YOUTUBE_BASE_URL)
                .buildUpon()
                .appendQueryParameter("v", videoKey)
                .build();
    }

    public static URL buildVideoUrl(String videoKey) {

        Uri builtUri = buildVideoUri(videoKey);

        URL videoUrl = null;

        try {
            videoUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return videoUrl;
    }
}
