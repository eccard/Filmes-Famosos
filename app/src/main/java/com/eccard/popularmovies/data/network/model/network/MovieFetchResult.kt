package com.eccard.popularmovies.data.network.model.network

import androidx.room.Entity
import androidx.room.TypeConverters
import com.eccard.popularmovies.data.network.database.MovieTypeConverters

@Entity(primaryKeys = ["orderType"])
@TypeConverters(MovieTypeConverters::class)
data class MovieFetchResult(
        val orderType: String,
        val movieIds: List<Int>,
        val totalCount: Int,
        val next: Int?
)