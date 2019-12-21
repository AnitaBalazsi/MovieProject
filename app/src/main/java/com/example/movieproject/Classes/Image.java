package com.example.movieproject.Classes;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("aspect_ratio")
    private float aspect;

    @SerializedName("file_path")
    private String path;

    @SerializedName("height")
    private int height;

    @SerializedName("iso_631_1")
    private String iso631;

    @SerializedName("vote_average")
    private float voteAvg;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("width")
    private int width;

    public Image(float aspect, String path, int height, String iso631, float voteAvg, int voteCount, int width) {
        this.aspect = aspect;
        this.path = path;
        this.height = height;
        this.iso631 = iso631;
        this.voteAvg = voteAvg;
        this.voteCount = voteCount;
        this.width = width;
    }

    public float getAspect() {
        return aspect;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getIso631() {
        return iso631;
    }

    public void setIso631(String iso631) {
        this.iso631 = iso631;
    }

    public float getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(float voteAvg) {
        this.voteAvg = voteAvg;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
