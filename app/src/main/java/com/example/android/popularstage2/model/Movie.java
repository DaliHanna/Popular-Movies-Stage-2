package com.example.android.popularstage2.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularstage2.data.MovieContract;

public class Movie implements Parcelable {

    private String id;
    private String original_title;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private String vote_average;
    private String release_date;

    public Movie (Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        this.original_title = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
        this.poster_path = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
        this.backdrop_path = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_BACK_PATH));
        this.overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_OVERVIEW));
        this.vote_average = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
        this.release_date = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.original_title = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readString();
        this.release_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getId());
        parcel.writeString(getOriginal_title());
        parcel.writeString(getPoster_path());
        parcel.writeString(getBackdrop_path());
        parcel.writeString(getOverview());
        parcel.writeString(getVote_average());
        parcel.writeString(getRelease_date());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
