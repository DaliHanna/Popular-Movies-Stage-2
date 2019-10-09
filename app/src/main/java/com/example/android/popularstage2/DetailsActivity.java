package com.example.android.popularstage2;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.popularstage2.adapter.ReviewsAdapter;
import com.example.android.popularstage2.adapter.VideosAdapter;
import com.example.android.popularstage2.database.FavoriteMovie;
import com.example.android.popularstage2.database.MovieDatabase;
import com.example.android.popularstage2.model.Movie;
import com.example.android.popularstage2.model.Review;
import com.example.android.popularstage2.model.ReviewsList;
import com.example.android.popularstage2.model.Video;
import com.example.android.popularstage2.model.VideosList;
import com.example.android.popularstage2.utils.UrlInfo;
import com.example.android.popularstage2.utils.VideoNameComparator;

import com.example.android.popularstage2.databinding.ActivityMovieDetailsBinding;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity
        implements VideosAdapter.TrailerItemClickListener {

    private Movie mMovie;
    private ArrayList<Video> mVideosList;
    private ArrayList<Review> mReviewsList;

    private VideosAdapter mVideosListAdapter;
    private ReviewsAdapter mReviewsListAdapter;

    private ActivityMovieDetailsBinding mBinding;

    Button button;
    private MovieDatabase mDb;
    private Boolean isFav = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMovie != null) {
            outState.putParcelable(
                    getString(R.string.movie_details_bundle),
                    mMovie);
        }

        if (mVideosList != null) {
            outState.putParcelableArrayList(
                    getString(R.string.movie_video_bundle),
                    mVideosList
            );
        }

        if (mReviewsList != null) {
            outState.putParcelableArrayList(
                    getString(R.string.movie_review_bundle),
                    mReviewsList
            );
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        button = findViewById(R.id.btn_add_remove_to_favs);

        getValuesFromIntentOrBundle(savedInstanceState);
        initializeLayoutParams();

        if (isFav) {
            mBinding.btnAddRemoveToFavs.setText(getString(R.string.btn_remove_from_favs));
        } else {
            mBinding.btnAddRemoveToFavs.setText(getString(R.string.btn_add_to_favs));

        }
        mBinding.btnAddRemoveToFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFav) {
                    mBinding.btnAddRemoveToFavs.setText(getString(R.string.btn_add_to_favs));

                } else {
                    mBinding.btnAddRemoveToFavs.setText(getString(R.string.btn_remove_from_favs));


                }
                final FavoriteMovie mov = new FavoriteMovie(
                        Integer.parseInt(mMovie.getId()),
                        mMovie.getOriginal_title(),
                        mMovie.getRelease_date(),
                        mMovie.getVote_average(),
                        mMovie.getOverview(),
                        mMovie.getPoster_path(),
                        mMovie.getBackdrop_path()
                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFav) {
                            // delete item
                            mDb.movieDao().deleteMovie(mov);
                        } else {
                            // insert item
                            mDb.movieDao().insertMovie(mov);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                boolean ischeck = !isFav;
                                setFavorite(ischeck);
                            }
                        });
                    }

                });
            }
        });


    }

    private void setFavorite(Boolean fav) {
        if (fav) {
            isFav = true;
        } else {
            isFav = false;
        }
    }


    private void getValuesFromIntentOrBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null ||
                !savedInstanceState.containsKey(getString(R.string.movie_details_bundle))) {
            Intent intent = getIntent();

            if (intent.hasExtra(getString(R.string.movies_from_list_to_details_intent))) {
                mMovie = intent.getParcelableExtra(getString(R.string.movies_from_list_to_details_intent));

                mDb = MovieDatabase.getInstance(getApplicationContext());
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final FavoriteMovie fmov = mDb.movieDao().loadMovieById(Integer.parseInt(mMovie.getId()));
                        setFavorite((fmov != null) ? true : false);
                    }
                });
            }
            return;
        }

        if (savedInstanceState.containsKey(getString(R.string.movie_details_bundle))) {
            mMovie = savedInstanceState.getParcelable(getString(R.string.movie_details_bundle));
        }

        if (savedInstanceState.containsKey(getString(R.string.movie_video_bundle))) {
            mVideosList = savedInstanceState.getParcelableArrayList(getString(R.string.movie_video_bundle));
        }

        if (savedInstanceState.containsKey(getString(R.string.movie_review_bundle))) {
            mReviewsList = savedInstanceState.getParcelableArrayList(getString(R.string.movie_review_bundle));
        }
    }

    private void initializeLayoutParams() {
        if (mMovie != null) {
            mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

            setTitle(mMovie.getOriginal_title());

            mBinding.tvMovieTitle.setText(mMovie.getOriginal_title());
            mBinding.tvMovieReleasedate.setText(mMovie.getRelease_date());

            mBinding.rbMovieRating.setStepSize(0.25f);
            mBinding.rbMovieRating.setRating(Float.parseFloat(mMovie.getVote_average()) / 2);

            mBinding.tvMovieSynopsis.setText(mMovie.getOverview());

            String image = UrlInfo.THE_MOVIE_DB_IMAGE_API_URL +
                    UrlInfo.THE_MOVIE_DB_DEFAULT_IMAGE_SIZE +
                    mMovie.getPoster_path();
            Picasso.with(this).load(image).into(mBinding.ivMovieThumb);

            mBinding.rvMovieTrailersList.setLayoutManager(new LinearLayoutManager(this));
            mBinding.rvMovieReviewsList.setLayoutManager(new LinearLayoutManager(this));

            mVideosListAdapter = new VideosAdapter(this, mVideosList, this);

            mReviewsListAdapter = new ReviewsAdapter();

            if (mVideosList == null) {
                getTrailerContent(mMovie.getId());
            } else {
                mBinding.pbLoadTrailerList.setVisibility(View.GONE);

                mVideosListAdapter.setVideosList(mVideosList);
                mBinding.rvMovieTrailersList.setAdapter(mVideosListAdapter);
            }

            if (mReviewsList != null) {
                mReviewsListAdapter.setReviewsList(mReviewsList);
                mBinding.rvMovieReviewsList.setAdapter(mReviewsListAdapter);
            }
        }
    }

    private void getTrailerContent(String id) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(UrlInfo.THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MovieApi client = retrofit.create(MovieApi.class);

        Call<VideosList> call = client.listOfVideos(id, UrlInfo.API_KEY);

        call.enqueue(new Callback<VideosList>() {
            @Override
            public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                mBinding.pbLoadTrailerList.setVisibility(View.GONE);
                VideosList list = response.body();
                mVideosList = list.getVideos();

                if (mVideosList.size() == 0) {
                    mBinding.tvMovieNoTrailerFound.setVisibility(View.VISIBLE);
                } else {
                    mBinding.tvMovieNoTrailerFound.setVisibility(View.GONE);
                    // Display Video Trailers sorted by Name
                    Collections.sort(mVideosList, new VideoNameComparator());

                    mVideosListAdapter.setVideosList(mVideosList);
                    mBinding.rvMovieTrailersList.setAdapter(mVideosListAdapter);
                }
            }

            @Override
            public void onFailure(Call<VideosList> call, Throwable t) {
                String message = getString(R.string.errors);

                Toast.makeText(DetailsActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getReviewContent() {
        mBinding.pbLoadReviewList.setVisibility(View.VISIBLE);
        mBinding.rvMovieReviewsList.setVisibility(View.GONE);
        mBinding.tvMovieNoReviewFound.setVisibility(View.GONE);
        mBinding.btnLoadReviews.setEnabled(false);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(UrlInfo.THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MovieApi client = retrofit.create(MovieApi.class);

        Call<ReviewsList> call = client.listOfReviews(mMovie.getId(), UrlInfo.API_KEY);

        call.enqueue(new Callback<ReviewsList>() {
            @Override
            public void onResponse(Call<ReviewsList> call, Response<ReviewsList> response) {
                mBinding.pbLoadReviewList.setVisibility(View.GONE);
                mBinding.rvMovieReviewsList.setVisibility(View.VISIBLE);
                mBinding.btnLoadReviews.setEnabled(true);

                ReviewsList list = response.body();
                mReviewsList = list.getReviews();

                if (mReviewsList.size() == 0) {
                    mBinding.tvMovieNoReviewFound.setVisibility(View.VISIBLE);
                } else {
                    mBinding.tvMovieNoReviewFound.setVisibility(View.GONE);

                    mReviewsListAdapter.setReviewsList(mReviewsList);
                    mBinding.rvMovieReviewsList.setAdapter(mReviewsListAdapter);
                }
            }

            @Override
            public void onFailure(Call<ReviewsList> call, Throwable t) {
                String message = getString(R.string.exceptional_error);

                Toast.makeText(DetailsActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onTrailerClick(Video video) {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + video.getKey()));

        if (intent.resolveActivity(pm) != null) {
            startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException anfe) {
                Toast.makeText(this, getString(R.string.device_not_capable_to_watch), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void LoadReviews(View view) {
        getReviewContent();
    }

    public void AddOrRemoveToFavs(View view) {


    }

}
