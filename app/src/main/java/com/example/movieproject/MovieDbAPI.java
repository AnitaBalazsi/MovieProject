package com.example.movieproject;

import com.example.movieproject.Classes.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDbAPI {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "5a126949dac916c91e70aabb0134c8f8";
    String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("search/movie")
    Call<MoviesResponse> searchMovie(@Query("api_key") String apiKey, @Query("page") int page, @Query("query") String query);
}
