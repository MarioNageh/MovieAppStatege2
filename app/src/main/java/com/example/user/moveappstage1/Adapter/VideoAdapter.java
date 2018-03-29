package com.example.user.moveappstage1.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.user.moveappstage1.Modules.Videos;
import com.example.user.moveappstage1.Networking.Network;
import com.example.user.moveappstage1.R;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by user on 10/03/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    List<Videos> Videos;
    Context context;
    final private VideoAdapter.OnItemClickListner listner;

    public VideoAdapter(List<Videos> Videosss, Context context, VideoAdapter.OnItemClickListner onItemClickListner) {
        this.listner = onItemClickListner;
        this.Videos = Videosss;
        this.context = context;
    }

    public VideoAdapter(Context context, VideoAdapter.OnItemClickListner listner) {
        this.context = context;
        this.listner = listner;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int LayoutFormListItem = R.layout.videolist;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(LayoutFormListItem, parent, false);
        VideoAdapter.VideoViewHolder holder = new VideoAdapter.VideoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {

        holder.putPic(context, this.Videos.get(position));

    }

    @Override
    public int getItemCount() {
        if (null == this.Videos)
            return 0;
        return Videos.size();
    }


    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView Title;

        public VideoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Video_Iamge);
            Title = itemView.findViewById(R.id.Video_Title);
            itemView.setOnClickListener(this);
        }

        void putPic(Context context, Videos videoooo) {
            Log.e("JSONExceeeeeeption: ", videoooo.getTrailer_name());

            Title.setText(videoooo.getTrailer_name());
            Picasso.with(context).load(videoooo.getVideoIamge()).into(imageView);
        }

        @Override
        public void onClick(View view) {
            int itemclicked = getAdapterPosition();
            listner.onItemClicked(itemclicked);
        }
    }

    public interface OnItemClickListner {
        void onItemClicked(int i);
    }

    public void setLinks(List<Videos> videos) {
        this.Videos = videos;
        Log.i("this.Videos", this.Videos.size() + "");
        notifyDataSetChanged();
    }
}
