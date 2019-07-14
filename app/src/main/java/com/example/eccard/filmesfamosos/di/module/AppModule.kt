package com.example.eccard.filmesfamosos.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.eccard.filmesfamosos.AppConstants
import com.example.eccard.filmesfamosos.BuildConfig
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.api.MoviesApi
import com.example.eccard.filmesfamosos.data.network.database.AppDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class AppModule {
    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, AppConstants.APP_DB_NAME).build()

    @Provides
    @Singleton
    internal fun provideMovieDao(appDatabase: AppDatabase) = appDatabase.movieDao()



    @Provides
    @Singleton
    internal fun provideApiHelper(retrofit: Retrofit): AppApiHelper {
        return AppApiHelper(retrofit.create(MoviesApi::class.java))
    }


    @Provides
    @Singleton
    internal fun providePostApi(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        var asdf = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val originalUrl = originalRequest.url

                // Request customization: add request headers
                val newUrl = originalUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.THEMOVIEDB_API_KEY).build()


                val newRequest = originalRequest.newBuilder()
                        .url(newUrl).build()
                return chain.proceed(newRequest)
            }
        }


        val okhttp = OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(asdf)
//                .addInterceptor { chain ->
//                    val originalRequest = chain.request()
//                    val originalUrl = originalRequest.url
//
//                    val newUrl = originalUrl.newBuilder()
//                            .addQueryParameter(Constants.API_KEY, BuildConfig.THEMOVIEDB_API_KEY)
//                            .build()
//
//                    val newRequest = originalRequest.newBuilder()
//                            .url(newUrl).build()
//
//                    chain.proceed(newRequest)
//                }
                .addInterceptor(loggingInterceptor)
                .build()

        return Retrofit.Builder()
//                .baseUrl(AppConstants.ENDPOINT_MOVIES_POSTER_BASE_URL)
                .baseUrl(BuildConfig.BASE_URL)
//                .addConverterFactory(MoshiConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .client(okhttp)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

}