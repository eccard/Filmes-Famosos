package com.eccard.popularmovies.data.network.model

import androidx.room.Entity

@Entity(tableName = "movieTrailer", primaryKeys = [("id")])
data class TrailerResult(
        val id: String,
        val name: String,
        val key: String
)