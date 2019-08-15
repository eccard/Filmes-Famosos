package com.eccard.popularmovies.data.network.model.network

import com.eccard.popularmovies.data.network.model.MovieReviewResult

data class MovieReviewResponse(
        val id: Int,
        val page: Int,
        val results: List<MovieReviewResult>,
        val total_pages: Int,
        val total_results: Int
){
    var nextPage : Int? = null
}