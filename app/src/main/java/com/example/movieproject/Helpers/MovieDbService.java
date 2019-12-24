package com.example.movieproject.Helpers;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.movieproject.Classes.Movie;
import com.example.movieproject.Classes.NowPlayingResponse;

import java.util.ArrayList;

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
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.deleteNowPlaying();
                Log.d("movies","service");

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
                handler.postDelayed(this,2 * 60 * 1000);

            }
        };
        handler.postDelayed(runnable, 0);

        return START_STICKY;
    }
}
