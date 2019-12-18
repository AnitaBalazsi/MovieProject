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
import android.widget.SearchView;

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
public class HomeFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    private static int pageNumber = 1;
    private Retrofit retrofit;
    private MovieDbAPI api;
    private MovieListAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog loadingDialog;
    private int totalPages;
    private Button nextButton, previousButton;
    private SearchView searchView;

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
        loadingDialog.show();
        getData();

        if (pageNumber > 1){
            previousButton.setVisibility(View.VISIBLE);
        }
        if (pageNumber < totalPages){
            nextButton.setVisibility(View.VISIBLE);
        }

        loadingDialog.dismiss();
    }

    private void initializeVariables() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MovieDbAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(MovieDbAPI.class);

        recyclerView = getView().findViewById(R.id.movieListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new MovieListAdapter(new ArrayList<Movie>());
        recyclerView.setAdapter(adapter);

        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setMessage(getString(R.string.loading));

        searchView = getView().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        nextButton = getView().findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        previousButton = getView().findViewById(R.id.previousButton);
        previousButton.setOnClickListener(this);

    }

    private void getData() {
        api.getTopRatedMovies(MovieDbAPI.API_KEY, pageNumber).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.body() != null){
                    totalPages = response.body().getPages();
                    adapter.setMovieList(response.body().getMovies());
                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        api.searchMovie(MovieDbAPI.API_KEY,pageNumber,query).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
               adapter.setMovieList(response.body().getMovies());
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
