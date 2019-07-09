package com.example.eccard.filmesfamosos.ui.main

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eccard.filmesfamosos.R
import com.example.eccard.filmesfamosos.data.network.api.AppApiHelper
import com.example.eccard.filmesfamosos.data.network.database.MovieDao
import com.example.eccard.filmesfamosos.data.network.model.MovieResponse
import com.example.eccard.filmesfamosos.data.network.model.MovieResult
import com.example.eccard.filmesfamosos.utils.EndlessRecyclerViewScrollListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.movie_item_view_holder.view.*
import kotlinx.coroutines.*
import muxi.kotlin.walletfda.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private var moviesDao: MovieDao, private var apiHelper: AppApiHelper):
        BaseViewModel<MainNavigator>(){

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private var moviesData = MutableLiveData<List<MovieResult>>()
    var selected: MutableLiveData<MovieResult>
    private var moviesAdapter: MoviesAdapter
    var showEmpty: ObservableInt
    var loading: ObservableInt

    fun getMovies():MutableLiveData<List<MovieResult>>  = moviesData

    fun getSelectedMovie() = selected

    init {
        moviesAdapter = MoviesAdapter(R.layout.movie_item_view_holder,this)
        showEmpty = ObservableInt(View.GONE)
        loading = ObservableInt(View.GONE)
        selected = MutableLiveData()
    }


    fun onItemClick(index: Int?) {
        selected.value = getMovieAt(index)
    }

    fun getMovieAt(index:Int?):MovieResult?{
        return if (moviesData.value != null &&
                index != null &&
                moviesData.value!!.size > index
        ){
            moviesData.value!![index]
        } else null
    }


    fun setMoviesInAdapter(movies: List<MovieResult>){
        this.moviesAdapter.setMovies(movies)
        this.moviesAdapter.notifyDataSetChanged()
    }

    fun getAdapter(): MoviesAdapter = moviesAdapter







    private var mCurrentMovieOrderType: AppApiHelper.MovieOrderType = AppApiHelper.MovieOrderType.POPULAR


    fun getNotes( ): LiveData<List<MovieResult>>? {
        return moviesData
    }

    fun subscribeToNotesDBChanges( callback: OnSync) {
//        doAsync {

//            moviesData = moviesDao.loadAllMovies()
//            info("Notes received.")
//            callback.notesReceived()

//        }
    }

    public interface OnSync {
        fun notesReceived( )
    }

    fun getFirstPage() {
        getData(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX)
    }

    fun getData(pageIndex: Int) {

            if (mCurrentMovieOrderType === AppApiHelper.MovieOrderType.TOP_BOOKMARK) {

//            if (mCallbacks != null) {

                //                getMoviesViewModel.getMovies().observe(this, new Observer<List<MovieResult>>() {
                //                    @Override
                //                    public void onChanged(@Nullable List<MovieResult> movieResults) {
                //                        mCallbacks.onMoviesResult(movieResults);
                //                    }
                //                });


//            } else {
//                Log.e(TAG, "mCallbacks == nul")
//            }
            } else {

                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch(context = Dispatchers.Main) {

                    loading.set(View.VISIBLE)

                    val response = withContext(context = Dispatchers.IO) {
                        apiHelper.doGetMoviesApiCall(mCurrentMovieOrderType, pageIndex)
                    }


                    if (response.isSuccessful){
                        moviesData.value = (response.body() as MovieResponse).movieResults
                    } else {
                        //                    loading.set(View.INVISIBLE)
                        Log.e("error","response.isSuccessful falseeee")
                    }
                }



//                getNavigator()!!.
//                }
                /*
            removeObservers();

            compositeDisposable.add(AppApiHelper.getInstance()
                    .doGetMoviesApiCall(mCurrentMovieOrderType, pageIndex)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MovieResponse>() {
                                   @Override
                                   public void accept(MovieResponse movieResponse) throws Exception {
                                       Log.d(TAG, movieResponse.toString());

                                       appendMovieResults(movieResponse.getMovieResults());

                                       if (mCallbacks != null) {
                                           mCallbacks.onMoviesResult(retainMovies);
                                       } else {
                                           Log.e(TAG, "mCallbacks == nul");
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Log.e(TAG, throwable.toString());
                                    if (mCallbacks != null) {
                                        mCallbacks.onMovieError(throwable);
                                    } else {
                                        Log.e(TAG, "mCallbacks == nul");
                                    }
                                }
                            }
                    )
            );
        }
    }
        */

            }
    }
}