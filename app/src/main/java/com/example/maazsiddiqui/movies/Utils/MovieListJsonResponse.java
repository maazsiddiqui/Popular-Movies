package com.example.maazsiddiqui.movies.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListJsonResponse {

    public Integer page;
    @SerializedName("total_results")
    public Integer totalResults;
    @SerializedName("total_pages")
    public Integer totalPages;
    public List<MovieResult> results;

    public MovieListJsonResponse(Integer page, Integer totalResults, Integer totalPages, List<MovieResult> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public List<MovieResult> getResults() {
        return results;
    }
}
