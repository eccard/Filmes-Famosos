package com.example.eccard.filmesfamosos.data.network;

import io.reactivex.Single;

import com.example.eccard.filmesfamosos.BuildConfig;
import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;
import com.example.eccard.filmesfamosos.utils.Constants;
import com.rx2androidnetworking.Rx2AndroidNetworking;

public class AppApiHelper implements ApiHelper{

    private static AppApiHelper instance;


    private AppApiHelper(){}

    public static AppApiHelper getInstance() {

        if ( instance == null){
            instance = new AppApiHelper();
        }

        return instance;
    }

    @Override
    public Single<MovieResponse> doGetPopularMoviesApiCall() {
        String url = ApiEndPoint.ENDPOINT_POPULAR_MOVIES;
        return Rx2AndroidNetworking.post(url)
                .addQueryParameter(Constants.API_KEY,BuildConfig.THEMOVIEDB_API_KEY)
                .build()
                .getObjectSingle(MovieResponse.class);
    }
}
