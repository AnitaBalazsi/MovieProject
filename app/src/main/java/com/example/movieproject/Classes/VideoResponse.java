package com.example.movieproject.Classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {
    @SerializedName("id")
    private int movieId;

    @SerializedName("results")
    private List<Video> video;

    public VideoResponse(int movieId, List<Video> video) {
        this.movieId = movieId;
        this.video = video;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<Video> getVideo() {
        return video;
    }

    public void setVideo(List<Video> video) {
        this.video = video;
    }
}
