package com.eccard.popularmovies.di.builder

import com.eccard.popularmovies.ui.main.MainActivity
import com.eccard.popularmovies.ui.main.MainActivityProvider
import com.eccard.popularmovies.ui.moviedetail.MovieDetailActivity
import com.eccard.popularmovies.ui.moviedetail.reviews.ReviewsFragmentProvider
import com.eccard.popularmovies.ui.moviedetail.summary.SummaryFragmentProvider
import com.eccard.popularmovies.ui.moviedetail.trailers.TrailerFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder{

    @ContributesAndroidInjector(modules = [MainActivityProvider::class])
    abstract fun bindMainActivity(): MainActivity


    @ContributesAndroidInjector(modules = [SummaryFragmentProvider::class, TrailerFragmentProvider::class, ReviewsFragmentProvider::class])
    abstract fun bindMovieDetailActivity(): MovieDetailActivity
}