package com.example.android.popularstage2.utils;

import com.example.android.popularstage2.model.Video;
import java.util.Comparator;

// Comparator used to sort Video trailers by Name.
public class VideoNameComparator implements Comparator<Video> {
    public int compare(Video left, Video right) {
        return left.getName().compareTo(right.getName());
    }
}
