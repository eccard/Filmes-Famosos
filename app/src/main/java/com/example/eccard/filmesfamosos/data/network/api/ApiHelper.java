package com.example.eccard.filmesfamosos.data.network.api;

import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieTrailersReviewResponse;

import java.net.URL;

import io.reactivex.Single;

interface ApiHelper {

    URL generatePosterPath(String imagePath);

    Single<MovieResponse> doGetMoviesApiCall(AppApiHelper.MovieOrderType movieOrderType, int page);


    Single<MovieTrailersReviewResponse> doGetTrailersFromMovieApiCall(int movieId, int page);

    Single<MovieReviewResponse> doGetReviewsFromMovieApiCall(int movieId, int page);



}
