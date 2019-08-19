package com.eccard.popularmovies.data.repository

import com.eccard.popularmovies.data.network.api.MoviesApi
import com.eccard.popularmovies.data.network.database.AppDatabase
import com.eccard.popularmovies.data.network.model.MovieOrderType
import com.eccard.popularmovies.data.network.model.network.MovieFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieResponse
import retrofit2.Call

class FetchNextMoviePage constructor(private val orderType : MovieOrderType,
                                     private val moviesApi: MoviesApi,
                                     private val db: AppDatabase) :
        BaseFetchNextPage<MovieFetchResult, MovieResponse>() {

    override fun findFetchInDb(): MovieFetchResult? {
        return db.movieDao().findSearchMoveiResult(orderType.name)
    }

    override fun getNextPage(): Int? {
        if ( mFetch != null) {
            mFetch?.next?.let {
                return it
            }
        }
        return null
    }

    override fun createApi(): Call<MovieResponse>? {
        getNextPage()?.let {
            return if (orderType == MovieOrderType.POPULAR){
                moviesApi.doGetPopularMovies(it)
            } else {
                moviesApi.doGetTopRatedMovies(it)
            }
        }

        return null
    }

    override fun onSuccessApiRequest(netResult: MovieResponse, apiNextPage: Int?) {
        val ids = arrayListOf<Int>()
        ids.addAll(mFetch!!.movieIds)

        ids.addAll(netResult.results.map {it.id})

        val merged = MovieFetchResult(orderType.name,
                ids,netResult.total_results,
                apiNextPage)
        db.runInTransaction{
            db.movieDao().insert(merged)
            db.movieDao().insertMovies(netResult.results)
        }

    }
}