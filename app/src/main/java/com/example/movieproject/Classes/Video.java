package com.example.movieproject.Classes;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("site")
    private String site;

    @SerializedName("iso_639_1")
    private String iso639;

    @SerializedName("iso_3166_1")
    private String iso3166;

    @SerializedName("name")
    private String name;

    @SerializedName("size")
    private int size;

    @SerializedName("type")
    private String type;

    public Video(String id, String key, String site, String iso639, String iso3166, String name, int size, String type) {
        this.id = id;
        this.key = key;
        this.site = site;
        this.iso639 = iso639;
        this.iso3166 = iso3166;
        this.name = name;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
