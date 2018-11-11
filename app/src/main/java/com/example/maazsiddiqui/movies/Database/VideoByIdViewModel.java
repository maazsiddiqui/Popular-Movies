package com.example.maazsiddiqui.movies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import com.example.maazsiddiqui.movies.Utils.VideoResult;

public class VideoByIdViewModel extends ViewModel {


    private LiveData<List<VideoResult>> movieVideos;

    public VideoByIdViewModel(AppDatabase mdb, Integer id) {
        this.movieVideos = mdb.movieDao().loadMovieVideosByIdFromDB(id);
    }

    public LiveData<List<VideoResult>> getMovieVideos() {
        return movieVideos;
    }

}
