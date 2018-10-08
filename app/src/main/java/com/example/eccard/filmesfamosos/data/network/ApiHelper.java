package com.example.eccard.filmesfamosos.data.network;

import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;

import java.net.URL;

import io.reactivex.Single;

public interface ApiHelper {

    URL generatePosterPath(String imagePath);

    Single<MovieResponse> doGetPopularMoviesApiCall();

}
