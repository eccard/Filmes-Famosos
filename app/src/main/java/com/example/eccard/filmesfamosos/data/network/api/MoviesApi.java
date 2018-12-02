package com.example.eccard.filmesfamosos.data.network.api;

import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieTrailersReviewResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MoviesApi {

    @GET("movie/popular")
    Single<MovieResponse> doGetPopularMovies( @Query("page") int page);

    @GET("movie/top_rated")
    Single<MovieResponse> doGetTopRatedMovies( @Query("page") int page);


    @GET("movie/{movie_id}/videos")
    Single<MovieTrailersReviewResponse> doGetTrailersFromMovieApiCall(@Path("movie_id") int movieId, @Query("page") int page);


    @GET("movie/{movie_id}/reviews")
    Single<MovieReviewResponse> doGetReviewsFromMovieApiCall(@Path("movie_id") int movieId, @Query("page") int page);


//    Single<Response<MovieVideosResponse>> findVideos(@Path("movie_id") int movieId);
//    Single<Response<MovieReviewsResponse>> findReviews(@Path("movie_id") int movieId);


}

