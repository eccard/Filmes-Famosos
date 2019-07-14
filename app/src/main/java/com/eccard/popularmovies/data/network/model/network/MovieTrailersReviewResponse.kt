package com.eccard.filmesfamosos.data.network.model.network

import com.eccard.filmesfamosos.data.network.model.TrailerResult

data class MovieTrailersReviewResponse(
        val id: Int,
        val page: Int,
        val results: List<TrailerResult>,
        val total_pages: Int,
        val total_results: Int
)