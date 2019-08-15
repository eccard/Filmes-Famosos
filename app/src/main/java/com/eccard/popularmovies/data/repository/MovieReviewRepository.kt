package com.eccard.popularmovies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.eccard.popularmovies.AppExecutors
import com.eccard.popularmovies.data.network.api.ApiResponse
import com.eccard.popularmovies.data.network.api.ApiSuccessResponse
import com.eccard.popularmovies.data.network.api.MoviesApi
import com.eccard.popularmovies.data.network.database.AppDatabase
import com.eccard.popularmovies.data.network.model.MovieReviewResult
import com.eccard.popularmovies.data.network.model.network.MovieReviewFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieReviewResponse
import com.eccard.popularmovies.utils.AbsentLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieReviewRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db : AppDatabase,
        private val movieApi : MoviesApi){

    fun fetchNextPage(movieId : Int)
            : LiveData<Resource<Boolean>> {
        val fetchNextMoviePage = FetchNextMovieReviewPage(
                movieId,
                movieApi,
                db
        )

        appExecutors.networkIO().execute(fetchNextMoviePage)

        return fetchNextMoviePage.liveData
    }


    fun fetchMovieReviews(movieId : Int)
            : LiveData<Resource<List<MovieReviewResult>>> {

        return object : NetworkBoundResource<List<MovieReviewResult>, MovieReviewResponse>(appExecutors){
            override fun saveCallResult(item: MovieReviewResponse) {
                var movieReviewIds = item.results.map {it.id}
                val movieReviewFetchResult = MovieReviewFetchResult(movieId,
                        movieReviewIds,
                        item.total_results,
                        item.nextPage)

                db.runInTransaction {
                    db.movieDao().insertMovieReviews(item.results)
                    db.movieDao().insertMovieReviewFetch(movieReviewFetchResult)
                }

            }

            override fun shouldFetch(data: List<MovieReviewResult>?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<List<MovieReviewResult>> {

                return Transformations.switchMap(db.movieDao().searchMovieReviewResult(movieId)){
                    fetchData ->
                    if (fetchData == null) {
                        AbsentLiveData.create()
                    } else {
                        db.movieDao().loadMovieReviews(fetchData.reviewIds)
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<MovieReviewResponse>> {
                return movieApi.doGetReviewsFromMovieApiCall(movieId)
            }

            override fun processResponse(response: ApiSuccessResponse<MovieReviewResponse>): MovieReviewResponse {
                val body = response.body
//                body.nextPage = response.body.nextPage
                body.nextPage = response.nextPage
                return body
            }

        }.asLiveData()
    }
}