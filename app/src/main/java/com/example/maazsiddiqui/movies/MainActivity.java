package com.example.maazsiddiqui.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.maazsiddiqui.movies.Database.MoviesViewModel;
import com.example.maazsiddiqui.movies.RecyclerViewUtils.ImageAdapter;
import com.example.maazsiddiqui.movies.RecyclerViewUtils.SpaceDecoration;
import com.example.maazsiddiqui.movies.Utils.Api;
import com.example.maazsiddiqui.movies.Utils.MovieListJsonResponse;
import com.example.maazsiddiqui.movies.Utils.MovieResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.maazsiddiqui.movies.RecyclerViewUtils.NoOfColumns.calculateNoOfColumns;
import static com.example.maazsiddiqui.movies.Constants.*;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ListItemClickListener {

    @BindView(R.id.rv_images)
    RecyclerView mRecyclerView;
    public ImageAdapter mImageAdapter;
    public static Api api;

    private List<MovieResult> movieList;
    private String movieCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            movieCategory = savedInstanceState.getString(MOVIE_CATEGORY_KEY);
            movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
        } else {
            movieCategory = MOVIE_CATEGORY_POPULAR;
        }

        setupRetrofit();
        setupRecyclerView();
        setupViewModel();
        loadUI();
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_MOVIE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpaceDecoration(GRID_ITEM_SPACING));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, Math.max(2, calculateNoOfColumns(this))));
        mImageAdapter = new ImageAdapter(this, this);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void setupViewModel() {
        MoviesViewModel viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        viewModel.getAllMoviesFromDB().observe(this, new Observer<List<MovieResult>>() {
            @Override
            public void onChanged(@Nullable List<MovieResult> movieResults) {
                if (movieCategory.equals(MOVIE_CATEGORY_FAVORITE)) {
                    mImageAdapter.setmResults(movieResults);
                }
            }
        });
    }

    private void loadUI() {

        setTitle();

        if (movieCategory.equals(MOVIE_CATEGORY_FAVORITE)) {
            setupViewModel();
        } else if (movieList == null || movieList.isEmpty()) {
            Call<MovieListJsonResponse> movieListFromAPI = api.getMovies(movieCategory, BuildConfig.PARAM_MOVIE_API_KEY);
            movieListFromAPI.enqueue(new Callback<MovieListJsonResponse>() {
                @Override
                public void onResponse(Call<MovieListJsonResponse> call, Response<MovieListJsonResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        movieList = response.body().results;
                        mImageAdapter.setmResults(movieList);
                    } else {
                        Toast.makeText(MainActivity.this, WEB_SERVER_ERROR, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieListJsonResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, NETWORK_ERROR, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            mImageAdapter.setmResults(movieList);
        }
    }

    private void setTitle() {
        switch (movieCategory) {
            case MOVIE_CATEGORY_POPULAR:
                setTitle(MOVIE_LABEL_POPULAR);
                break;
            case MOVIE_CATEGORY_TOP_RATED:
                setTitle(MOVIE_LABEL_TOP_RATED);
                break;
            default:
                setTitle(MOVIE_LABEL_FAVORITE);
                break;
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_SELECTED_KEY, mImageAdapter.getResultofIndex(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();
        String previousMovieCategory = movieCategory;

        if (selectedItemId == R.id.top_rated) {
            movieCategory = MOVIE_CATEGORY_TOP_RATED;
        } else if (selectedItemId == R.id.popular) {
            movieCategory = MOVIE_CATEGORY_POPULAR;
        } else if (selectedItemId == R.id.favorite) {
            movieCategory = MOVIE_CATEGORY_FAVORITE;
        }

        if (!previousMovieCategory.equals(movieCategory)) {
            mRecyclerView.removeAllViewsInLayout();
            mImageAdapter.clearResults();
            movieList.clear();
            loadUI();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(MOVIE_CATEGORY_KEY, movieCategory);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<? extends Parcelable>) movieList);
        super.onSaveInstanceState(outState);
    }

}

