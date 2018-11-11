package com.example.maazsiddiqui.movies;

public class Constants {

    // URLs
    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org";
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w";

    // Movie Categories and labels
    public static final String MOVIE_CATEGORY_POPULAR = "popular";
    public static final String MOVIE_LABEL_POPULAR = "Popular Movies";
    public static final String MOVIE_CATEGORY_TOP_RATED = "top_rated";
    public static final String MOVIE_LABEL_TOP_RATED = "Top Rated Movies";
    public static final String MOVIE_CATEGORY_FAVORITE = "favorite";
    public static final String MOVIE_LABEL_FAVORITE = "Favorite Movies";

    // Keys for (key, value)
    public static final String MOVIE_LIST_KEY = "movieListKey";
    public static final String MOVIE_CATEGORY_KEY = "movieCategoryKey";
    public static final String MOVIE_SELECTED_KEY = "movieSelectedKey";
    public static final String MOVIE_VIDEOS_KEY = "movieVideosKey";
    public static final String MOVIE_REVIEWS_KEY = "movieReviewsKey";

    // Errors
    public static final String WEB_SERVER_ERROR = "Web Server Error. Please retry later.";
    public static final String NETWORK_ERROR = "Network Error. Please retry later.";
    public static final String LOADING_ERROR = "Loading error. Please retry later.";

    // Misc
    public static final int GRID_ITEM_SPACING = 20;
    public static final int MOVIE_IMAGE_POSTER_WIDTH = 185;
    public static final int MOVIE_EXISTS_IN_DB = 1;
}
