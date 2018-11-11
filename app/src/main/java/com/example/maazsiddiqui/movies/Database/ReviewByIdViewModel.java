package com.example.maazsiddiqui.movies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import com.example.maazsiddiqui.movies.Utils.ReviewResult;

public class ReviewByIdViewModel extends ViewModel {

    private LiveData<List<ReviewResult>> movieReviews;

    public ReviewByIdViewModel(AppDatabase mdb, Integer id) {
        this.movieReviews = mdb.movieDao().loadMovieReviewsByIdFromDB(id);
    }

    public LiveData<List<ReviewResult>> getMovieReviews() {
        return movieReviews;
    }
}
