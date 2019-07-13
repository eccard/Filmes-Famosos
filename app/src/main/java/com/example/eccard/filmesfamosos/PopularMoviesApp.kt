package com.example.eccard.filmesfamosos

import android.app.Activity
import android.app.Application
import com.example.eccard.filmesfamosos.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class PopularMoviesApp : Application(), HasActivityInjector {

    @Inject
    lateinit internal var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>


    @Inject
    lateinit internal var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = activityDispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

}