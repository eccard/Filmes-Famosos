package com.eccard.popularmovies.data.network.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.data.network.model.MovieReviewResult
import com.eccard.popularmovies.data.network.model.TrailerResult
import com.eccard.popularmovies.data.network.model.network.MovieFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieReviewFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieTrailerFetchResult

@Database(entities = [
    MovieResult::class,
    MovieFetchResult::class,
    MovieReviewFetchResult::class,
    MovieReviewResult::class,
    MovieTrailerFetchResult::class,
    TrailerResult::class],
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao


}