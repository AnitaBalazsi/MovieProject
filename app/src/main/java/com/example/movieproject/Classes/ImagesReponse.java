package com.example.movieproject.Classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImagesReponse {
    @SerializedName("id")
    private int movieId;

    @SerializedName("backdrops")
    private List<Image> images;

    public ImagesReponse(int movieId, List<Image> images) {
        this.movieId = movieId;
        this.images = images;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
