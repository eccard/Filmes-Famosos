package com.eccard.popularmovies.data.network.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eccard.popularmovies.data.network.model.Movie
import com.eccard.popularmovies.data.network.model.MovieReview
import com.eccard.popularmovies.data.network.model.MovieTrailer
import com.eccard.popularmovies.data.network.model.network.MovieFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieReviewFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieTrailerFetchResult

@Database(entities = [
    Movie::class,
    MovieFetchResult::class,
    MovieReviewFetchResult::class,
    MovieReview::class,
    MovieTrailerFetchResult::class,
    MovieTrailer::class],
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao


}