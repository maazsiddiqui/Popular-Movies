package com.example.maazsiddiqui.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.maazsiddiqui.movies.Database.AppDatabase;
import com.example.maazsiddiqui.movies.Database.AppExecutors;
import com.example.maazsiddiqui.movies.Database.MovieByIdViewModel;
import com.example.maazsiddiqui.movies.Database.MovieByIdViewModelFactory;
import com.example.maazsiddiqui.movies.Database.MovieExistsInDBViewModel;
import com.example.maazsiddiqui.movies.Database.MovieExistsInDBViewModelFactory;
import com.example.maazsiddiqui.movies.Database.ReviewByIdViewModel;
import com.example.maazsiddiqui.movies.Database.ReviewByIdViewModelFactory;
import com.example.maazsiddiqui.movies.Database.VideoByIdViewModel;
import com.example.maazsiddiqui.movies.Database.VideoByIdViewModelFactory;
import com.example.maazsiddiqui.movies.RecyclerViewUtils.ReviewAdapter;
import com.example.maazsiddiqui.movies.RecyclerViewUtils.VideoAdapter;
import com.example.maazsiddiqui.movies.Utils.Api;
import com.example.maazsiddiqui.movies.Utils.MovieResult;
import com.example.maazsiddiqui.movies.Utils.MovieReviewJsonResponse;
import com.example.maazsiddiqui.movies.Utils.ReviewResult;
import com.example.maazsiddiqui.movies.Utils.VideoJsonResponse;
import com.example.maazsiddiqui.movies.Utils.VideoResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.maazsiddiqui.movies.Constants.*;


public class MovieDetailActivity extends AppCompatActivity implements VideoAdapter.ListItemClickListener {

    @BindView(R.id.iv_movie_poster)
    ImageView mMoviePoster;
    @BindView(R.id.tv_original_title)
    TextView mOriginalTitle;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;
    @BindView(R.id.tv_user_ratings)
    TextView mUserRatings;
    @BindView(R.id.tb_favorite)
    ToggleButton mFavoriteToggleButton;
    @BindView(R.id.tv_overview)
    TextView mOverview;
    @BindView(R.id.tv_videos)
    TextView mVideoSectionTitle;
    @BindView(R.id.tv_reviews)
    TextView mReviewSectionTitle;
    @BindView(R.id.rv_videos)
    RecyclerView mVideoRecyclerView;
    VideoAdapter mVideoAdapter;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewRecyclerView;
    ReviewAdapter mReviewAdapter;
    MovieResult selectedMovieResult;
    public List<VideoResult> movieVideos;
    public List<ReviewResult> movieReviews;
    private AppDatabase mDB;
    public Api api;
    public Boolean movieInDB;
    public Boolean videosLoaded = false;
    public Boolean reviewsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mDB = AppDatabase.getsInstance(getApplicationContext());
        setupRetrofit();
        setupVideosRecyclerView();
        setupReviewsRecyclerView();
        mFavoriteToggleButton.setEnabled(false);

        if (savedInstanceState != null) {
            selectedMovieResult = savedInstanceState.getParcelable(MOVIE_SELECTED_KEY);
            movieVideos = savedInstanceState.getParcelableArrayList(MOVIE_VIDEOS_KEY);
            movieReviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS_KEY);
            setupUI(selectedMovieResult);
        } else if (getIntent().hasExtra(MOVIE_SELECTED_KEY)) {
            selectedMovieResult = getIntent().getParcelableExtra(MOVIE_SELECTED_KEY);
            final int selectedMovieKey = selectedMovieResult.getId();

            MovieExistsInDBViewModelFactory movieExistsInDBViewModelFactory = new MovieExistsInDBViewModelFactory(mDB, selectedMovieKey);
            final MovieExistsInDBViewModel movieExistsInDBViewModel = ViewModelProviders.of(this, movieExistsInDBViewModelFactory).get(MovieExistsInDBViewModel.class);
            movieExistsInDBViewModel.getMovieExistsInDb().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
                    movieExistsInDBViewModel.getMovieExistsInDb().removeObserver(this);
                    movieInDB = (integer == MOVIE_EXISTS_IN_DB);
                    if (movieInDB) {
                        mFavoriteToggleButton.setChecked(true);
                        loadMovieByID(selectedMovieKey);
                    } else {
                        mFavoriteToggleButton.setChecked(false);
                    }
                    setupUI(selectedMovieResult);
                }
            });
        } else {
            Toast.makeText(this, LOADING_ERROR, Toast.LENGTH_LONG).show();
        }

        mFavoriteToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFavoriteToggleButton.isChecked()) {
                    int movieId = selectedMovieResult.getId();
                    for (VideoResult videoResult : movieVideos) {
                        videoResult.movieID = movieId;
                    }
                    for (ReviewResult reviewResult : movieReviews) {
                        reviewResult.movieID = movieId;
                    }

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDB.movieDao().insertMovieIntoDB(selectedMovieResult);
                            mDB.movieDao().insertMovieVideosIntoDB(movieVideos);
                            mDB.movieDao().insertMovieReviewsIntoDB(movieReviews);
                        }
                    });
                    mFavoriteToggleButton.setChecked(true);
                } else {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDB.movieDao().deleteMovieFromDB(selectedMovieResult);
                        }
                    });
                    mFavoriteToggleButton.setChecked(false);
                }
            }
        });

    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_MOVIE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    private void setupVideosRecyclerView() {
        mVideoRecyclerView.setHasFixedSize(false);
        mVideoRecyclerView.setNestedScrollingEnabled(false);
        mVideoRecyclerView.setFocusable(false);
        RecyclerView.LayoutManager layoutManagerVideo = new LinearLayoutManager(this);
        mVideoRecyclerView.setLayoutManager(layoutManagerVideo);
        mVideoAdapter = new VideoAdapter(this, this);
        mVideoRecyclerView.setAdapter(mVideoAdapter);
    }

    private void setupReviewsRecyclerView() {
        mReviewRecyclerView.setHasFixedSize(false);
        mReviewRecyclerView.setNestedScrollingEnabled(false);
        mReviewRecyclerView.setFocusable(false);
        RecyclerView.LayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(layoutManagerReviews);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
    }

    private void loadMovieByID(int selectedMovieKey) {
        MovieByIdViewModelFactory movieByIdViewModelFactory = new MovieByIdViewModelFactory(mDB, selectedMovieKey);
        final MovieByIdViewModel movieByIdViewModel = ViewModelProviders.of(this, movieByIdViewModelFactory).get(MovieByIdViewModel.class);
        movieByIdViewModel.getMovie().observe(this, new Observer<MovieResult>() {
            @Override
            public void onChanged(@Nullable MovieResult movieResult) {
                movieByIdViewModel.getMovie().removeObserver(this);
                selectedMovieResult = movieResult;
            }
        });
    }

    public void getVideoList(int movieId) {
        if (movieInDB) {
            VideoByIdViewModelFactory videoByIdViewModelFactory = new VideoByIdViewModelFactory(mDB, movieId);
            final VideoByIdViewModel videoByIdViewModel = ViewModelProviders.of(this, videoByIdViewModelFactory).get(VideoByIdViewModel.class);
            videoByIdViewModel.getMovieVideos().observe(this, new Observer<List<VideoResult>>() {
                @Override
                public void onChanged(@Nullable List<VideoResult> videoResults) {
                    videoByIdViewModel.getMovieVideos().removeObserver(this);
                    loadVideos(videoResults);
                }
            });
        } else {
            Call<VideoJsonResponse> videoList = api.getVideos(movieId, BuildConfig.PARAM_MOVIE_API_KEY);
            videoList.enqueue(new Callback<VideoJsonResponse>() {
                @Override
                public void onResponse(Call<VideoJsonResponse> call, Response<VideoJsonResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        loadVideos(response.body().getVideoResultList());
                    } else {
                        Toast.makeText(MovieDetailActivity.this, WEB_SERVER_ERROR, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<VideoJsonResponse> call, Throwable t) {
                    Toast.makeText(MovieDetailActivity.this, NETWORK_ERROR, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void loadVideos(List<VideoResult> videoResultList) {
        if ((videoResultList != null) && (!videoResultList.isEmpty())) {
            movieVideos = videoResultList;
            mVideoAdapter.setmResults(movieVideos);
        } else {
            movieVideos = new ArrayList<>();
            mVideoSectionTitle.setText(getString(R.string.no_videos_available_label));
        }
        videosLoaded = true;
        considerFavoriteButtonVisibility();
    }

    public void getReviewList(int movieId) {
        if (movieInDB) {
            ReviewByIdViewModelFactory reviewByIdViewModelFactory = new ReviewByIdViewModelFactory(mDB, movieId);
            final ReviewByIdViewModel reviewByIdViewModel = ViewModelProviders.of(this, reviewByIdViewModelFactory).get(ReviewByIdViewModel.class);
            reviewByIdViewModel.getMovieReviews().observe(this, new Observer<List<ReviewResult>>() {
                @Override
                public void onChanged(@Nullable List<ReviewResult> reviewResults) {
                    reviewByIdViewModel.getMovieReviews().removeObserver(this);
                    loadReviews(reviewResults);
                }
            });
        } else {
            Call<MovieReviewJsonResponse> reviewList = api.getReviews(movieId, BuildConfig.PARAM_MOVIE_API_KEY);
            reviewList.enqueue(new Callback<MovieReviewJsonResponse>() {
                @Override
                public void onResponse(Call<MovieReviewJsonResponse> call, Response<MovieReviewJsonResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        loadReviews(response.body().getReviewResultList());
                    } else {
                        Toast.makeText(MovieDetailActivity.this, WEB_SERVER_ERROR, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieReviewJsonResponse> call, Throwable t) {
                    Toast.makeText(MovieDetailActivity.this, NETWORK_ERROR, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    void loadReviews(List<ReviewResult> reviewResultList) {
        if (reviewResultList != null && (!reviewResultList.isEmpty())) {
            movieReviews = reviewResultList;
            mReviewAdapter.setmResults(movieReviews);
        } else {
            movieReviews = new ArrayList<>();
            mReviewSectionTitle.setText(getString(R.string.no_reviews_available_label));
        }
        reviewsLoaded = true;
        considerFavoriteButtonVisibility();
    }

    private void setupUI(MovieResult result) {
        Picasso.get()
                .load(BASE_IMAGE_URL +
                        String.valueOf(MOVIE_IMAGE_POSTER_WIDTH) +
                        result.getPosterPath())
                .into(mMoviePoster);

        mOriginalTitle.setText(result.getTitle());
        mReleaseDate.setText(result.getReleaseDate());
        mUserRatings.setText(String.format("%1$,.1f", result.getVoteAverage()));
        mOverview.setText((result.getOverview().isEmpty() ? getString(R.string.movie_overview_missing) : result.getOverview()));
        if (movieVideos == null) {
            getVideoList(result.getId());
        } else {
            loadVideos(movieVideos);
        }
        if (movieReviews == null) {
            getReviewList(result.getId());
        } else {
            loadReviews(movieReviews);
        }

    }

    private void considerFavoriteButtonVisibility() {
        if (videosLoaded && reviewsLoaded) {
            mFavoriteToggleButton.setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getString(R.string.youtube_video_thumb_uri_scheme))
                .authority(getString(R.string.youtube_video_thumb_uri_authority))
                .appendPath(getString(R.string.youtube_video_thumb_uri_append_path))
                .appendQueryParameter(getString(R.string.youtube_video_thumb_uri_query_parameter), movieVideos.get(clickedItemIndex).getKey());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(builder.build());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_SELECTED_KEY, selectedMovieResult);
        outState.putParcelableArrayList(MOVIE_VIDEOS_KEY, (ArrayList<? extends Parcelable>) movieVideos);
        outState.putParcelableArrayList(MOVIE_REVIEWS_KEY, (ArrayList<? extends Parcelable>) movieReviews);
        super.onSaveInstanceState(outState);
    }
}
