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

//    private var btnBookMark: ImageView? = null


//    internal var mDb: AppDatabase
    internal var movieIsBookmarked = false

    private var movieResult: MovieResult? = null


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        summaryViewModel.setNavigator(this)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        frgSummaryBinding = getViewDataBinding()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        /*
        val view = inflater.inflate(R.layout.frg_summary, container, false)

        val imgPoster = view.findViewById<ImageView>(R.id.img_view_detail_activity)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val tvRate = view.findViewById<TextView>(R.id.tv_rate)
        val tvLauchDate = view.findViewById<TextView>(R.id.tv_lauch_date)
        val tvOverView = view.findViewById<TextView>(R.id.tv_over_view)
        btnBookMark = view.findViewById(R.id.btn_bookmark)

//        mDb = AppDatabase.getInstance(activity!!.applicationContext)
*/
        val intent = activity!!.intent

        if (intent.hasExtra(MovieResult::class.java.simpleName)) {
            movieResult = intent.getParcelableExtra(MovieResult::class.java.simpleName)

            activity!!.title = movieResult!!.title

            summaryViewModel.movie.value = movieResult

            checkIfMovieIsAlreadyBookmarked(movieResult!!.id!!)

//            val posterUrl = AppApiHelper.getInstance()
//                    .generatePosterPath(movieResult!!.posterPath)
//
//            Picasso.get().load(posterUrl.toString()).into(imgPoster)
            /*
            tvTitle.text = movieResult!!.originalTitle
            tvRate.text = movieResult!!.voteAverage.toString()
            tvLauchDate.text = movieResult!!.releaseDate
            tvOverView.text = movieResult!!.overview
            */

            summaryViewModel.movieIsBookmarked.observe(this, Observer {
                updateBookmarkedState(it)
            })
        }

        /*btnBookMark!!.setOnClickListener {
            if (movieResult != null) {

                AppExecutors.getInstance().diskIO().execute {
                    if (!movieIsBookmarked) {
                        mDb.movieDao().insertMovie(movieResult!!)
                        movieIsBookmarked = true
                    } else {
                        mDb.movieDao().deleteMovie(movieResult!!)
                        movieIsBookmarked = false

                    }
                    updateBookmarkedState(movieIsBookmarked)
                }

            } else {
                Log.d(TAG, "btnbookMark onClick movieResult == null")
            }
        }*/
        return view
    }


    private fun checkIfMovieIsAlreadyBookmarked(movieId: Int) {

        val movieData = summaryViewModel.getMovieFromDb(movieId)

        movieData.observe(this, object : Observer<MovieResult> {
                override fun onChanged(movieResult: MovieResult?) {
                    movieData.removeObserver(this)
                    movieIsBookmarked = movieResult != null

                    summaryViewModel.movieIsBookmarked.value = movieIsBookmarked

//                    updateBookmarkedState(movieIsBookmarked)
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
//        activity!!.runOnUiThread { btnBookMark!!.setImageDrawable(draw) }
        frgSummaryBinding.root.btn_bookmark.setImageDrawable(draw)
//        activity!!.runOnUiThread { btnBookMark!!.setImageDrawable(draw) }
    }
    companion object {

        val TAG = FrgSummary::class.java.simpleName
    }


}