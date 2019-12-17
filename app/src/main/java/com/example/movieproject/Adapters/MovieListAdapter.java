package com.example.movieproject.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieproject.Classes.Movie;
import com.example.movieproject.MovieDbAPI;
import com.example.movieproject.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ListViewHolder> {
    private List<Movie> movieList;

    public MovieListAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.movie_list_item, parent,false);
        ListViewHolder viewHolder = new ListViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ListViewHolder holder, int position) {
        holder.movieTitle.setText(movieList.get(position).getTitle());
        holder.movieDate.setText(movieList.get(position).getReleaseDate());
        holder.movieOverview.setText(movieList.get(position).getOverview());
        Glide.with(holder.itemView.getContext()).load(MovieDbAPI.IMAGE_BASE_URL + movieList.get(position).getPosterPath()).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView movieTitle, movieDate, movieOverview;
        private ImageView moviePoster;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDate = itemView.findViewById(R.id.movieDate);
            movieOverview = itemView.findViewById(R.id.overview);
        }
    }
}
