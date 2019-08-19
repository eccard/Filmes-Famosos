package com.eccard.popularmovies.data.repository

import com.eccard.popularmovies.data.network.api.MoviesApi
import com.eccard.popularmovies.data.network.database.AppDatabase
import com.eccard.popularmovies.data.network.model.network.MovieReviewFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieReviewResponse
import retrofit2.Call

class FetchNextMovieReviewPage constructor(private val movieId : Int,
                                           private val moviesApi: MoviesApi,
                                           private val db: AppDatabase) :
        BaseFetchNextPage<MovieReviewFetchResult, MovieReviewResponse>() {

    override fun findFetchInDb(): MovieReviewFetchResult? {
        return db.movieDao().findSearchMovieReviewResult(movieId)
    }

    override fun getNextPage(): Int? {
        if ( mFetch != null) {
            mFetch?.next?.let {
                return it
            }
        }
        return null
    }

    override fun createApi(): Call<MovieReviewResponse>? {
        getNextPage()?.let {
            return moviesApi.doGetReviewsFromMovieApiCall(movieId,it)
        }

        return null

    }

    override fun onSuccessApiRequest(netResult: MovieReviewResponse, apiNextPage : Int?) {
        val ids = arrayListOf<String>()
        ids.addAll(mFetch!!.reviewIds)

        ids.addAll(netResult.results.map {it.id})

        val merged = MovieReviewFetchResult(movieId,
                ids,netResult.total_results,
                apiNextPage)
        db.runInTransaction{
            db.movieDao().insertMovieReviewFetch(merged)
            db.movieDao().insertMovieReviews(netResult.results)
        }

    }

}