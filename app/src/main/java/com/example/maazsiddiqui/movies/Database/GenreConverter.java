package com.example.maazsiddiqui.movies.Database;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

// got code from Stack Overflow website

public class GenreConverter {

    @TypeConverter
    public List<Integer> stringToList(String genreIds) {
        List<Integer> list = new ArrayList<>();

        String[] array = genreIds.split(",");

        for (String s : array) {
            if (!s.isEmpty()) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }

    @TypeConverter
    public String listToString(List<Integer> list) {
        StringBuilder genreIds = new StringBuilder();
        if (list != null && (!list.isEmpty())) {
            for (int i : list) {
                genreIds.append(",").append(i);
            }
        }
        return genreIds.toString();
    }
}
