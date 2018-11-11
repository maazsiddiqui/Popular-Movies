package com.example.maazsiddiqui.movies.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewJsonResponse {

    public Integer id;
    @SerializedName("page")
    public Integer pageNo;
    @SerializedName("results")
    List<ReviewResult> reviewResultList;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("total_results")
    public Integer totalResults;

    public MovieReviewJsonResponse(Integer id, Integer pageNo, List<ReviewResult> reviewResultList, Integer totalPages, Integer totalResults) {
        this.id = id;
        this.pageNo = pageNo;
        this.reviewResultList = reviewResultList;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public List<ReviewResult> getReviewResultList() {
        return reviewResultList;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }
}
