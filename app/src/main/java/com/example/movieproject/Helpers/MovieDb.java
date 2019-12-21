package com.example.movieproject.Helpers;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDb {
    public static String API_KEY = "5a126949dac916c91e70aabb0134c8f8";
    public static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public static MovieDbAPI getInstance() {
        String BASE_URL = "https://api.themoviedb.org/3/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        return retrofit.create(MovieDbAPI.class);
    }
}
