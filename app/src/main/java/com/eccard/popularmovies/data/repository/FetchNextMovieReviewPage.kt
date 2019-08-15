package com.eccard.popularmovies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eccard.popularmovies.data.network.api.*
import com.eccard.popularmovies.data.network.database.AppDatabase
import com.eccard.popularmovies.data.network.model.network.MovieFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieReviewFetchResult
import java.io.IOException

class FetchNextMovieReviewPage constructor(private val movieId : Int,
                                           private val moviesApi: MoviesApi,
                                           private val db: AppDatabase) : Runnable {

    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData


    override fun run() {

        val current = db.movieDao().findSearchMovieReviewResult(movieId)
        if ( current == null){
            _liveData.postValue(null)
            return
        }

        val nextPage = current.next
        if ( nextPage == null){
            _liveData.postValue(Resource.success(false))
            return
        }

        val newValue = try{

            val response = moviesApi.doGetReviewsFromMovieApiCall(movieId,nextPage).execute()

            val apiResponse = ApiResponse.create(response)

            when(apiResponse){

                is ApiSuccessResponse -> {
                    val ids = arrayListOf<String>()
                    ids.addAll(current.reviewIds)

                    ids.addAll(apiResponse.body.results.map {it.id})

                    val merged = MovieReviewFetchResult(movieId,
                            ids,apiResponse.body.total_results,
                            apiResponse.nextPage)
                    db.runInTransaction{
                        db.movieDao().insertMovieReviewFetch(merged)
                        db.movieDao().insertMovieReviews(apiResponse.body.results)
                    }

                    Resource.success(apiResponse.nextPage != null)


                }

                is ApiEmptyResponse -> {
                    Resource.success(false)
                }

                is ApiErrorResponse -> {
                    Resource.error(apiResponse.errorMessage,true)
                }
            }

        } catch (e : IOException){
            Resource.error(e.message!!,true)
        }
        _liveData.postValue(newValue)

    }
}