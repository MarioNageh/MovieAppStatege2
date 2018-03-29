package com.example.user.moveappstage1.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.moveappstage1.Database.MovieContract;
import com.example.user.moveappstage1.Movies;
import com.example.user.moveappstage1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22/02/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.IamgeViewHolder> {
    // List<String> Links;
    // List<String> Names;
    Context context;
    Cursor cursor;
    List<String> IDS_Favourits;
    List<Movies> movies;
    final private OnItemClickListner listner;

    public ImageAdapter(List<String> links, List<String> Names, Context context, OnItemClickListner onItemClickListner) {
        this.listner = onItemClickListner;
        //   this.Links = links;
        ///   this.Names = Names;
        this.context = context;
    }

    public ImageAdapter(Context context, OnItemClickListner listner) {
        this.context = context;
        this.listner = listner;
    }

    @Override
    public IamgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int LayoutFormListItem = R.layout.ltemlist;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(LayoutFormListItem, parent, false);
        IamgeViewHolder holder = new IamgeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(IamgeViewHolder holder, int position) {

        holder.putPic(context, movies.get(position).getMovie_Poster(), movies.get(position).getTitle(), movies.get(position));

    }

    @Override
    public int getItemCount() {
        if (null == movies)
            return 0;
        return movies.size();
    }

    class IamgeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView Title;
        ImageButton ImageButton;
        RatingBar ratingBar;
        boolean favrite = false;

        public IamgeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_item_image);
            Title = itemView.findViewById(R.id.movie_item_title);
            ImageButton = itemView.findViewById(R.id.movie_item_btn_favorite);
            ratingBar = itemView.findViewById(R.id.ratingd_bar_Movie_item);

            itemView.setOnClickListener(this);
        }

        void putPic(Context context, String Pic, String s, Movies moviese) {
            //Remark The Favourites IDs
            if (IDS_Favourits != null) {
                for (String id : IDS_Favourits) {
                    if (id.equals(moviese.getID())) {
                        ImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.selector_favorite_button_in));
                        favrite = true;
                        break;
                    } else {
                        ImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.selector_favorite_button));
                        favrite = false;
                    }
                }
            }

            Title.setText(s);

            Picasso.with(context).load(Pic).into(imageView);

            if (Float.valueOf(moviese.getVote_average()) > 10)
                ratingBar.setRating(10);
            else {
                float value = Float.valueOf(moviese.getVote_average()) * .5f;
                ratingBar.setRating(value);
            }
        }

        @Override
        public void onClick(View view) {
            int itemclicked = getAdapterPosition();
            listner.onItemClicked(itemclicked, favrite);
        }
    }

    public interface OnItemClickListner {
        void onItemClicked(int i, Boolean Favurite);
    }

    public void setLinks(Cursor cursorr, ArrayList<Movies> movies) {
        // this.Links=links;
        IDS_Favourits = new ArrayList<>();
        // this.Names=Names;
        this.movies = movies;

        if (cursorr == null)
            return;
        if (cursorr.moveToFirst()) {
            do {
                IDS_Favourits.add(cursorr.getString(cursorr.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID_Movie)));
            } while (cursorr.moveToNext());
        }
        // cursorr.close();
        notifyDataSetChanged();
    }
}
