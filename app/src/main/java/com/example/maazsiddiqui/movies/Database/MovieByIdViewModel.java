package com.example.maazsiddiqui.movies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.maazsiddiqui.movies.Utils.MovieResult;

public class MovieByIdViewModel extends ViewModel {

    private LiveData<MovieResult> movie;

    public MovieByIdViewModel(AppDatabase mdb, Integer id) {
        this.movie = mdb.movieDao().loadMovieByIdFromDB(id);
    }

    public LiveData<MovieResult> getMovie() {
        return movie;
    }
}
