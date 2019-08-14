package com.eccard.popularmovies.data.network.database

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.*
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.data.repository.MovieFetchResult
import java.util.*

@Dao
abstract class MovieDao {

    @Query("SELECT * FROM movie")
    abstract fun loadAllMovies(): LiveData<List<MovieResult>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    abstract fun getMovieResult(movieId: Int): LiveData<MovieResult>

    // todo add a propertie to bookmarked movie
    @Insert
    abstract fun insertMovie(movieResult: MovieResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovies(movieResults: List<MovieResult>)

    @Delete
    abstract fun deleteMovie(movieResult: MovieResult)

    @Query("SELECT * FROM movie WHERE id in (:movieIds)")
    abstract fun loadById(movieIds: List<Int>) : LiveData<List<MovieResult>>

    fun loadOrdered(repoIds: List<Int>): LiveData<List<MovieResult>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(repoIds)) { repositories ->
            Collections.sort(repositories) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            repositories
        }
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(resut : MovieFetchResult)

    @Query("SELECT * FROM MovieFetchResult WHERE orderType = :orderType")
    abstract fun searchMovieResult(orderType : String) : LiveData<MovieFetchResult>

    @Query("SELECT * FROM MovieFetchResult WHERE orderType = :orderType")
    abstract fun findSearchMoveiResult(orderType : String) : MovieFetchResult?
}
