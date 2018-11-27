package com.example.eccard.filmesfamosos.data.network.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.eccard.filmesfamosos.data.network.model.MovieResult;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    List<MovieResult> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id = :movieId")
    MovieResult getMovieResult(int movieId);

    @Insert
    void insertMovie(MovieResult movieResult);

    @Delete
    void deleteMovie(MovieResult movieResult);
}
