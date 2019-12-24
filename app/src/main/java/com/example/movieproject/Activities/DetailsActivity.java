package com.example.movieproject.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieproject.Classes.Image;
import com.example.movieproject.Classes.ImagesReponse;
import com.example.movieproject.Classes.Movie;
import com.example.movieproject.Classes.RecommendationsResponse;
import com.example.movieproject.Classes.Video;
import com.example.movieproject.Classes.VideoResponse;
import com.example.movieproject.Fragments.FavouritesFragment;
import com.example.movieproject.Helpers.DatabaseHelper;
import com.example.movieproject.Helpers.MovieDb;
import com.example.movieproject.R;
import com.example.movieproject.Utilities;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends YouTubeBaseActivity implements View.OnClickListener {
    private final String YOUTUBE_API_KEY = "AIzaSyAXOnWL0BvBZwWu9P4PPi6BUF3wOdn15n4";
    private Movie movie;
    private YouTubePlayerView videoView;
    private LinearLayout imageContainer, recommendationsContainer;
    private ImageView backView, addToFavourites;
    private TextView movieTitle, movieOverview;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        initializeVariables();
        getMovieTrailer();
        getImages();
        getRecommendations();
        getMovieDetails();
    }

    private void getMovieDetails() {
        MovieDb.getInstance().getMovieDetails(movie.getId(),MovieDb.API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                movieTitle.setText(response.body().getTitle());
                movieOverview.setText(response.body().getOverview());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    private void getRecommendations() {
        MovieDb.getInstance().getRecommendations(movie.getId(),MovieDb.API_KEY).enqueue(new Callback<RecommendationsResponse>() {
            @Override
            public void onResponse(Call<RecommendationsResponse> call, Response<RecommendationsResponse> response) {
                if (response.body() != null){
                    loadRecommendations(response.body().getMovies());
                }
            }

            @Override
            public void onFailure(Call<RecommendationsResponse> call, Throwable t) {

            }
        });
    }

    private void loadRecommendations(List<Movie> movies) {
        for (final Movie movie : movies){
            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);

            TextView title = new TextView(this);
            title.setPadding(60,0,0,0);
            title.setText(movie.getTitle());

            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(400,400));
            Glide.with(imageView.getContext()).load(MovieDb.IMAGE_BASE_URL.concat(movie.getPosterPath())).into(imageView);

            layout.addView(imageView);
            layout.addView(title);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //reloads activity with selected recommendation

                    finish();
                    startActivity(getIntent().putExtra("movieId",movie.getId()));
                }
            });

            recommendationsContainer.addView(layout);
        }
    }

    private void getImages() {
        MovieDb.getInstance().getImages(movie.getId(),MovieDb.API_KEY).enqueue(new Callback<ImagesReponse>() {
            @Override
            public void onResponse(Call<ImagesReponse> call, Response<ImagesReponse> response) {
                if (response.body() != null){
                    loadImages(response.body().getImages());
                }
            }

            @Override
            public void onFailure(Call<ImagesReponse> call, Throwable t) {

            }
        });
    }

    private void loadImages(List<Image> images) {
        for (Image image : images){
            ImageView imageView = new ImageView(getBaseContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(600,600));
            Glide.with(imageView.getContext()).load(MovieDb.IMAGE_BASE_URL.concat(image.getPath())).into(imageView);
            imageContainer.addView(imageView);
        }
    }

    private void initializeVariables() {
        databaseHelper = new DatabaseHelper(this);
        movie = (Movie) getIntent().getSerializableExtra("selectedMovie");

        videoView = findViewById(R.id.movieTrailer);
        imageContainer = findViewById(R.id.images);
        recommendationsContainer = findViewById(R.id.recommendations);

        backView = findViewById(R.id.back);
        backView.setOnClickListener(this);

        addToFavourites = findViewById(R.id.addToFavourites);
        if (databaseHelper.checkIfInFavourites(LoginActivity.username, movie)){
            addToFavourites.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp));
        } else {
            addToFavourites.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }
        addToFavourites.setOnClickListener(this);

        movieTitle = findViewById(R.id.movieTitle);
        movieOverview = findViewById(R.id.movieOverview);
    }

    private void getMovieTrailer() {
        MovieDb.getInstance().getVideo(movie.getId(),MovieDb.API_KEY).enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.body() != null){
                    loadVideo(response.body().getVideo().get(0));
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });
    }

    private void loadVideo(final Video video) {
        videoView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(video.getKey());

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.addToFavourites:
                if (addToFavourites.getDrawable().getConstantState().equals(getDrawable(R.drawable.ic_favorite_border_black_24dp).getConstantState())){
                    //add to favourites
                    databaseHelper.addFavouriteMovie(LoginActivity.username,movie);
                    addToFavourites.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp));

                    Toast.makeText(this,getString(R.string.addedToFavourites),Toast.LENGTH_SHORT).show();
                } else {
                    //delete from favourites
                    databaseHelper.deleteFromFavourites(LoginActivity.username,movie);
                    addToFavourites.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp));

                    Toast.makeText(this,getString(R.string.removedFromFavourites),Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
