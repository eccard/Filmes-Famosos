package com.example.eccard.filmesfamosos.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityProvider{

    @ContributesAndroidInjector
    abstract fun provideMainViewFactory() : MainActivity
}