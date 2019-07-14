package com.example.eccard.filmesfamosos.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.database.MovieDao
import com.example.eccard.filmesfamosos.ui.main.MainViewModel
import com.example.eccard.filmesfamosos.ui.moviedetail.reviews.ReviewsViewModel
import com.example.eccard.filmesfamosos.ui.moviedetail.summary.SummaryViewModel
import com.example.eccard.filmesfamosos.ui.moviedetail.trailers.TrailerViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory @Inject constructor(private var moviewDao: MovieDao,
                                                   private var apiHelper: AppApiHelper):
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(moviewDao,apiHelper) as T
        } else if(modelClass.isAssignableFrom(SummaryViewModel::class.java)){
            return SummaryViewModel(moviewDao, apiHelper) as T
        } else if (modelClass.isAssignableFrom(TrailerViewModel::class.java)){
            return TrailerViewModel(apiHelper) as T
        } else if (modelClass.isAssignableFrom(ReviewsViewModel::class.java)){
            return ReviewsViewModel(apiHelper) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}