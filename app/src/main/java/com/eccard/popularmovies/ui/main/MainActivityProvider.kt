package com.eccard.popularmovies.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityProvider{

    @ContributesAndroidInjector
    abstract fun provideMainViewFactory() : MainActivity
}