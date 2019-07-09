package com.example.eccard.filmesfamosos.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eccard.filmesfamosos.data.network.database.AppDatabase;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;

import java.util.List;

public class GetMoviesViewModel extends AndroidViewModel {
    public GetMoviesViewModel(@NonNull Application application) {
        super(application);

        mMovies = AppDatabase.getInstance(application.getApplicationContext()).movieDao().loadAllMovies();
    }

    final LiveData<List<MovieResult>> mMovies;

    public LiveData<List<MovieResult>> getMovies() {
        return mMovies;
    }
}
