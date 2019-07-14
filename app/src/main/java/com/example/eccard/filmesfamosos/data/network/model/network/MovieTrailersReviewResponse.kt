package com.example.eccard.filmesfamosos.data.network.model.network

import com.example.eccard.filmesfamosos.data.network.model.TrailerResult

data class MovieTrailersReviewResponse(
        val id: Int,
        val page: Int,
        val results: List<TrailerResult>,
        val total_pages: Int,
        val total_results: Int
)