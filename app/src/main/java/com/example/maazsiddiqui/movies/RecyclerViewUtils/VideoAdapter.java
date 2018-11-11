package com.example.maazsiddiqui.movies.RecyclerViewUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maazsiddiqui.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.example.maazsiddiqui.movies.Utils.VideoResult;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<VideoResult> mResults;
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mVideoThumb;
        TextView mVideoDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            mVideoThumb = itemView.findViewById(R.id.iv_video);
            mVideoDescription = itemView.findViewById(R.id.tv_video);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public VideoAdapter(Context mContext, ListItemClickListener listener) {
        this.mResults = new ArrayList<>();
        this.mContext = mContext;
        this.mOnClickListener = listener;
    }

    public void setmResults(List<VideoResult> mResults) {
        this.mResults.clear();
        this.mResults.addAll(mResults);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new VideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get()
                .load("https://img.youtube.com/vi/" + mResults.get(position).getKey() + "/mqdefault.jpg")
                .into(holder.mVideoThumb);

        holder.mVideoDescription.setText(mResults.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }
}
