package com.example.eccard.filmesfamosos.data.network.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.eccard.filmesfamosos.data.network.model.MovieResult

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun loadAllMovies(): List<MovieResult>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getMovieResult(movieId: Int): LiveData<MovieResult>

    @Insert
    fun insertMovie(movieResult: MovieResult)

    @Delete
    fun deleteMovie(movieResult: MovieResult)
}
