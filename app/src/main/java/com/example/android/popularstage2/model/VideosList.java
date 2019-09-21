package com.example.android.popularstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideosList {
    @SerializedName("results")
    private ArrayList<Video> videos;

    public ArrayList<Video> getVideos() {
        return videos;
    }
}
