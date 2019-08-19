package com.eccard.popularmovies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.eccard.popularmovies.utils.AppExecutors
import com.eccard.popularmovies.data.network.api.ApiResponse
import com.eccard.popularmovies.data.network.api.ApiSuccessResponse
import com.eccard.popularmovies.data.network.api.MoviesApi
import com.eccard.popularmovies.data.network.database.AppDatabase
import com.eccard.popularmovies.data.network.model.MovieTrailer
import com.eccard.popularmovies.data.network.model.network.MovieTrailerFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieTrailersResponse
import com.eccard.popularmovies.utils.AbsentLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieTrailerRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db : AppDatabase,
        private val movieApi : MoviesApi){

    fun fetchNextPage(movieId : Int)
            : LiveData<Resource<Boolean>> {
        val fetchNextTrailerPage = FetchNextTrailerPage(
                movieId,
                movieApi,db
        )

        appExecutors.networkIO().execute(fetchNextTrailerPage)

        return fetchNextTrailerPage.liveData
    }

    fun fetchMovieTrailer(movieId : Int)
            :LiveData<Resource<List<MovieTrailer>>> {

        return object : NetworkBoundResource<List<MovieTrailer>, MovieTrailersResponse>(appExecutors){
            override fun saveCallResult(item: MovieTrailersResponse) {
                val movieTrailerIds = item.results.map {it.id}
                val movieTrailerFetchResult = MovieTrailerFetchResult(movieId,
                        movieTrailerIds,item.total_results,item.nextPage)

                db.runInTransaction {
                    db.movieDao().insertMovieTrailer(item.results)
                    db.movieDao().insertMovieTrailerFetch(movieTrailerFetchResult)
                }
            }

            override fun shouldFetch(data: List<MovieTrailer>?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<List<MovieTrailer>> {
                return Transformations.switchMap(
                        db.movieDao().searchMovieTrailerFetchResult(movieId)){
                    fetchData ->
                    if (fetchData == null){
                        AbsentLiveData.create()
                    } else {
                        db.movieDao().loadMovieTrailers(fetchData.trailerIds)
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<MovieTrailersResponse>> {
                return movieApi.doGetTrailersFromMovieApiCall(movieId)
            }

            override fun processResponse(response: ApiSuccessResponse<MovieTrailersResponse>): MovieTrailersResponse {
                val body = response.body
                body.nextPage = response.nextPage
                return body
            }
        }.asLiveData()
    }
}