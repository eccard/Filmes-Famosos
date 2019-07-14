package com.example.eccard.filmesfamosos.data.network.model.network;

import java.util.List;

import com.example.eccard.filmesfamosos.data.network.model.TrailerResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieTrailersReviewResponse {


    @SerializedName("results")
    @Expose
    private List<TrailerResult> results = null;

    public List<TrailerResult> getResults() {
        return results;
    }

    public void setResults(List<TrailerResult> results) {
        this.results = results;
    }
}