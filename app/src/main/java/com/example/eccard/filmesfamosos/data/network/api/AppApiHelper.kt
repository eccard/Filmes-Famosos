package com.example.eccard.filmesfamosos.data.network.api

import com.example.eccard.filmesfamosos.data.network.model.MovieResponse
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResponse
import com.example.eccard.filmesfamosos.data.network.model.MovieTrailersReviewResponse
import retrofit2.Response
import javax.inject.Inject


class AppApiHelper @Inject constructor(private val moviesApi : MoviesApi) {

    enum class MovieOrderType {
        POPULAR,
        TOP_RATED,
        TOP_BOOKMARK
    }

//    override fun generatePosterPath(imagePath: String?): URL? {
//        val uri = Uri.parse(AppConstants.ENDPOINT_MOVIES_POSTER_BASE_URL + imagePath)
//
//        var url: URL? = null
//
//        try {
//            url = URL(uri.toString())
//        } catch (e: MalformedURLException) {
//            e.printStackTrace()
//        }
//
//
//        return url
//    }

    suspend fun doGetMoviesApiCall(movieOrderType: MovieOrderType?, page: Int):
            Response<MovieResponse> {
        return if (movieOrderType == MovieOrderType.POPULAR) {
            moviesApi.doGetPopularMovies(page)
        } else {
            moviesApi.doGetTopRatedMovies(page)
        }
    }

    suspend fun doGetTrailersFromMovieApiCall(movieId: Int, page: Int): Response<MovieTrailersReviewResponse> {
        return moviesApi.doGetTrailersFromMovieApiCall(movieId, page)
    }

    suspend fun doGetReviewsFromMovieApiCall(movieId: Int, page: Int): Response<MovieReviewResponse> {
        return moviesApi.doGetReviewsFromMovieApiCall(movieId, page)
    }
}
