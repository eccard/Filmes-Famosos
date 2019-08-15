package com.eccard.popularmovies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eccard.popularmovies.data.network.api.AppApiHelper
import com.eccard.popularmovies.data.network.database.MovieDao
import com.eccard.popularmovies.data.repository.MovieRepository
import com.eccard.popularmovies.data.repository.MovieReviewRepository
import com.eccard.popularmovies.ui.main.MainViewModel
import com.eccard.popularmovies.ui.moviedetail.reviews.ReviewsViewModel
import com.eccard.popularmovies.ui.moviedetail.summary.SummaryViewModel
import com.eccard.popularmovies.ui.moviedetail.trailers.TrailerViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory @Inject constructor(private var moviewDao: MovieDao,
                                                   private var apiHelper: AppApiHelper,
                                                   private var movieRepository: MovieRepository,
                                                   private var movieReviewRepository: MovieReviewRepository):
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(movieRepository) as T
        } else if(modelClass.isAssignableFrom(SummaryViewModel::class.java)){
            return SummaryViewModel(moviewDao, apiHelper) as T
        } else if (modelClass.isAssignableFrom(TrailerViewModel::class.java)){
            return TrailerViewModel(apiHelper) as T
        } else if (modelClass.isAssignableFrom(ReviewsViewModel::class.java)){
            return ReviewsViewModel(movieReviewRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}