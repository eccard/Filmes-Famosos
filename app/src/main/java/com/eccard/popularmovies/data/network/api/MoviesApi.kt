package com.eccard.popularmovies.data.network.api

import androidx.lifecycle.LiveData
import com.eccard.popularmovies.data.network.model.network.MovieResponse
import com.eccard.popularmovies.data.network.model.network.MovieReviewResponse
import com.eccard.popularmovies.data.network.model.network.MovieTrailersReviewResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    // todo remove this
    @GET("movie/popular")
    fun doGetPopularMovies(@Query("page") page: Int): Call<MovieResponse>

    @GET("movie/popular")
    fun doGetPopularMoviesFirstPage(@Query("page") page: Int): LiveData<ApiResponse<MovieResponse>>



    // todo remove this
    @GET("movie/top_rated")
    fun doGetTopRatedMovies(@Query("page") page: Int): Call<MovieResponse>

    @GET("movie/top_rated")
    fun doGetTopRatedMoviesFirstPage(@Query("page") page: Int): LiveData<ApiResponse<MovieResponse>>


    @GET("movie/{movie_id}/videos")
    suspend fun doGetTrailersFromMovieApiCall(@Path("movie_id") movieId: Int): Response<MovieTrailersReviewResponse>



    @GET("movie/{movie_id}/reviews")
    fun doGetReviewsFromMovieApiCall(@Path("movie_id") movieId: Int, @Query("page") page: Int): Call<MovieReviewResponse>

    @GET("movie/{movie_id}/reviews")
    fun doGetReviewsFromMovieApiCall(@Path("movie_id") movieId: Int): LiveData<ApiResponse<MovieReviewResponse>>

}