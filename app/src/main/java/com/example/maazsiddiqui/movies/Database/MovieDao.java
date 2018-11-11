package com.example.maazsiddiqui.movies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import com.example.maazsiddiqui.movies.Utils.MovieResult;
import com.example.maazsiddiqui.movies.Utils.ReviewResult;
import com.example.maazsiddiqui.movies.Utils.VideoResult;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY id ASC")
    LiveData<List<MovieResult>> loadAllMoviesFromDB();

    @Query("SELECT * FROM movies WHERE id = :movieID")
    LiveData<MovieResult> loadMovieByIdFromDB(Integer movieID);

    @Query("SELECT * FROM videos WHERE movieID = :movieID")
    LiveData<List<VideoResult>> loadMovieVideosByIdFromDB(Integer movieID);

    @Query("SELECT * FROM reviews WHERE movieID = :movieID")
    LiveData<List<ReviewResult>> loadMovieReviewsByIdFromDB(Integer movieID);

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieID LIMIT 1)")
    LiveData<Integer> MovieExistsInDB(Integer movieID);

    @Insert
    void insertMovieIntoDB(MovieResult movieResult);

    @Insert
    void insertMovieVideosIntoDB(List<VideoResult> movieVideos);

    @Insert
    void insertMovieReviewsIntoDB(List<ReviewResult> movieReviews);

    @Delete
    void deleteMovieFromDB(MovieResult movieResult);
}
