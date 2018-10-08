package com.example.eccard.filmesfamosos.ui;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ItemViewHolder>{

    List<MovieResult> movieResults;

    public void setMovieResults(List<MovieResult> movieResults) {
        this.movieResults = movieResults;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater =  LayoutInflater.from(viewGroup.getContext());

        View view = layoutInflater.inflate(R.layout.movie_item_view_holder,viewGroup,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        MovieResult movie = movieResults.get(i);

        itemViewHolder.bind(movie.getPosterPath(),
                movie.getOriginalTitle());
    }

    @Override
    public int getItemCount() {
        int size;
        if (movieResults != null){
            size = movieResults.size();
        }else {
            size = 0;
        }

        return size;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_movie_item)
        ImageView imgMovie;

        @BindView(R.id.tv_movie_item)
        TextView tvDescription;

        Context context;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context = itemView.getContext();
        }

        void bind(String url,String description){
            URL posterUrl = AppApiHelper.getInstance().generatePosterPath(url);

//            Picasso.get().load(posterUrl.toString()).into(imgMovie);
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e("Picasso",exception.getMessage());
                }
            });

            Picasso picasso = builder.build();
            String stringUrl = posterUrl.toString();

            Log.e("bind","valor da string-" + stringUrl);

            picasso.load(posterUrl.toString()).into(imgMovie,
                    new Callback() {
                        @Override
                        public void onSuccess() {
                             Log.e("into Picasso","sucess");

                        }

                        @Override
                        public void onError(Exception e) {
                             Log.e("into Picasso",e.getMessage());

                        }
                    });

            tvDescription.setText(description);
        }
    }
}
