package com.example.maazsiddiqui.movies.Utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("/3/movie/{category}?")
    Call<MovieListJsonResponse> getMovies(
            @Path("category") String category,
            @Query("api_key") String key);

    @GET("/3/movie/{id}/videos?")
    Call<VideoJsonResponse> getVideos(
            @Path("id") Integer id,
            @Query("api_key") String key);

    @GET("/3/movie/{id}/reviews?")
    Call<MovieReviewJsonResponse> getReviews(
            @Path("id") Integer id,
            @Query("api_key") String key);
}
