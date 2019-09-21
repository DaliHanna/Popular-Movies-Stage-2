package com.example.android.popularstage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewsList {
    @SerializedName("results")
    private ArrayList<Review> reviews;

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
