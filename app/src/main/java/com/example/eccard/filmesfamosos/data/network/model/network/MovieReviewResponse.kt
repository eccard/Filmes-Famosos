package com.example.eccard.filmesfamosos.data.network.model.network

import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResult

data class MovieReviewResponse(
        val id: Int,
        val page: Int,
        val results: List<MovieReviewResult>,
        val total_pages: Int,
        val total_results: Int
)