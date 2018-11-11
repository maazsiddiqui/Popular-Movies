package com.example.maazsiddiqui.movies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class MovieExistsInDBViewModel extends ViewModel {

    private LiveData<Integer> movieExistsInDb;

    public MovieExistsInDBViewModel(AppDatabase mdb, Integer movieId) {
        this.movieExistsInDb = mdb.movieDao().MovieExistsInDB(movieId);
    }

    public LiveData<Integer> getMovieExistsInDb() {
        return movieExistsInDb;
    }
}
