package com.example.eccard.filmesfamosos.data.network.api;

import android.net.Uri;

import com.example.eccard.filmesfamosos.BuildConfig;
import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResponse;
import com.example.eccard.filmesfamosos.data.network.model.MovieTrailersReviewResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppApiHelper implements ApiHelper{


    private static MoviesApi moviesApi;


    private static AppApiHelper instance;

    public enum MovieOrderType {
        POPULAR,
        TOP_RATED,
        TOP_BOOKMARK
    }

    private AppApiHelper(){}

    public static AppApiHelper getInstance() {

        if ( instance == null){


            HttpLoggingInterceptor loggingInterceptor =
                    new HttpLoggingInterceptor();

            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            OkHttpClient okhttp = new OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            HttpUrl originalUrl = originalRequest.url();

                            HttpUrl newUrl = originalUrl.newBuilder()
                                    .addQueryParameter("api_key", BuildConfig.THEMOVIEDB_API_KEY)
                                    .build();

                            Request newRequest = originalRequest.newBuilder()
                                    .url(newUrl).build();

                            return chain.proceed(newRequest);
                        }
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(okhttp)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build();


            moviesApi = retrofit.create(MoviesApi.class);

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
        if (movieOrderType == MovieOrderType.POPULAR ){
            return moviesApi.doGetPopularMovies(page);
        }else {
            return moviesApi.doGetTopRatedMovies(page);
        }
    }

    @Override
    public Single<MovieTrailersReviewResponse> doGetTrailersFromMovieApiCall(int movieId, int page) {
        return moviesApi.doGetTrailersFromMovieApiCall(movieId,page);
    }

    @Override
    public Single<MovieReviewResponse> doGetReviewsFromMovieApiCall(int movieId, int page) {
        return moviesApi.doGetReviewsFromMovieApiCall(movieId,page);
    }
}
