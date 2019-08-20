package com.eccard.popularmovies.ui.moviedetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.Movie
import com.eccard.popularmovies.databinding.ActivityMovieDetailBinding
import com.eccard.popularmovies.ui.moviedetail.reviews.FrgReviews
import com.eccard.popularmovies.ui.moviedetail.summary.FrgSummary
import com.eccard.popularmovies.ui.moviedetail.trailers.FrgTrailers
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.lang.Exception
import javax.inject.Inject



class MovieDetailActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var mViewDataBinding: ActivityMovieDetailBinding

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)


        if (intent.hasExtra(Movie::class.java.simpleName)) {
            movie = intent.getParcelableExtra(Movie::class.java.simpleName)
            mViewDataBinding.movie = movie

            val url = if (movie?.backdrop_path == null)  movie?.generatePosterUrl() else movie?.getBackdropPath()

            val picasso: Picasso = Picasso.get()
            picasso.setIndicatorsEnabled(true)
            picasso.load(url)
                    .fit()
                    .centerCrop()
                    .into(mViewDataBinding.movieImgToolbal,object : Callback {
                        override fun onSuccess() {
                            circularRevealedAtCenter(mViewDataBinding.movieImgToolbal)
                        }

                        override fun onError(e: Exception?) {

                        }
                    })





            mViewDataBinding.executePendingBindings()
        }

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frg_summary,FrgSummary())
        ft.add(R.id.frg_trailers,FrgTrailers())
        ft.add(R.id.frg_reviews,FrgReviews())
        ft.commit()


        applyToolbarMargin(mViewDataBinding.movieDetailToolbar)

        setSupportActionBar(mViewDataBinding.movieDetailToolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator()
            title = movie?.title
        }



    }


    fun circularRevealedAtCenter(view: View) {
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) / 2
        val finalRadius = Math.max(view.width, view.height)

        if (checkIsMaterialVersion() && view.isAttachedToWindow) {
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
            view.visibility = View.VISIBLE
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_800))
            anim.duration = 550
            anim.start()
        }
    }

    fun checkIsMaterialVersion() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    private fun AppCompatActivity.getStatusBarSize(): Int {
        val idStatusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (idStatusBarHeight > 0) {
            resources.getDimensionPixelSize(idStatusBarHeight)
        } else {
            0
        }
    }

    fun applyToolbarMargin(toolbar: Toolbar) {
        if (checkIsMaterialVersion()) {
            toolbar.layoutParams = (toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams).apply {
                topMargin = getStatusBarSize()
            }
        }
    }
    companion object {

        private val TAG = MovieDetailActivity::class.java.simpleName

        fun newIntent(context: Context): Intent {
            return Intent(context, MovieDetailActivity::class.java)
        }
    }


}