package com.example.eccard.filmesfamosos.ui.moviedetail.summary

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.eccard.filmesfamosos.BR
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import com.example.eccard.filmesfamosos.databinding.FrgSummaryBinding
import com.example.eccard.filmesfamosos.di.ViewModelProviderFactory
import com.example.eccard.filmesfamosos.ui.main.MainViewModel
import kotlinx.android.synthetic.main.activivty_movie_details_cardview.view.*
import muxi.kotlin.walletfda.ui.base.BaseFragment
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

    override fun showToast(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    internal var movieIsBookmarked = false

    private var movieResult: MovieResult? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        frgSummaryBinding = getViewDataBinding()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val intent = activity!!.intent

        if (intent.hasExtra(MovieResult::class.java.simpleName)) {
            movieResult = intent.getParcelableExtra(MovieResult::class.java.simpleName)

            activity!!.title = movieResult!!.title

            summaryViewModel.movie.value = movieResult

            checkIfMovieIsAlreadyBookmarked(movieResult!!.id!!)

            summaryViewModel.movieIsBookmarked.observe(this, Observer {
                updateBookmarkedState(it)
            })
        }
        return view
    }


    private fun checkIfMovieIsAlreadyBookmarked(movieId: Int) {

        val movieData = summaryViewModel.getMovieFromDb(movieId)

        movieData.observe(this, object : Observer<MovieResult> {
                override fun onChanged(movieResult: MovieResult?) {
                    movieData.removeObserver(this)
                    movieIsBookmarked = movieResult != null

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
