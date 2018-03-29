package com.example.user.moveappstage1.Database;

import com.example.user.moveappstage1.Database.MovieContract;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by user on 10/03/2018.
 */

public class MovieContentProvider extends ContentProvider {
    private MovieDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new MovieDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        int matcher = MovieContract.SURIMATcher.match(uri);
        Cursor cursor;
        String[] projection = new String[]{MovieContract.MovieEntry.COLUMN_ID_Movie}; //specify columns you want to return
        switch (matcher) {
            case MovieContract.MOVIES_Matcher_ALL:
                cursor = sqLiteDatabase.query(MovieContract.MovieEntry.Table_Name
                        , null
                        , null
                        , null,
                        null
                        , null,
                        MovieContract.MovieEntry.COLUMN_MoviePoster);
                break;
            default:
                throw new UnsupportedOperationException("Not True");

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = MovieContract.SURIMATcher.match(uri);
        Uri retunerUri;
        switch (match) {
            case MovieContract.MOVIES_Matcher_ALL:
                long id = db.insert(MovieContract.MovieEntry.Table_Name, null, contentValues);
                if (id > 0) {
                    retunerUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Conn't insert" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not Operation");

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retunerUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = MovieContract.SURIMATcher.match(uri);
        int retunner = 0;
        switch (match) {
            case MovieContract.MOVIES_Matcher_With_ID:
                String Id = uri.getPathSegments().get(1);
                Log.i("IdIdIdId", Id);
                String M_Selection = MovieContract.MovieEntry.COLUMN_ID_Movie + "=?";
                String[] M_SelectionArrgs = new String[]{Id};
                retunner = db.delete(MovieContract.MovieEntry.Table_Name, M_Selection, M_SelectionArrgs);
                break;
            default:
                throw new UnsupportedOperationException("Not Operation");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retunner;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
