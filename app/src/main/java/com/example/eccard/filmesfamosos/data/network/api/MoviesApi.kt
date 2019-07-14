package com.example.eccard.filmesfamosos.data.network.api

import com.example.eccard.filmesfamosos.data.network.model.network.MovieResponse
import com.example.eccard.filmesfamosos.data.network.model.network.MovieReviewResponse
import com.example.eccard.filmesfamosos.data.network.model.network.MovieTrailersReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/popular")
    suspend fun doGetPopularMovies(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun doGetTopRatedMovies(@Query("page") page: Int): Response<MovieResponse>


    @GET("movie/{movie_id}/videos")
    suspend fun doGetTrailersFromMovieApiCall(@Path("movie_id") movieId: Int): Response<MovieTrailersReviewResponse>


    @GET("movie/{movie_id}/reviews")
    suspend fun doGetReviewsFromMovieApiCall(@Path("movie_id") movieId: Int, @Query("page") page: Int): Response<MovieReviewResponse>}


//    Single<Response<MovieVideosResponse>> findVideos(@Path("movie_id") int movieId);
//    Single<Response<MovieReviewsResponse>> findReviews(@Path("movie_id") int movieId);

