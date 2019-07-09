package com.example.eccard.filmesfamosos.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;


public class GetMoviesFragment extends Fragment {

    public static final String TAG = GetMoviesFragment.class.getSimpleName();


    private List<MovieResult> retainMovies = null;
//    private GetMoviesViewModel getMoviesViewModel;

    public List<MovieResult> getRetainMovies() {
        return retainMovies;
    }

    private void appendMovieResults(List<MovieResult> movieResults) {
        if ( this.retainMovies == null){
            this.retainMovies = movieResults;
        }else{
            this.retainMovies.addAll(movieResults);
        }
    }

    public void resetMovieResults(){
        if (this.retainMovies != null){
            this.retainMovies.clear();
        }
    }

    private CompositeDisposable compositeDisposable;

//    public interface GetMoviesCallbacks {
//        void onMoviesResult(List<MovieResult> movies);
//        void onMovieError(Throwable throwable);
//    }

//    private GetMoviesCallbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mCallbacks = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getMoviesViewModel = ViewModelProviders.of(this)
//                .get(GetMoviesViewModel.class);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        removeObservers();
    }

    @Override
    public void onDetach() {
        super.onDetach();

//        mCallbacks = null;
    }

    public void getData(int pageIndex, AppApiHelper.MovieOrderType mCurrentMovieOrderType) {

        // TODO get next pages
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        if (mCurrentMovieOrderType == AppApiHelper.MovieOrderType.TOP_BOOKMARK) {

//            if (mCallbacks != null) {

//                getMoviesViewModel.getMovies().observe(this, new Observer<List<MovieResult>>() {
//                    @Override
//                    public void onChanged(@Nullable List<MovieResult> movieResults) {
//                        mCallbacks.onMoviesResult(movieResults);
//                    }
//                });


//            } else {
//                Log.e(TAG, "mCallbacks == nul");
//            }
        } else {
/*
            removeObservers();

            compositeDisposable.add(AppApiHelper.getInstance()
                    .doGetMoviesApiCall(mCurrentMovieOrderType, pageIndex)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MovieResponse>() {
                                   @Override
                                   public void accept(MovieResponse movieResponse) throws Exception {
                                       Log.d(TAG, movieResponse.toString());

                                       appendMovieResults(movieResponse.getMovieResults());

                                       if (mCallbacks != null) {
                                           mCallbacks.onMoviesResult(retainMovies);
                                       } else {
                                           Log.e(TAG, "mCallbacks == nul");
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Log.e(TAG, throwable.toString());
                                    if (mCallbacks != null) {
                                        mCallbacks.onMovieError(throwable);
                                    } else {
                                        Log.e(TAG, "mCallbacks == nul");
                                    }
                                }
                            }
                    )
            );*/
        }
    }

//    private void removeObservers() {
//        if ( getMoviesViewModel != null && getMoviesViewModel.getMovies().hasObservers()){
//            getMoviesViewModel.getMovies().removeObservers(this);
//        }
//    }
}
