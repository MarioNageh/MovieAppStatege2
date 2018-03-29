package com.example.user.moveappstage1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.moveappstage1.Modules.Reviews;
import com.example.user.moveappstage1.Modules.Videos;
import com.example.user.moveappstage1.R;

import java.util.List;

/**
 * Created by user on 10/03/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    List<Reviews> reviews;
    Context context;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    public ReviewAdapter(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int LayoutFormListItem = R.layout.reviewlsit;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(LayoutFormListItem, parent, false);
        ReviewAdapter.ReviewHolder holder = new ReviewAdapter.ReviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.putvaluse(reviews.get(position).getAuthor(), reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (reviews == null)
            return 0;
        return reviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView AuthorName, Review;

        public ReviewHolder(View itemView) {
            super(itemView);
            AuthorName = itemView.findViewById(R.id.review_Autohr);
            Review = itemView.findViewById(R.id.review_review);
        }

        void putvaluse(String name, String review) {
            AuthorName.setText(name);
            Log.i("HoleeeedeRAutho", name);

            Review.setText(review);

        }


    }

    public void setLinks(List<Reviews> Reviews) {
        this.reviews = Reviews;
        notifyDataSetChanged();
    }
}
