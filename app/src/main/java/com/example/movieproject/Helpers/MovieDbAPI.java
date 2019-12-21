package com.example.movieproject.Helpers;

import com.example.movieproject.Classes.ImagesReponse;
import com.example.movieproject.Classes.Movie;
import com.example.movieproject.Classes.MoviesResponse;
import com.example.movieproject.Classes.RecommendationsResponse;
import com.example.movieproject.Classes.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbAPI {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("search/movie")
    Call<MoviesResponse> searchMovie(@Query("api_key") String apiKey, @Query("page") int page, @Query("query") String query);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideo( @Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/images")
    Call<ImagesReponse> getImages(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/recommendations")
    Call<RecommendationsResponse> getRecommendations(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
