package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.model.MovieReviewResult;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewItem> {

    private final LayoutInflater layoutInflater;
    private List<MovieReviewResult> reviews;

    public void setReviews(List<MovieReviewResult> reviews) {
        this.reviews = reviews;
    }

    public ReviewsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ReviewItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_review_item,parent,false);
        return new ReviewItem(view);
    }

    @Override
    public void onBindViewHolder(ReviewItem holder, int position) {
        MovieReviewResult reivew = reviews.get(position);
        holder.tvUser.setText(reivew.getAuthor());
        holder.tvReviewContent.setText(reivew.getContent());

    }

    @Override
    public int getItemCount() {
        return (reviews == null) ? 0 : reviews.size();
    }

    public static class ReviewItem extends RecyclerView.ViewHolder{

        TextView tvUser;
        TextView tvReviewContent;
        ReviewItem(View itemView) {
            super(itemView);

            tvUser = itemView.findViewById(R.id.textViewUser);
            tvReviewContent = itemView.findViewById(R.id.textReviewContent);

        }
    }


}
