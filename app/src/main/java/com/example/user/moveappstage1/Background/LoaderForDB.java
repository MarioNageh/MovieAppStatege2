package com.example.user.moveappstage1.Background;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.user.moveappstage1.Database.MovieContract;

/**
 * Created by user on 10/03/2018.
 */

public class LoaderForDB implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    Context context;
    LoaderBDCompleteListenerDetails listener;


    public LoaderForDB(Context context,LoaderBDCompleteListenerDetails listener) {
        this.context = context;
        this.listener = listener;

    }
    private static class LoaderForDBAsyan extends AsyncTaskLoader<Cursor>{


        Context mcontext;
        Cursor mCursor=null;

        public LoaderForDBAsyan(Context context) {
            super(context);
            this.mcontext=context;
        }

        public Cursor loadInBackground() {
            String[] projection = new String[] {MovieContract.MovieEntry.COLUMN_ID_Movie};

            try{
                return mcontext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,projection ,null,null,null);
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mCursor != null) {
                deliverResult(mCursor);
            } else {
                forceLoad();
            }

        }
        @Override
        public void deliverResult(Cursor data) {
            mCursor=data;
            super.deliverResult(data);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i,final Bundle args) {
        return new LoaderForDBAsyan(context);

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            listener.ONLoaderOFDataBaseComplete(data);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public interface LoaderBDCompleteListenerDetails<N> {
        public void ONLoaderOFDataBaseComplete(N Cursor);
    }
}
