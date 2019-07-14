package com.example.eccard.filmesfamosos.data.network.api

import com.example.eccard.filmesfamosos.data.network.model.network.MovieResponse
import com.example.eccard.filmesfamosos.data.network.model.network.MovieReviewResponse
import com.example.eccard.filmesfamosos.data.network.model.network.MovieTrailersReviewResponse
import retrofit2.Response
import javax.inject.Inject


class AppApiHelper @Inject constructor(private val moviesApi : MoviesApi) {

    enum class MovieOrderType {
        POPULAR,
        TOP_RATED,
        TOP_BOOKMARK
    }

    suspend fun doGetMoviesApiCall(movieOrderType: MovieOrderType?, page: Int):
            Response<MovieResponse> {
        return if (movieOrderType == MovieOrderType.POPULAR) {
            moviesApi.doGetPopularMovies(page)
        } else {
            moviesApi.doGetTopRatedMovies(page)
        }
    }

    suspend fun doGetTrailersFromMovieApiCall(movieId: Int): Response<MovieTrailersReviewResponse> {
        return moviesApi.doGetTrailersFromMovieApiCall(movieId)
    }

    suspend fun doGetReviewsFromMovieApiCall(movieId: Int, page: Int): Response<MovieReviewResponse> {
        return moviesApi.doGetReviewsFromMovieApiCall(movieId, page)
    }
}
