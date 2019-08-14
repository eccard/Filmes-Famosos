package com.eccard.popularmovies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eccard.popularmovies.data.network.api.*
import com.eccard.popularmovies.data.network.database.AppDatabase
import java.io.IOException

class FetchNextMoviePage constructor(private val orderType : AppApiHelper.MovieOrderType,
                                     private val moviesApi: MoviesApi,
                                     private val db: AppDatabase) : Runnable {
    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {

        val current = db.movieDao().findSearchMoveiResult(orderType.name)
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

            val response = if (orderType == AppApiHelper.MovieOrderType.POPULAR){
                moviesApi.doGetPopularMovies(nextPage).execute()
            } else {
//         todo fazer apenas para popular e top rated
                moviesApi.doGetTopRatedMovies(nextPage).execute()
            }
            val apiResponse = ApiResponse.create(response)

            when(apiResponse){

                is ApiSuccessResponse -> {
                    val ids = arrayListOf<Int>()
                    ids.addAll(current.movieIds)

                    ids.addAll(apiResponse.body.results.map {it.id})

                    val merged = MovieFetchResult(orderType.name,
                            ids,apiResponse.body.total_results,
                            apiResponse.nextPage)
                    db.runInTransaction{
                        db.movieDao().insert(merged)
                        db.movieDao().insertMovies(apiResponse.body.results)
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