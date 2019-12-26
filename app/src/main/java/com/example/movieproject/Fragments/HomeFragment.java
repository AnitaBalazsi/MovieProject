package com.example.movieproject.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.SearchView;

import com.example.movieproject.Activities.DetailsActivity;
import com.example.movieproject.Helpers.MovieDb;
import com.example.movieproject.Helpers.MovieListAdapter;
import com.example.movieproject.Classes.Movie;
import com.example.movieproject.Classes.MoviesResponse;
import com.example.movieproject.Helpers.MovieDbAPI;
import com.example.movieproject.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, MovieListAdapter.ListViewHolder.MovieClickListener {
    private static int pageNumber = 1;
    private MovieListAdapter adapter;
    private ProgressDialog loadingDialog;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;


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
    }

    private void initializeVariables() {
        RecyclerView recyclerView = getView().findViewById(R.id.movieListView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    pageNumber++;
                    getData();
                }
            }
        });

        adapter = new MovieListAdapter(new ArrayList<Movie>(),this);
        recyclerView.setAdapter(adapter);

        loadingDialog = new ProgressDialog(getContext(), R.style.ProgressDialog);
        loadingDialog.setMessage(getString(R.string.loading));

        SearchView searchView = getView().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
    }

    public void onMovieClick(int position){
        //open detail screen
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("selectedMovie",adapter.getMovieList().get(position));
        startActivity(intent);
    }

    private void getData() {
        MovieDb.getInstance().getTopRatedMovies(MovieDb.API_KEY, pageNumber).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.body() != null){
                    adapter.addMovies(response.body().getMovies());
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
    public boolean onQueryTextSubmit(String query) {
        if (!query.isEmpty()){
            MovieDb.getInstance().searchMovie(MovieDb.API_KEY,pageNumber,query).enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    adapter.setMovieList(response.body().getMovies());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                }
            });
        } else {
            pageNumber = 1;
            getData();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            MovieDb.getInstance().searchMovie(MovieDb.API_KEY,pageNumber,newText).enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    adapter.setMovieList(response.body().getMovies());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                }
            });
        } else {
            pageNumber = 1;
            getData();
        }
        return false;
    }

}
