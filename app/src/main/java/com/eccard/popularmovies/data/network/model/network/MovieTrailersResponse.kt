package com.eccard.popularmovies.data.network.model.network

import com.eccard.popularmovies.data.network.model.TrailerResult

data class MovieTrailersResponse(
        val id: Int,
        val page: Int,
        val results: List<TrailerResult>,
        val total_pages: Int,
        val total_results: Int
)