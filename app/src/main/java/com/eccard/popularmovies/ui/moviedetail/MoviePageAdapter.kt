package com.eccard.popularmovies.ui.moviedetail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.eccard.popularmovies.R
import com.eccard.popularmovies.ui.moviedetail.reviews.FrgReviews
import com.eccard.popularmovies.ui.moviedetail.summary.FrgSummary
import com.eccard.popularmovies.ui.moviedetail.trailers.FrgTrailers

internal class MoviePageAdapter(fm: FragmentManager, private val context: Context) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        var frg: Fragment? = null

        when (position) {
            0 -> frg = FrgSummary()
            1 -> frg = FrgTrailers()
            2 -> frg = FrgReviews()
            else -> {
            }
        }

        return frg
    }

    override fun getCount(): Int {
        return TOTAL_OF_FRGS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val charSequence: CharSequence

        when (position) {
            0 -> charSequence = context.getString(R.string.summary)
            1 -> charSequence = context.getString(R.string.videos)
            else -> charSequence = context.getString(R.string.review)
        }

        return charSequence
    }

    companion object {
        private val TOTAL_OF_FRGS = 3
    }
}