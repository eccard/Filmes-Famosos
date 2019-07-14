package com.example.eccard.filmesfamosos.ui.moviedetail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.ui.moviedetail.reviews.FrgReviews
import com.example.eccard.filmesfamosos.ui.moviedetail.summary.FrgSummary
import com.example.eccard.filmesfamosos.ui.moviedetail.trailers.FrgTrailers

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