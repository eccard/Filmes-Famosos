package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FrgReviews extends Fragment {

    public static final String TAG = FrgReviews.class.getSimpleName();

    private MovieResult movieResult;
    private ReviewsAdapter reviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_trailers,container,false);


        RecyclerView recyclerView = view.findViewById(R.id.rv_trailers);
        reviewAdapter = new ReviewsAdapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(reviewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable((ContextCompat.getDrawable(getContext(), R.drawable.divider)));

        recyclerView.addItemDecoration(dividerItemDecoration);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(MovieResult.class.getSimpleName())) {
            movieResult = intent.getParcelableExtra(MovieResult.class.getSimpleName());


            getMovieReviews(movieResult.getId(),1);
        }

        return view;
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
                                   reviewAdapter.setReviews(movieResponse.getResults());
                                   reviewAdapter.notifyDataSetChanged();
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