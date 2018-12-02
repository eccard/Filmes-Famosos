package com.example.eccard.filmesfamosos.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerResult {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("key")
    @Expose
    private String key;

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}