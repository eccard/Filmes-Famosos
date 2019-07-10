package com.example.eccard.filmesfamosos.ui.main

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.utils.EndlessRecyclerViewScrollListener
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

object CustomViewBindings {
//    @BindingAdapter("setAdapter")
//    @JvmStatic
//    fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
//
//        val posterWidth = recyclerView.context.resources.getDimension(R.dimen.img_view_recycler_view_holder_width)
//        val windowManager = recyclerView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val display = windowManager.defaultDisplay
//        val outMetrics = DisplayMetrics()
//        display.getMetrics(outMetrics)
//        val screenWidth = outMetrics.widthPixels.toFloat()
//        val bestSpanCount = (screenWidth / posterWidth).roundToInt()
//
//        recyclerView.setHasFixedSize(true)
//        val layoutManager = GridLayoutManager(recyclerView.context,bestSpanCount)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = adapter
//
//
//        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
//            public override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
//               adapter.getMoviePage(page)
//            }
//        }
//        recyclerView.addOnScrollListener(scrollListener)
//    }


    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindRecyclerViewAdapter(imageView: ImageView?, imageUrl: String?) {
        if (imageUrl != null) {
            val picasso: Picasso = Picasso.get()
            picasso.setIndicatorsEnabled(true)
            picasso.load(imageUrl)
                    .fit()
                    .centerCrop()
                    .into(imageView)
        } else {
            imageView?.setImageBitmap(null)
        }
    }
}