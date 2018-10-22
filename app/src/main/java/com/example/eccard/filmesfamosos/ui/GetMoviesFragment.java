package com.example.eccard.filmesfamosos.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.eccard.filmesfamosos.data.network.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class GetMoviesFragment extends Fragment {

    public static final String TAG = GetMoviesFragment.class.getSimpleName();


    private List<MovieResult> retainMovies = null;

    public List<MovieResult> getRetainMovies() {
        return retainMovies;
    }

    private void setRetainMovies(List<MovieResult> retainMovies) {
        this.retainMovies = retainMovies;
    }

    private CompositeDisposable compositeDisposable;

    interface GetMoviesCallbacks {
        void onMoviesResult(List<MovieResult> movies);
        void onMovieError(Throwable throwable);
    }

    private GetMoviesCallbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }

    public void getData(int pageIndex, AppApiHelper.MovieOrderType mCurrentMovieOrderType){

        // TODO get next pages
        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(AppApiHelper.getInstance()
                .doGetMoviesApiCall(mCurrentMovieOrderType,pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                               @Override
                               public void accept(MovieResponse movieResponse) throws Exception {
                                   Log.d(TAG,movieResponse.toString());
                                   setRetainMovies(movieResponse.getMovieResults());

                                   if ( mCallbacks != null){
                                       mCallbacks.onMoviesResult(movieResponse.getMovieResults());
                                   }else{
                                       Log.e(TAG,"mCallbacks == nul");
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG,throwable.toString());
                                if ( mCallbacks != null){
                                    mCallbacks.onMovieError(throwable);
                                }else{
                                    Log.e(TAG,"mCallbacks == nul");
                                }
                            }
                        }
                )
        );
    }
}
