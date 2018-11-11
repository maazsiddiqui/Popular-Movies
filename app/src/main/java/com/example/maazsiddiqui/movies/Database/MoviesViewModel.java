package com.example.maazsiddiqui.movies.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import com.example.maazsiddiqui.movies.Utils.MovieResult;

public class MoviesViewModel extends AndroidViewModel {

    private LiveData<List<MovieResult>> allMoviesFromDB;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mdb = AppDatabase.getsInstance(this.getApplication());
        allMoviesFromDB = mdb.movieDao().loadAllMoviesFromDB();
    }

    public LiveData<List<MovieResult>> getAllMoviesFromDB() {
        return allMoviesFromDB;
    }
}
