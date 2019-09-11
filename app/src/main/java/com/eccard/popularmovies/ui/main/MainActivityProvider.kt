package com.eccard.popularmovies.ui.main

import com.eccard.popularmovies.ui.moviedetail.reviews.FrgReviews
import com.eccard.popularmovies.ui.moviedetail.summary.FrgSummary
import com.eccard.popularmovies.ui.moviedetail.trailers.FrgTrailers
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityProvider{

    @ContributesAndroidInjector
    abstract fun contributeFrgSummary(): FrgSummary

    @ContributesAndroidInjector
    abstract fun contributeFrgTrailers(): FrgTrailers

    @ContributesAndroidInjector
    abstract fun contributeFrgReviews(): FrgReviews

}