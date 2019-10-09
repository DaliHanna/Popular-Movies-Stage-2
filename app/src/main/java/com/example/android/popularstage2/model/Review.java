package com.example.android.popularstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;
    private String url;

    public Review(Parcel parcel) {
        this.author = parcel.readString();
        this.content = parcel.readString();
        this.url = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getAuthor());
        parcel.writeString(getContent());
        parcel.writeString(getUrl());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
