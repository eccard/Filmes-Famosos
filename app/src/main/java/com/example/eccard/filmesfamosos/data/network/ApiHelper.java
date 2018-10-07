package com.example.eccard.filmesfamosos.data.network;

import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;
import io.reactivex.Single;

public interface ApiHelper {

    Single<MovieResponse> doGetPopularMoviesApiCall();

}
