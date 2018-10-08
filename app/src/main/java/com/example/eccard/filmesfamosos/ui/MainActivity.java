package com.example.eccard.filmesfamosos.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GRID_COLLUMS = 2;

    @BindView(R.id.rv_movies)
    RecyclerView mRecycleView;

    @BindView(R.id.pb_main)
    ProgressBar mPB;

    @BindView(R.id.tv_generic_error)
    TextView mTvGenericError;

    private CompositeDisposable compositeDisposable;
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setUpViews();

        getData();
    }

    private void setUpViews() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, GRID_COLLUMS);

        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter();
        mRecycleView.setAdapter(moviesAdapter);
    }

    private void showMovies(){
        mRecycleView.setVisibility(View.VISIBLE);
        mPB.setVisibility(View.INVISIBLE);
        mTvGenericError.setVisibility(View.INVISIBLE);
    }

    private void showLoadingError(){
        mRecycleView.setVisibility(View.INVISIBLE);
        mPB.setVisibility(View.INVISIBLE);
        mTvGenericError.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == R.id.action_refresh){
            getData();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    void getData(){

        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(AppApiHelper.getInstance()
                .doGetPopularMoviesApiCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                               @Override
                               public void accept(MovieResponse movieResponse) throws Exception {
                                   Log.d(TAG,movieResponse.toString());
                                   showMovies();
                                   moviesAdapter.setMovieResults(movieResponse.getMovieResults());
                                   moviesAdapter.notifyDataSetChanged();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                   Log.e(TAG,throwable.toString());
				   showLoadingError();
                            }
                        }
                )
        );
    }

}
