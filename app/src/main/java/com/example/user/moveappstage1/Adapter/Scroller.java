package com.example.user.moveappstage1.Adapter;

import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by user on 28/02/2018.
 */

public abstract class Scroller extends RecyclerView.OnScrollListener {
    // before loading more.
    private int visibleThreshold = 7;
    // Cuuren Page we also in page 1 because fist we download data from api from page 1
    private int currentPage = 1;
    //Total item Counr To save total item
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // if there no data or no internet connection to loadata  this is converd because the asyntask get page 1 all the time
    private int startingPageIndex = 0;
    RecyclerView.LayoutManager mLayoutManager;

    public Scroller(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        // befor load any data
        visibleThreshold = visibleThreshold * ((GridLayoutManager) mLayoutManager).getSpanCount();
    }

    public void SetSpan(int span) {
        GridLayoutManager gridLayoutManager = ((GridLayoutManager) mLayoutManager);
        gridLayoutManager.setSpanCount(span);
        this.mLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {

        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        Log.i("totalItemCount ", totalItemCount + "");
        if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            Log.i("lastVisibleItemPosition", lastVisibleItemPosition + "");
            // to assign valuse to compare beneath
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            Log.i("previousTotalItemCount", previousTotalItemCount + "");

            // stop loading where totalitems in layout > the last tolal item
            // thats mean we can scroll down again
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                currentPage = (totalItemCount / 20);
                previousTotalItemCount = totalItemCount;
            }
            // on stop loading and there is some viwes to scroll
            // send the abstract with page number
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                if (currentPage == 0 && (lastVisibleItemPosition + visibleThreshold) > 0) {
                    currentPage = (totalItemCount / 20) + 1; //when press top_rated$$Most_Poular
                    Log.i("currentPage+1", currentPage + "");

                } else {
                    currentPage++;
                    Log.i("currentPage+2", currentPage + "");
                }
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }
    }

    public void remakeLayout() {
        //to remove all layour views
        this.mLayoutManager.removeAllViews();
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}