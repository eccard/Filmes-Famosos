package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.example.eccard.filmesfamosos.data.network.model.MovieTrailersReviewResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.eccard.filmesfamosos.ui.moviedetail.FrgSummary.TAG;

public class FrgTrailers extends Fragment {

    private MovieResult movieResult;
    private RecyclerView recyclerView;
    private TrailerAdapter trailerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_trailers,container,false);


        recyclerView = view.findViewById(R.id.rv_trailers);
        trailerAdapter = new TrailerAdapter(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(trailerAdapter);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(MovieResult.class.getSimpleName())) {
            movieResult = intent.getParcelableExtra(MovieResult.class.getSimpleName());


            getMovieTrailler(movieResult.getId(),1);
        }

        return view;
    }


    private void getMovieTrailler(final int movieId, int page){
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
}
