package com.example.maazsiddiqui.movies.RecyclerViewUtils;

import android.content.Context;
import android.util.DisplayMetrics;

import static com.example.maazsiddiqui.movies.Constants.MOVIE_IMAGE_POSTER_WIDTH;

public class NoOfColumns {

    // got this method from StackOverflow
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / MOVIE_IMAGE_POSTER_WIDTH);
    }
}
