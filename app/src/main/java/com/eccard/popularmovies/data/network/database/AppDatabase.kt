package com.eccard.popularmovies.data.network.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eccard.popularmovies.data.network.model.MovieResult

@Database(entities = [MovieResult::class], version = 1, exportSchema = false)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao


}