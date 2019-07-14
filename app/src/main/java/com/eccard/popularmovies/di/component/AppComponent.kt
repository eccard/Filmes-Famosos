package com.eccard.popularmovies.di.component

import android.app.Application
import com.eccard.popularmovies.PopularMoviesApp
import com.eccard.popularmovies.di.builder.ActivityBuilder
import com.eccard.popularmovies.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class),(ActivityBuilder::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build() : AppComponent
    }

    fun inject(app: PopularMoviesApp)
}