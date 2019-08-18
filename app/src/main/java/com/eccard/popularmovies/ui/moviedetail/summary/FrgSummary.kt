package com.eccard.popularmovies.ui.moviedetail.summary

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.eccard.popularmovies.BR
import com.eccard.popularmovies.R
import com.eccard.popularmovies.data.network.model.Movie
import com.eccard.popularmovies.databinding.FrgSummaryBinding
import com.eccard.popularmovies.di.ViewModelProviderFactory
import kotlinx.android.synthetic.main.movie_details.view.*
import com.eccard.popularmovies.ui.base.BaseFragment
import javax.inject.Inject

class FrgSummary : BaseFragment<FrgSummaryBinding,SummaryViewModel>() {

    private lateinit var summaryViewModel: SummaryViewModel

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var frgSummaryBinding :FrgSummaryBinding

    override fun getLayoutId() = R.layout.frg_summary

    override fun getViewModel(): SummaryViewModel {
        summaryViewModel = ViewModelProviders.of(this,factory)
                .get(SummaryViewModel::class.java)
        return summaryViewModel
    }

    override fun getBindingVariable() : Int = BR.viewModel

    internal var movieIsBookmarked = false

    private var movie: Movie? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        frgSummaryBinding = getViewDataBinding()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val intent = activity!!.intent

        if (intent.hasExtra(Movie::class.java.simpleName)) {
            movie = intent.getParcelableExtra(Movie::class.java.simpleName)

            activity!!.title = movie!!.title

            summaryViewModel.movie.value = movie

            checkIfMovieIsAlreadyBookmarked(movie!!.id)

            summaryViewModel.movieIsBookmarked.observe(this, Observer {
                updateBookmarkedState(it)
            })
        }
        return view
    }


    private fun checkIfMovieIsAlreadyBookmarked(movieId: Int) {

        val movieData = summaryViewModel.getMovieFromDb(movieId)

        movieData.observe(this, object : Observer<Movie> {
                override fun onChanged(movie: Movie?) {
                    movieData.removeObserver(this)
                    movieIsBookmarked = false
                    movie?.bookmarked?.let {
                        if (it){
                            movieIsBookmarked = true
                        }
                    }

                    summaryViewModel.movieIsBookmarked.value = movieIsBookmarked

            }
        })
    }


    private fun updateBookmarkedState(isBookmarked: Boolean) {
        val draw: Drawable?
        if (isBookmarked) {
            draw = ResourcesCompat.getDrawable(resources,
                    android.R.drawable.btn_star_big_on, null)

        } else {
            draw = ResourcesCompat.getDrawable(resources,
                    android.R.drawable.btn_star_big_off, null)

        }
        frgSummaryBinding.root.btn_bookmark.setImageDrawable(draw)
    }
    companion object {

        val TAG = FrgSummary::class.java.simpleName
    }


}
