package com.example.maazsiddiqui.movies.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoJsonResponse {

    public Integer id;
    @SerializedName("results")
    public List<VideoResult> videoResultList;

    public VideoJsonResponse(Integer id, List<VideoResult> videoResultList) {
        this.id = id;
        this.videoResultList = videoResultList;
    }

    public Integer getId() {
        return id;
    }

    public List<VideoResult> getVideoResultList() {
        return videoResultList;
    }
}
