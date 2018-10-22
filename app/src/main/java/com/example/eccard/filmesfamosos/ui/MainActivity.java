package com.example.eccard.filmesfamosos.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.example.eccard.filmesfamosos.ui.moviedetail.MovieDetailActivity;
import com.example.eccard.filmesfamosos.utils.EndlessRecyclerViewScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener, GetMoviesFragment.GetMoviesCallbacks {

    private static final int GRID_COLUMNS_PORTRAIT = 2;
    private static final int GRID_COLUMNS_LANDSCAPE = 3;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.rv_movies)
    RecyclerView mRecycleView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.pb_main)
    ProgressBar mPB;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_generic_error)
    TextView mTvGenericError;


    private MoviesAdapter moviesAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private AppApiHelper.MovieOrderType mCurrentMovieOrderType = AppApiHelper.MovieOrderType.POPULAR;

    private GetMoviesFragment mGetMoviewFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);

        setUpViews();


        FragmentManager fm = getSupportFragmentManager();
        mGetMoviewFrg = (GetMoviesFragment) fm.findFragmentByTag(GetMoviesFragment.TAG);

        if ( mGetMoviewFrg == null){
            mGetMoviewFrg = new GetMoviesFragment();
            fm.beginTransaction().add(mGetMoviewFrg, GetMoviesFragment.TAG).commit();

            getMoviePage(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX);
        } else {
            onMoviesResult(mGetMoviewFrg.getRetainMovies());
        }
    }

    private void getMoviePage(int startingPageIndex) {
        showLoading();
        mGetMoviewFrg.getData(startingPageIndex, mCurrentMovieOrderType);
    }

    // todo check internet conection
    private void setUpViews() {

        GridLayoutManager layoutManager;

        if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(this, GRID_COLUMNS_LANDSCAPE);
        } else {
            layoutManager = new GridLayoutManager(this, GRID_COLUMNS_PORTRAIT);
        }

        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter();
        moviesAdapter.setOnMovieClickListener(this);
        mRecycleView.setAdapter(moviesAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getMoviePage(page);
            }
        };

        mRecycleView.addOnScrollListener(scrollListener);
    }

    private void showMovies(){
        mRecycleView.setVisibility(View.VISIBLE);
        mPB.setVisibility(View.INVISIBLE);
        mTvGenericError.setVisibility(View.INVISIBLE);
    }

    private void showLoading(){
        mRecycleView.setVisibility(View.INVISIBLE);
        mPB.setVisibility(View.VISIBLE);
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
        if ( item.getItemId() == R.id.action_order_by){

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setTitle(getString(R.string.select_order));


            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                    (MainActivity.this,
                            R.array.order_array,
                            android.R.layout.simple_list_item_1);

            builderSingle.setNegativeButton(android.R.string.cancel, new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AppApiHelper.MovieOrderType newOrderType;
                    if ( which == 0 ){
                        newOrderType = AppApiHelper.MovieOrderType.POPULAR;
                    }else {
                        newOrderType = AppApiHelper.MovieOrderType.TOP_RATED;

                    }

                    if (mCurrentMovieOrderType == newOrderType){

                        mRecycleView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecycleView.smoothScrollToPosition(0);
                            }
                        });

                    }else {

                        mCurrentMovieOrderType = newOrderType;

                        moviesAdapter.resetMovieResults();
                        moviesAdapter.notifyDataSetChanged();
                        scrollListener.resetState();

                        getMoviePage(EndlessRecyclerViewScrollListener.STARTING_PAGE_INDEX);

                    }


                }
            });
            builderSingle.show();

            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onMovieItemClick(MovieResult movie) {
        Intent intent = MovieDetailActivity.newIntent(MainActivity.this);
        intent.putExtra(MovieResult.class.getSimpleName(),movie);

        startActivity(intent);
    }

    @Override
    public void onMovieError(Throwable throwable) {
        showLoadingError();
    }

    @Override
    public void onMoviesResult(List<MovieResult> movies) {
        showMovies();
        moviesAdapter.appendMovieResults(movies);
        moviesAdapter.notifyDataSetChanged();

    }
}
