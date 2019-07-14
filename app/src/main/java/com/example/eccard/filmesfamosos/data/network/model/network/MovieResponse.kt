package com.example.eccard.filmesfamosos.data.network.model.network

import com.example.eccard.filmesfamosos.data.network.model.MovieResult

data class MovieResponse(
        val page: Int,
        val results: List<MovieResult>,
        val total_results: Int,
        val total_pages: Int
)