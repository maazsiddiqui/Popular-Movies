package com.example.maazsiddiqui.movies.Database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class VideoByIdViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final Integer movieId;

    public VideoByIdViewModelFactory(AppDatabase mDb, Integer movieId) {
        this.mDb = mDb;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VideoByIdViewModel(mDb, movieId);
    }

}
