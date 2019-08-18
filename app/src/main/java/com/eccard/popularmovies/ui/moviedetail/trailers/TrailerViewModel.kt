package com.eccard.popularmovies.ui.moviedetail.trailers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.eccard.popularmovies.data.network.model.MovieTrailer
import com.eccard.popularmovies.data.repository.MovieTrailerRepository
import com.eccard.popularmovies.data.repository.Resource
import com.eccard.popularmovies.data.repository.Status
import com.eccard.popularmovies.ui.base.BaseViewModel
import javax.inject.Inject

class TrailerViewModel @Inject constructor(private var movieTrailerRepository: MovieTrailerRepository):
    BaseViewModel<Any>(){

    companion object {
        val TAG = TrailerViewModel::class.java.simpleName
    }

    private val nextPageHandler = NextPageHandler(movieTrailerRepository)
    private val _movieId = MutableLiveData<Int>()

    fun setMovieId(movieId : Int){
        _movieId.value = movieId
    }

    val results: LiveData<Resource<List<MovieTrailer>>> = Transformations
            .switchMap(_movieId) { fetched ->
                movieTrailerRepository.fetchMovieTrailer(fetched)
            }

    fun loadNextPage() {
        _movieId.value?.let { nextPageHandler.queryNextPage(it) }
    }

    fun refresh() {
        _movieId.value?.let {
            _movieId.value = it
        }
    }

    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }

    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState


    class NextPageHandler(private val repository: MovieTrailerRepository) : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var movieId: Int? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage( movieId: Int) {
            if (this.movieId == movieId) {
                return
            }
            unregister()
            this.movieId = movieId
            nextPageLiveData = repository.fetchNextPage(movieId)
            loadMoreState.value = LoadMoreState(
                    isRunning = true,
                    errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = null
                                )
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = result.message
                                )
                        )
                    }
                    Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                movieId = null
            }
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                    isRunning = false,
                    errorMessage = null
            )
        }
    }

}