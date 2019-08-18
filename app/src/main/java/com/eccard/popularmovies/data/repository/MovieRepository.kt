package com.eccard.popularmovies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.eccard.popularmovies.utils.AppExecutors
import com.eccard.popularmovies.data.network.api.ApiResponse
import com.eccard.popularmovies.data.network.api.ApiSuccessResponse
import com.eccard.popularmovies.data.network.api.MoviesApi
import com.eccard.popularmovies.data.network.database.AppDatabase
import com.eccard.popularmovies.data.network.model.MovieOrderType
import com.eccard.popularmovies.data.network.model.MovieResult
import com.eccard.popularmovies.data.network.model.network.MovieFetchResult
import com.eccard.popularmovies.data.network.model.network.MovieResponse
import com.eccard.popularmovies.utils.AbsentLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db : AppDatabase,
        private val moviApi : MoviesApi){

    fun fetchNextPage(orderType : MovieOrderType)
    : LiveData<Resource<Boolean>>{
        val fetchNextMoviePage = FetchNextMoviePage(
                orderType,
                moviApi,
                db
        )

        appExecutors.networkIO().execute(fetchNextMoviePage)

        return fetchNextMoviePage.liveData
    }


    fun fetchMovies(orderType : MovieOrderType)
            : LiveData<Resource<List<MovieResult>>> {

        return object : NetworkBoundResource<List<MovieResult>, MovieResponse>(appExecutors){
            override fun saveCallResult(item: MovieResponse) {
                var movieIds = item.results.map {it.id}

                val movieFetchResult = MovieFetchResult(
                        orderType.name,
                        movieIds,
                        item.total_results,
                        item.nextPage
                )

                db.runInTransaction {
                    db.movieDao().insertMovies(item.results)
                    db.movieDao().insert(movieFetchResult)
                }

            }

            override fun shouldFetch(data: List<MovieResult>?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<List<MovieResult>> {

                return if (orderType == MovieOrderType.TOP_BOOKMARK) {
                    db.movieDao().loadAllMoviesWithBookmarked(true)
                } else {

                    Transformations.switchMap(db.movieDao().searchMovieResult(orderType.name))
                    { fetchData ->
                        if (fetchData == null) {
                            AbsentLiveData.create()
                        } else {
                            db.movieDao().loadOrdered(fetchData.movieIds)
                        }
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<MovieResponse>> {
                return if ( orderType == MovieOrderType.POPULAR){
                    moviApi.doGetPopularMoviesFirstPage(1)
                } else {
                    moviApi.doGetTopRatedMoviesFirstPage(1)
                }
            }

            override fun processResponse(response: ApiSuccessResponse<MovieResponse>): MovieResponse {
                val body = response.body
//                body.nextPage = response.body.nextPage
                body.nextPage = response.nextPage
                return body
            }
        }.asLiveData()

    }
}