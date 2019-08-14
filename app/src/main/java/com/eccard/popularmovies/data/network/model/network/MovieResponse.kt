package com.eccard.popularmovies.data.network.model.network

import com.eccard.popularmovies.data.network.model.MovieResult

data class MovieResponse(
        val page: Int,
        val results: List<MovieResult>,
        val total_results: Int,
        val total_pages: Int
){
    var nextPage : Int? = null
}