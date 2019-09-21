package com.example.android.popularstage2.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public Video(Parcel parcel) {
        this.key = parcel.readString();
        this.name = parcel.readString();
        this.site = parcel.readString();
        this.size = parcel.readString();
        this.type = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getKey());
        parcel.writeString(getName());
        parcel.writeString(getSite());
        parcel.writeString(getSize());
        parcel.writeString(getType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel parcel) {
            return new Video(parcel);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
