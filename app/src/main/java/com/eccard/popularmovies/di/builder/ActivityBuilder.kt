package com.eccard.filmesfamosos.di.builder

import com.eccard.filmesfamosos.ui.main.MainActivity
import com.eccard.filmesfamosos.ui.main.MainActivityProvider
import com.eccard.filmesfamosos.ui.moviedetail.MovieDetailActivity
import com.eccard.filmesfamosos.ui.moviedetail.reviews.ReviewsFragmentProvider
import com.eccard.filmesfamosos.ui.moviedetail.summary.SummaryFragmentProvider
import com.eccard.filmesfamosos.ui.moviedetail.trailers.TrailerFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder{

    @ContributesAndroidInjector(modules = [MainActivityProvider::class])
    abstract fun bindMainActivity(): MainActivity


    @ContributesAndroidInjector(modules = [SummaryFragmentProvider::class, TrailerFragmentProvider::class, ReviewsFragmentProvider::class])
    abstract fun bindMovieDetailActivity(): MovieDetailActivity
}