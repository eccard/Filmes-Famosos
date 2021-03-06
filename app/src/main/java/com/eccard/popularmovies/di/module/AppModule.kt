package com.eccard.popularmovies.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.eccard.popularmovies.AppConstants
import com.eccard.popularmovies.BuildConfig
import com.eccard.popularmovies.data.network.api.MoviesApi
import com.eccard.popularmovies.data.network.database.AppDatabase
import com.eccard.popularmovies.di.ViewModelModule
import com.eccard.popularmovies.utils.LiveDataCallAdapterFactory
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


@Module(includes = [ViewModelModule::class])
object AppModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, AppConstants.APP_DB_NAME).build()

    @JvmStatic
    @Provides
    @Singleton
    fun provideMovieDao(appDatabase: AppDatabase) = appDatabase.movieDao()

    @JvmStatic
    @Provides
    @Singleton
    fun provideMovies(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }


    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofitInterface(): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val headerIntercept = object : Interceptor {
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
                .addInterceptor(headerIntercept)
                .addInterceptor(loggingInterceptor)
                .build()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(okhttp)
                .build()
    }

}