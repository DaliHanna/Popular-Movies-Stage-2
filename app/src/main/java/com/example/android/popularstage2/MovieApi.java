package com.example.android.popularstage2;

import com.example.android.popularstage2.model.MoviesList;
import com.example.android.popularstage2.model.ReviewsList;
import com.example.android.popularstage2.model.VideosList;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("/3/movie/{sort}")
    Call<MoviesList> listOfMovies(@Path("sort") String sort, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<ReviewsList> listOfReviews(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<VideosList> listOfVideos(@Path("id") String id, @Query("api_key") String apiKey);
}
