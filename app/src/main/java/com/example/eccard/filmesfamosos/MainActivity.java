package com.example.eccard.filmesfamosos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eccard.filmesfamosos.data.network.ApiHelper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }

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

        compositeDisposable.add(AppApiHelper.getInstance()
                .doGetPopularMoviesApiCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                               @Override
                               public void accept(MovieResponse movieResponse) throws Exception {
                                   Log.d(TAG,movieResponse.toString());
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                   Log.e(TAG,throwable.toString());

                            }
                        }
                )
        );
    }

}
