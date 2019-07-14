package com.eccard.filmesfamosos.ui.moviedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eccard.filmesfamosos.R
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_movie_detail.*
import javax.inject.Inject

class MovieDetailActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        AndroidInjection.inject(this)

        val moviePageAdapter = MoviePageAdapter(supportFragmentManager, this)

        pager.setAdapter(moviePageAdapter)

        tabLayout.setupWithViewPager(pager)

    }

    companion object {

        private val TAG = MovieDetailActivity::class.java.simpleName

        fun newIntent(context: Context): Intent {
            return Intent(context, MovieDetailActivity::class.java)
        }
    }


}