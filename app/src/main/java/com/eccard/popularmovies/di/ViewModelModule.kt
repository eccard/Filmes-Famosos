package com.eccard.popularmovies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eccard.popularmovies.ui.main.MainViewModel
import com.eccard.popularmovies.ui.moviedetail.reviews.ReviewsViewModel
import com.eccard.popularmovies.ui.moviedetail.summary.SummaryViewModel
import com.eccard.popularmovies.ui.moviedetail.trailers.TrailerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel) : ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SummaryViewModel::class)
    abstract fun bindSummaryViewModel(summaryViewModel: SummaryViewModel) : ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TrailerViewModel::class)
    abstract fun bindTrailerViewModel(trailerViewModel: TrailerViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewsViewModel::class)
    abstract fun bindReviewViewModel(reviewsViewModel: ReviewsViewModel) : ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: PopularMovieViewModelFactory): ViewModelProvider.Factory
}