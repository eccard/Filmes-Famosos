package com.example.eccard.filmesfamosos.ui.moviedetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.database.AppDatabase
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import com.example.eccard.filmesfamosos.utils.AppExecutors
import com.squareup.picasso.Picasso

class FrgSummary : Fragment() {


    private var btnBookMark: ImageView? = null


//    internal var mDb: AppDatabase
    internal var movieIsBookmarked = false

    private var movieResult: MovieResult? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.frg_summary, container, false)

        val imgPoster = view.findViewById<ImageView>(R.id.img_view_detail_activity)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val tvRate = view.findViewById<TextView>(R.id.tv_rate)
        val tvLauchDate = view.findViewById<TextView>(R.id.tv_lauch_date)
        val tvOverView = view.findViewById<TextView>(R.id.tv_over_view)
        btnBookMark = view.findViewById(R.id.btn_bookmark)

//        mDb = AppDatabase.getInstance(activity!!.applicationContext)

        val intent = activity!!.intent

        if (intent.hasExtra(MovieResult::class.java.simpleName)) {
            movieResult = intent.getParcelableExtra(MovieResult::class.java.simpleName)

            activity!!.title = movieResult!!.title

//            checkIfMovieIsAlreadyBookmarked(movieResult!!.id!!)

//            val posterUrl = AppApiHelper.getInstance()
//                    .generatePosterPath(movieResult!!.posterPath)
//
//            Picasso.get().load(posterUrl.toString()).into(imgPoster)
            tvTitle.text = movieResult!!.originalTitle
            tvRate.text = movieResult!!.voteAverage.toString()
            tvLauchDate.text = movieResult!!.releaseDate
            tvOverView.text = movieResult!!.overview


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


/*
    private fun checkIfMovieIsAlreadyBookmarked(movieId: Int) {

        val movie = mDb.movieDao().getMovieResult(movieId)
        movie.observe(this, object : Observer<MovieResult> {
            override fun onChanged(movieResult: MovieResult?) {
                movie.removeObserver(this)

                movieIsBookmarked = movieResult != null
                updateBookmarkedState(movieIsBookmarked)
            }
        })
    }


    private fun updateBookmarkedState(isBookmarked: Boolean) {
        val draw: Drawable?
        if (isBookmarked) {
            draw = ResourcesCompat.getDrawabdialogle(resources,
                    android.R.drawable.btn_star_big_on, null)

        } else {
            draw = ResourcesCompat.getDrawable(resources,
                    android.R.drawable.btn_star_big_off, null)

        }
        activity!!.runOnUiThread { btnBookMark!!.setImageDrawable(draw) }
    }
*/
    companion object {

        val TAG = FrgSummary::class.java.simpleName
    }


}
