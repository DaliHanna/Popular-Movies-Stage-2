package com.example.android.popularstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularstage2.R;
import com.example.android.popularstage2.model.Movie;
import com.example.android.popularstage2.utils.UrlInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private ArrayList<Movie> mMoviesList;
    private Context mContext;

    private MovieItemClickListener mMovieItemClickListener;

    public MoviesAdapter(MovieItemClickListener listener) {
        mMoviesList = new ArrayList<>();
        mMovieItemClickListener = listener;
    }

    public void setMoviesList(ArrayList<Movie> list) {
        mMoviesList = list;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.list_item, viewGroup, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMoviesList != null ? mMoviesList.size() : 0;
    }

    public interface MovieItemClickListener {
        void onMovieClick(Movie movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        TextView textView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mImageView = itemView.findViewById(R.id.img_movie_thumbnail);
            textView = itemView.findViewById(R.id.title);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mMovieItemClickListener.onMovieClick(mMoviesList.get(position));
        }

        public void bind(int listIndex) {
            String image = UrlInfo.THE_MOVIE_DB_IMAGE_API_URL +
                    UrlInfo.THE_MOVIE_DB_DEFAULT_IMAGE_SIZE +
                    mMoviesList.get(listIndex).getPoster_path();
            Picasso.with(mContext).load(image).into(mImageView);

            String title = mMoviesList.get(listIndex).getOriginal_title();

            textView.setText(title);
        }

    }
}
