package com.example.user.moveappstage1.Background;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.user.moveappstage1.JsonParse.JsonAnalysis;
import com.example.user.moveappstage1.Movies;
import com.example.user.moveappstage1.Networking.Network;
import com.example.user.moveappstage1.PagesState;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 02/03/2018.
 */

public class NetworkAsyanTask extends AsyncTask<URL, Void, String> {
    Context context;
    AsyanTaskLoaderC.AsyncTaskCompleteListener<ArrayList<Movies>, PagesState> listener;
    ProgressBar bar;
    boolean getpages;

    public NetworkAsyanTask(Context context, AsyanTaskLoaderC.AsyncTaskCompleteListener<ArrayList<Movies>, PagesState> listener, ProgressBar bar, boolean getpages) {
        this.context = context;
        this.listener = listener;
        this.bar = bar;
        this.getpages = getpages;
    }

    @Override
    protected void onPostExecute(String s) {
        bar.setVisibility(View.INVISIBLE);
        if (s != null && !s.equals("")) {
            ArrayList<Movies> movies = JsonAnalysis.parseMoviesJson(s);
            PagesState state = JsonAnalysis.MakePageState(s);
            if (getpages)
                listener.onTaskComplete(movies, state);
            else
                listener.onTaskComplete(movies, state);

        }
    }

    @Override
    protected String doInBackground(URL... urls) {
        String Data = "";
        try {
            Data = Network.GetDataFromHtml(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        bar.setVisibility(View.VISIBLE);

    }
}
