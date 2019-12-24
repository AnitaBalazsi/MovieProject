package com.example.movieproject.Helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieproject.Classes.Movie;
import com.example.movieproject.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ListViewHolder> {
    private List<Movie> movieList;
    private ListViewHolder.MovieClickListener movieClickListener;

    public MovieListAdapter(List<Movie> movieList, ListViewHolder.MovieClickListener movieClickListener) {
        this.movieList = movieList;
        this.movieClickListener = movieClickListener;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void addMovies(List<Movie> movies){
        this.movieList.addAll(movies);
    }

    @NonNull
    @Override
    public MovieListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.movie_list_item, parent,false);
        ListViewHolder viewHolder = new ListViewHolder(listItem,movieClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ListViewHolder holder, int position) {
        holder.movieTitle.setText(movieList.get(position).getTitle());
        holder.movieDate.setText(movieList.get(position).getReleaseDate());
        holder.movieOverview.setText(movieList.get(position).getOverview());
        holder.movieRating.setText(String.valueOf(movieList.get(position).getVoteAverage()).concat("%"));
        Glide.with(holder.itemView.getContext()).load(MovieDb.IMAGE_BASE_URL + movieList.get(position).getPosterPath()).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }



    public static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView movieTitle, movieDate, movieOverview, movieRating;
        private ImageView moviePoster;
        private MovieClickListener movieClickListener;

        public ListViewHolder(@NonNull View itemView, MovieClickListener movieClickListener) {
            super(itemView);

            this.movieClickListener = movieClickListener;
            this.moviePoster = itemView.findViewById(R.id.moviePoster);
            this.movieTitle = itemView.findViewById(R.id.movieTitle);
            this.movieDate = itemView.findViewById(R.id.movieDate);
            this.movieOverview = itemView.findViewById(R.id.overview);
            this.movieRating = itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            movieClickListener.onMovieClick(getAdapterPosition());
        }

        public interface MovieClickListener{
            void onMovieClick(int position);
        }
    }
}
