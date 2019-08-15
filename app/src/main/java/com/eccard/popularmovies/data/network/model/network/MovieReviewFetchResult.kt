package com.eccard.popularmovies.data.network.model.network

import androidx.room.Entity
import androidx.room.TypeConverters
import com.eccard.popularmovies.data.network.database.MovieTypeConverters

@Entity(primaryKeys = ["movieId"])
@TypeConverters(MovieTypeConverters::class)
data class MovieReviewFetchResult(
        val movieId : Int,
        val reviewIds: List<String>,
        val totalCount: Int,
        val next: Int?
)