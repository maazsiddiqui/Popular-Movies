package com.example.maazsiddiqui.movies.RecyclerViewUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maazsiddiqui.movies.R;

import java.util.ArrayList;
import java.util.List;

import com.example.maazsiddiqui.movies.Utils.ReviewResult;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewResult> mResults;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mReviewAuthor;
        TextView mReviewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            mReviewAuthor = itemView.findViewById(R.id.tv_author);
            mReviewContent = itemView.findViewById(R.id.tv_content);
        }

    }

    public ReviewAdapter(Context mContext) {
        this.mResults = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setmResults(List<ReviewResult> mResults) {
        this.mResults.clear();
        this.mResults.addAll(mResults);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.mReviewAuthor.setText(mResults.get(position).getAuthor());
        holder.mReviewContent.setText(mResults.get(position).getContent());
    }


    @Override
    public int getItemCount() {
        return mResults.size();
    }

}
