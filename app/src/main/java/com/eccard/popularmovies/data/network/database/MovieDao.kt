package com.eccard.popularmovies.data.network.database

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.*
import com.eccard.popularmovies.data.network.model.Movie
import com.eccard.popularmovies.data.network.model.MovieReview
import com.eccard.popularmovies.data.network.model.MovieTrailer
import com.eccard.popularmovies.data.network.model.network.MovieFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieReviewFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieTrailerFetchResult
import java.util.*

@Dao
abstract class MovieDao {

    @Query("SELECT * FROM movie")
    abstract fun loadAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    abstract fun getMovieResult(movieId: Int): LiveData<Movie>

    // todo add a propertie to bookmarked movie
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovies(movies: List<Movie>)

    @Delete
    abstract fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM movie WHERE id in (:movieIds)")
    abstract fun loadById(movieIds: List<Int>) : LiveData<List<Movie>>

    fun loadOrdered(repoIds: List<Int>): LiveData<List<Movie>> {
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

    @Query("SELECT * FROM movie WHERE bookmarked = :bookMarked")
    abstract fun loadAllMoviesWithBookmarked(bookMarked : Boolean): LiveData<List<Movie>>



    @Query("SELECT * FROM MovieReviewFetchResult WHERE movieId = :movieId")
    abstract fun searchMovieReviewResult(movieId : Int) : LiveData<MovieReviewFetchResult>

    @Query("SELECT * FROM MovieReviewFetchResult WHERE movieId = :movieId")
    abstract fun findSearchMovieReviewResult(movieId : Int) : MovieReviewFetchResult?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovieReviewFetch(movieReviewFetchResult: MovieReviewFetchResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovieReviews(movieReviews: List<MovieReview>)

    @Query("SELECT * FROM movieReview WHERE id in (:movieReviewIds)")
    abstract fun loadMovieReviews(movieReviewIds: List<String>) : LiveData<List<MovieReview>>



    @Query("SELECT * FROM MovieTrailerFetchResult WHERE movieId = :movieId")
    abstract fun searchMovieTrailerFetchResult(movieId : Int) : LiveData<MovieTrailerFetchResult>

    @Query("SELECT * FROM MovieTrailerFetchResult WHERE movieId = :movieId")
    abstract fun findSearchMovieTrailerFetched(movieId : Int) : MovieTrailerFetchResult?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovieTrailerFetch(movieTrailerFetchResult: MovieTrailerFetchResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovieTrailer(movieReviews: List<MovieTrailer>)

    @Query("SELECT * FROM movieTrailer WHERE id in (:movieTrailerIds)")
    abstract fun loadMovieTrailers(movieTrailerIds: List<String>) : LiveData<List<MovieTrailer>>

}
