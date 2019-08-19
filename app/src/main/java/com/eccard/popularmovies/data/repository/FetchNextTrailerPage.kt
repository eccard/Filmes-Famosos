package com.eccard.popularmovies.data.repository

import com.eccard.popularmovies.data.network.api.MoviesApi
import com.eccard.popularmovies.data.network.database.AppDatabase
import com.eccard.popularmovies.data.network.model.network.MovieTrailerFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieTrailersResponse
import retrofit2.Call

class FetchNextTrailerPage constructor(private val movieId : Int,
                                       private val moviesApi: MoviesApi,
                                       private val db: AppDatabase) :
    BaseFetchNextPage<MovieTrailerFetchResult, MovieTrailersResponse>() {

    override fun findFetchInDb(): MovieTrailerFetchResult? {
        return db.movieDao().findSearchMovieTrailerFetched(movieId)
    }

    override fun getNextPage(): Int? {
        if ( mFetch != null) {
            mFetch?.next?.let {
                return it
            }
        }
        return null
    }

    override fun createApi(): Call<MovieTrailersResponse>? {
        getNextPage()?.let {
            return moviesApi.doGetTrailersFromMovieApiCall(movieId,it)
        }

        return null
    }

    override fun onSuccessApiRequest(netResult: MovieTrailersResponse, apiNextPage: Int?) {
        val ids = arrayListOf<String>()
        ids.addAll(mFetch!!.trailerIds)

        ids.addAll(netResult.results.map {it.id})

        val merged = MovieTrailerFetchResult(movieId,
                ids,netResult.total_results,
                apiNextPage)
        db.runInTransaction{
            db.movieDao().insertMovieTrailerFetch(merged)
            db.movieDao().insertMovieTrailer(netResult.results)
        }
    }
}