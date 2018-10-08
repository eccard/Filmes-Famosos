package com.example.eccard.filmesfamosos.data.network;

import com.example.eccard.filmesfamosos.BuildConfig;

public final class ApiEndPoint {

    public static final String ENDPOINT_POPULAR_MOVIES = BuildConfig.BASE_URL
            + "/movie/popular";

    public static final String ENDPOINT_MOVIES_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185/";
}
