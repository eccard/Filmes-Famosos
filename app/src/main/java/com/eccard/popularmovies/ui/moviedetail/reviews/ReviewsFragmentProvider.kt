package com.eccard.filmesfamosos.ui.moviedetail.reviews

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ReviewsFragmentProvider{
    @ContributesAndroidInjector
    internal abstract fun provideTrailerFragmentFactory(): FrgReviews
}