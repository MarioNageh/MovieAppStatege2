package com.example.user.moveappstage1.Background;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.user.moveappstage1.JsonParse.JsonAnalysis;
import com.example.user.moveappstage1.Modules.Reviews;
import com.example.user.moveappstage1.Modules.Videos;
import com.example.user.moveappstage1.Movies;
import com.example.user.moveappstage1.Networking.Network;
import com.example.user.moveappstage1.PagesState;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 08/03/2018.
 */

public class AsyanTaskLoaderDetails implements LoaderManager.LoaderCallbacks<ArrayList<String>> {
    Context context;
    AsyncTaskCompleteListenerDetails<ArrayList<Reviews>, ArrayList<Videos>> listener;
    static ProgressDialog progressDialogl;
    static String Query;
    static ProgressBar mLoadingIndicator_ViedowDeatilng, mLoadingIndicator_ReviewsDeatilng;
    static ImageView FavutireButton;

    public AsyanTaskLoaderDetails(Context context, String QueryInBunder, AsyncTaskCompleteListenerDetails<ArrayList<Reviews>, ArrayList<Videos>> listener, ProgressBar mLoadingIndicator_ViedowDeatilng, ProgressBar mLoadingIndicator_ReviewsDeatilng, ImageView imageView) {
        this.context = context;
        this.listener = listener;
        this.Query = QueryInBunder;
        this.mLoadingIndicator_ViedowDeatilng = mLoadingIndicator_ViedowDeatilng;
        this.mLoadingIndicator_ReviewsDeatilng = mLoadingIndicator_ReviewsDeatilng;
        this.FavutireButton = imageView;
    }

    private static class Looodereee extends AsyncTaskLoader<ArrayList<String>> {
        Bundle ages;
        Context mcontext;
        ArrayList<String> Returner;

        public Looodereee(Context context) {
            super(context);
            this.mcontext = context;
        }

        public Looodereee(Context context, Bundle ages) {
            super(context);
            this.ages = ages;
            this.mcontext = context;
        }

        @Override
        public ArrayList<String> loadInBackground() {
            ArrayList<String> Url = ages.getStringArrayList(Query);
            Returner = new ArrayList<>();
            if (Url == null) {
                return null;
            }
            for (int i = 0; i < Url.size(); i++) {
                String Data = "";
                try {
                    URL urls = new URL(Url.get(i));
                    Log.i("Url", urls.toString());
                    urls = Network.BuildURL(Url.get(i));
                    Data = Network.GetDataFromHtml(urls);
                    Returner.add(Data);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return Returner;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (ages == null) {
                return;
            }
            forceLoad();
         /*   progressDialogl=new ProgressDialog(mcontext);
            progressDialogl.setCancelable(false);
            progressDialogl.setTitle("Please Watting");
            progressDialogl.show();*/
            FavutireButton.setVisibility(View.INVISIBLE);
            mLoadingIndicator_ViedowDeatilng.setVisibility(View.VISIBLE);
            mLoadingIndicator_ReviewsDeatilng.setVisibility(View.VISIBLE);
        }

        @Override
        public void deliverResult(ArrayList<String> data) {
            Returner = data;
            super.deliverResult(data);
        }
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, final Bundle args) {
        return new Looodereee(context, args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {
        ArrayList<Reviews> reviews = new ArrayList<>();
        ArrayList<Videos> Videos = new ArrayList<>();
        ArrayList<String> VideoLinks = new ArrayList<>();
        ArrayList<String> VideoIamges = new ArrayList<>();
        if (data != null) {
            reviews = JsonAnalysis.parseReviewsJson(data.get(0));
            Videos = JsonAnalysis.parseVideoJson(data.get(1));

            listener.onTaskComplete(reviews, Videos);
        }
        FavutireButton.setVisibility(View.VISIBLE);
        mLoadingIndicator_ViedowDeatilng.setVisibility(View.INVISIBLE);
        mLoadingIndicator_ReviewsDeatilng.setVisibility(View.INVISIBLE);
        //  progressDialogl.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }

    public interface AsyncTaskCompleteListenerDetails<ArrayList, N> {
        public void onTaskComplete(ArrayList Reviews, N Videos);
    }

}


