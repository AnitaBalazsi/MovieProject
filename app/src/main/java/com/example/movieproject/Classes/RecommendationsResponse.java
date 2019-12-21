package com.example.movieproject.Classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendationsResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> movies;

    public RecommendationsResponse(int page, List<Movie> movies) {
        this.page = page;
        this.movies = movies;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
