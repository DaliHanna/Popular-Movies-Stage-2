package com.example.android.popularstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MoviesList {
    @SerializedName("results")
    private ArrayList<Movie> movies;

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}
