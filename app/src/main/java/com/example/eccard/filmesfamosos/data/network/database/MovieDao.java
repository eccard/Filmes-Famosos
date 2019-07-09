package com.example.eccard.filmesfamosos.data.network.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.eccard.filmesfamosos.data.network.model.MovieResult;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieResult>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id = :movieId")
    LiveData<MovieResult> getMovieResult(int movieId);

    @Insert
    void insertMovie(MovieResult movieResult);

    @Delete
    void deleteMovie(MovieResult movieResult);
}
