package com.example.maazsiddiqui.movies.RecyclerViewUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.maazsiddiqui.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.example.maazsiddiqui.movies.Utils.MovieResult;

import static com.example.maazsiddiqui.movies.Constants.BASE_IMAGE_URL;
import static com.example.maazsiddiqui.movies.Constants.MOVIE_IMAGE_POSTER_WIDTH;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<MovieResult> mResults;
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }


    public ImageAdapter(Context mContext, ListItemClickListener listener) {
        this.mResults = new ArrayList<>();
        this.mContext = mContext;
        this.mOnClickListener = listener;
    }

    public void setmResults(List<MovieResult> mResults) {
        this.mResults.clear();
        this.mResults.addAll(mResults);
        this.notifyDataSetChanged();
    }

    public MovieResult getResultofIndex(int index) {
        return mResults.get(index);
    }

    public void clearResults() {
        this.mResults.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get()
                .load(BASE_IMAGE_URL +
                        String.valueOf(MOVIE_IMAGE_POSTER_WIDTH) +
                        mResults.get(position).posterPath)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }


}
