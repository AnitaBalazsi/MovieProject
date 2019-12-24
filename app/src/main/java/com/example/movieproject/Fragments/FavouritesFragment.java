package com.example.movieproject.Fragments;


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

import com.example.movieproject.Activities.DetailsActivity;
import com.example.movieproject.Activities.LoginActivity;
import com.example.movieproject.Classes.Movie;
import com.example.movieproject.Helpers.DatabaseHelper;
import com.example.movieproject.Helpers.MovieListAdapter;
import com.example.movieproject.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment implements MovieListAdapter.ListViewHolder.MovieClickListener {

    private MovieListAdapter adapter;

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeVariables();
    }

    private void initializeVariables() {
        RecyclerView recyclerView = getView().findViewById(R.id.movieListView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));


        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        adapter = new MovieListAdapter(databaseHelper.getFavourites(LoginActivity.username),this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMovieClick(int position) {
        //open detail screen
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("selectedMovie",adapter.getMovieList().get(position));
        startActivity(intent);
    }
}
