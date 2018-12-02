package com.example.eccard.filmesfamosos.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.eccard.filmesfamosos.data.network.database.AppDatabase;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;

import java.util.List;

public class GetMoviesViewModel extends AndroidViewModel {
    public GetMoviesViewModel(@NonNull Application application) {
        super(application);

        mMoviews = AppDatabase.getInstance(application.getApplicationContext()).movieDao().loadAllMovies();
    }

    LiveData<List<MovieResult>> mMoviews;

    public LiveData<List<MovieResult>> getMoviews() {
        return mMoviews;
    }
}
