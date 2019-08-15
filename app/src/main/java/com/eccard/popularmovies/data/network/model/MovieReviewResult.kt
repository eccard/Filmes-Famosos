package com.eccard.popularmovies.data.network.model

import androidx.room.Entity

@Entity(tableName = "movieReview", primaryKeys = [("id")])
data class MovieReviewResult(
        val id: String,
        val author: String,
        val content: String,
        val url: String
)