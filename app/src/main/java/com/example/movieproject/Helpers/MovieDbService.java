package com.example.movieproject.Helpers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.movieproject.Classes.Movie;
import com.example.movieproject.Classes.NowPlayingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDbService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.deleteNowPlaying();

        MovieDb.getInstance().getNowPlaying(MovieDb.API_KEY).enqueue(new Callback<NowPlayingResponse>() {
            @Override
            public void onResponse(Call<NowPlayingResponse> call, Response<NowPlayingResponse> response) {
                if (response.isSuccessful()){
                    for (Movie movie : response.body().getResults()){
                        databaseHelper.addNowPlayingMovie(movie);
                    }
                }
            }

            @Override
            public void onFailure(Call<NowPlayingResponse> call, Throwable t) {

            }
        });

        return super.onStartCommand(intent,flags,startId);
    }
}
