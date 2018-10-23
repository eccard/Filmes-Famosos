package com.example.eccard.filmesfamosos.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.AppApiHelper;
import com.example.eccard.filmesfamosos.data.network.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ItemViewHolder>{

    private List<MovieResult> movieResults;

    public void setMovieResults(List<MovieResult> movieResults) {
        this.movieResults = movieResults;
    }

    public interface OnMovieClickListener{
        void onMovieItemClick(MovieResult movie);
    }

    private OnMovieClickListener onMovieClickListener;

    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
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

        itemViewHolder.bind(movie.getPosterPath());
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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView imgMovie;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.img_movie_item);
            itemView.setOnClickListener(this);
        }

        private void bind(String posterPath){
            URL posterUrl = AppApiHelper.getInstance().generatePosterPath(posterPath);

            Picasso.get().load(posterUrl.toString()).into(imgMovie);
        }

        @Override
        public void onClick(View v) {
            onMovieClickListener.onMovieItemClick(movieResults.get(getAdapterPosition()));
        }
    }
}
