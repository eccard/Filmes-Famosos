package com.eccard.popularmovies.data.network.model.network

import com.eccard.popularmovies.data.network.model.Movie

data class MovieResponse(
        val page: Int,
        val results: List<Movie>,
        val total_results: Int,
        val total_pages: Int
){
    var nextPage : Int? = null
}