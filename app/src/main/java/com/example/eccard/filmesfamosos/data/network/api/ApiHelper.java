package com.example.eccard.filmesfamosos.data.network.api;

import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;

import java.net.URL;

import io.reactivex.Single;

interface ApiHelper {

    URL generatePosterPath(String imagePath);

    Single<MovieResponse> doGetMoviesApiCall(AppApiHelper.MovieOrderType movieOrderType, int page);

}
