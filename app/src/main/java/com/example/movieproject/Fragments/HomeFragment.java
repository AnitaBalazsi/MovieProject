package com.example.movieproject.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.movieproject.Adapters.MovieListAdapter;
import com.example.movieproject.Classes.Movie;
import com.example.movieproject.Classes.MoviesResponse;
import com.example.movieproject.MovieDbAPI;
import com.example.movieproject.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private static int pageNumber = 1;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog loadingDialog;
    private int totalPages;
    private Button nextButton, previousButton;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeVariables();
        getData(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                adapter = new MovieListAdapter(movies);
                recyclerView.setAdapter(adapter);

                if (pageNumber > 1){
                    previousButton.setVisibility(View.VISIBLE);
                }
                if (pageNumber < totalPages){
                    nextButton.setVisibility(View.VISIBLE);
                }
                loadingDialog.dismiss();
            }
        });
    }

    private void initializeVariables() {
        recyclerView = getView().findViewById(R.id.movieListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);

        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setMessage(getString(R.string.loading));
        loadingDialog.show();

        nextButton = getView().findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        previousButton = getView().findViewById(R.id.previousButton);
        previousButton.setOnClickListener(this);

    }

    private void getData(final OnGetMoviesCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDbAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieDbAPI api = retrofit.create(MovieDbAPI.class);
        api.getTopRatedMovies(MovieDbAPI.API_KEY, pageNumber).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.body() != null){
                    totalPages = response.body().getPages();
                    callback.onSuccess(response.body().getMovies());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextButton:
                pageNumber++;
                break;
            case R.id.previousButton:
                pageNumber--;
        }

        //refresh fragment
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    public interface OnGetMoviesCallback {
        void onSuccess(List<Movie> movies);
    }
}
