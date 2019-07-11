package com.example.eccard.filmesfamosos.di.builder

import com.example.eccard.filmesfamosos.ui.main.MainActivity
import com.example.eccard.filmesfamosos.ui.main.MainActivityProvider
import com.example.eccard.filmesfamosos.ui.moviedetail.MovieDetailActivity
import com.example.eccard.filmesfamosos.ui.moviedetail.summary.SummaryFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder{

    @ContributesAndroidInjector(modules = [MainActivityProvider::class])
    abstract fun bindMainActivity(): MainActivity


    @ContributesAndroidInjector(modules = [SummaryFragmentProvider::class])
    abstract fun bindMovieDetailActivity(): MovieDetailActivity
}