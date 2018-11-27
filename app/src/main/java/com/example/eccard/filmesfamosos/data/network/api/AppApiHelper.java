package com.example.eccard.filmesfamosos.data.network.api;

import android.net.Uri;

import io.reactivex.Single;

import com.example.eccard.filmesfamosos.BuildConfig;
import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;
import com.example.eccard.filmesfamosos.utils.Constants;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.net.MalformedURLException;
import java.net.URL;

public class AppApiHelper implements ApiHelper{

    private static AppApiHelper instance;

    public enum MovieOrderType {
        POPULAR,
        TOP_RATED
    }

    private AppApiHelper(){}

    public static AppApiHelper getInstance() {

        if ( instance == null){
            instance = new AppApiHelper();
        }

        return instance;
    }

    @Override
    public URL generatePosterPath(String imagePath){
        Uri uri = Uri.parse(ApiEndPoint.ENDPOINT_MOVIES_POSTER_BASE_URL +imagePath );

        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    @Override
    public Single<MovieResponse> doGetMoviesApiCall(MovieOrderType movieOrderType, int page) {
        String url;
        if (movieOrderType == MovieOrderType.POPULAR ){
            url = ApiEndPoint.ENDPOINT_POPULAR_MOVIES;
        }else {
            url = ApiEndPoint.ENDPOINT_TOP_RATED_MOVIES;
        }
        return Rx2AndroidNetworking.post(url)
                .addQueryParameter(Constants.API_KEY,BuildConfig.THEMOVIEDB_API_KEY)
                .addQueryParameter(Constants.PAGE,String.valueOf(page))
                .build()
                .getObjectSingle(MovieResponse.class);
    }
}
