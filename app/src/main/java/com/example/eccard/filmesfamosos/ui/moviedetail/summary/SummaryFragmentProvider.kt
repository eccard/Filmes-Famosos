package com.example.eccard.filmesfamosos.ui.moviedetail.summary

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class SummaryFragmentProvider{

    @ContributesAndroidInjector
    internal abstract fun provideSummaryFragmentFactory(): FrgSummary
}