package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eccard.filmesfamosos.R;
import com.example.eccard.filmesfamosos.data.network.model.TrailerResult;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    public interface OnViewClicked{
        void onVideoCliked(String videoKey);
    }

    OnViewClicked videoClickListener;

    Context context;

    LayoutInflater layoutInflater;

    List<TrailerResult> trailerResults;

    public TrailerAdapter(Context context, OnViewClicked videoClickListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.videoClickListener = videoClickListener;
    }

    public void setTrailerResults(List<TrailerResult> trailerResults) {
        this.trailerResults = trailerResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_trailer,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = trailerResults.get(position).getName();
        holder.tvTrailerName.setText(name);
    }

    @Override
    public int getItemCount() {

        return trailerResults == null ? 0 : trailerResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTrailerName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTrailerName = itemView.findViewById(R.id.tv_trailer_name);

            itemView.findViewById(R.id.fl_adapter_trailler).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            videoClickListener.onVideoCliked(trailerResults.get(getAdapterPosition()).getKey());
        }
    }
}
