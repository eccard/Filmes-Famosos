package com.eccard.popularmovies.data.network.model.network

import com.eccard.popularmovies.data.network.model.MovieTrailer

data class MovieTrailersResponse(
        val id: Int,
        val page: Int,
        val results: List<MovieTrailer>,
        val total_pages: Int,
        val total_results: Int
){
    var nextPage : Int? = null
}