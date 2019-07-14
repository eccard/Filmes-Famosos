package com.eccard.filmesfamosos.ui.moviedetail.trailers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eccard.filmesfamosos.BR
import com.eccard.filmesfamosos.R
import com.eccard.filmesfamosos.data.network.model.MovieResult
import com.eccard.filmesfamosos.databinding.FrgTrailersBinding
import com.eccard.filmesfamosos.di.ViewModelProviderFactory
import muxi.kotlin.walletfda.ui.base.BaseFragment
import javax.inject.Inject

class FrgTrailers : BaseFragment<FrgTrailersBinding,TrailerViewModel>() {

    private lateinit var trailersViewModel: TrailerViewModel

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private lateinit var frgTrailersBinding: FrgTrailersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setupRecyclerView()
        return view
    }


    fun setupRecyclerView(){
        frgTrailersBinding = getViewDataBinding()

        val layoutManager = LinearLayoutManager(context)
        frgTrailersBinding.rvTrailers.layoutManager = layoutManager
        frgTrailersBinding.rvTrailers.adapter = trailersViewModel.getAdapter()
        val dividerItemDecoration = DividerItemDecoration(frgTrailersBinding.rvTrailers.context,
                DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)

        frgTrailersBinding.rvTrailers.addItemDecoration(dividerItemDecoration)



        trailersViewModel.trailers.observe(this, Observer {
            trailersViewModel.updateTralersList(it)
        })

        trailersViewModel.selected.observe(this, Observer {
            onSelectedTrailers(it.peekContent().key)
        })



        val intent = activity!!.intent
        if (intent.hasExtra(MovieResult::class.java.simpleName)) {
            val movieResult = intent.getParcelableExtra<MovieResult>(MovieResult::class.java.simpleName)


            trailersViewModel.movie.value = movieResult
        }


    }

    private fun onSelectedTrailers(videoKey: String) {
        val videoIntent = Intent()
        videoIntent.action = Intent.ACTION_VIEW
        videoIntent.data = buildVideoUri(videoKey)

        if (videoIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(videoIntent)
        }
    }


    companion object {

        private val YOUTUBE_BASE_URL = "https://www.youtube.com/watch"
        private fun buildVideoUri(videoKey: String): Uri {
            return Uri.parse(YOUTUBE_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("v", videoKey)
                    .build()
        }
    }


    override fun getLayoutId() = R.layout.frg_trailers

    override fun getViewModel(): TrailerViewModel {
        trailersViewModel = ViewModelProviders.of(this,factory)
                .get(TrailerViewModel::class.java)
        return trailersViewModel
    }

    override fun getBindingVariable() = BR.viewModel

    override fun showToast(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}