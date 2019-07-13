package com.example.eccard.filmesfamosos.ui.moviedetail.trailers

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class TrailerFragmentProvider{
    @ContributesAndroidInjector
    internal abstract fun provideTrailerFragmentFactory(): FrgTrailers

}