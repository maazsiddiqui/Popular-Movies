package com.example.maazsiddiqui.movies.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.maazsiddiqui.movies.Utils.MovieResult;
import com.example.maazsiddiqui.movies.Utils.ReviewResult;
import com.example.maazsiddiqui.movies.Utils.VideoResult;

@Database(entities = {MovieResult.class, VideoResult.class, ReviewResult.class}, version = 1, exportSchema = false)
@TypeConverters(GenreConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorite_movies";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
