package com.example.eccard.filmesfamosos.ui.moviedetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import java.net.MalformedURLException
import java.net.URL

class FrgTrailers : Fragment(), TrailerAdapter.OnViewClicked {

    private var trailerAdapter: TrailerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frg_trailers, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_trailers)
        trailerAdapter = TrailerAdapter(context, this)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = trailerAdapter
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
                DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider)!!)

        recyclerView.addItemDecoration(dividerItemDecoration)

        val intent = activity!!.intent
        if (intent.hasExtra(MovieResult::class.java.simpleName)) {
            val movieResult = intent.getParcelableExtra<MovieResult>(MovieResult::class.java.simpleName)


            //            getMovieTrailer(movieResult.getId(),1);
        }

        return view
    }


    /*private void getMovieTrailer(final int movieId, int page){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(AppApiHelper.getInstance()
                .doGetTrailersFromMovieApiCall(movieId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieTrailersReviewResponse>() {
                               @Override
                               public void accept(MovieTrailersReviewResponse movieResponse) throws Exception {
                                   Log.d(TAG,movieResponse.toString());
                                   trailerAdapter.setTrailerResults(movieResponse.getResults());
                                   trailerAdapter.notifyDataSetChanged();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG,throwable.getLocalizedMessage());
                            }
                        }
                )
        );
    }*/

    override fun onVideoClicked(videoKey: String) {
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

        fun buildVideoUrl(videoKey: String): URL? {

            val builtUri = buildVideoUri(videoKey)

            var videoUrl: URL? = null

            try {
                videoUrl = URL(builtUri.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            return videoUrl
        }
    }
}