package com.eccard.popularmovies.data.network.api

import com.eccard.popularmovies.data.network.model.network.MovieResponse
import com.eccard.popularmovies.data.network.model.network.MovieReviewResponse
import com.eccard.popularmovies.data.network.model.network.MovieTrailersReviewResponse
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
            moviesApi.doGetPopularMovies(page).execute()
        } else {
            moviesApi.doGetTopRatedMovies(page).execute()
        }
    }

    suspend fun doGetTrailersFromMovieApiCall(movieId: Int): Response<MovieTrailersReviewResponse> {
        return moviesApi.doGetTrailersFromMovieApiCall(movieId)
    }
}
