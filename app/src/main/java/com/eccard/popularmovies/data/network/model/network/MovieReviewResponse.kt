package com.eccard.popularmovies.data.network.model.network

import com.eccard.popularmovies.data.network.model.MovieReview

data class MovieReviewResponse(
        val id: Int,
        val page: Int,
        val results: List<MovieReview>,
        val total_pages: Int,
        val total_results: Int
){
    var nextPage : Int? = null
}