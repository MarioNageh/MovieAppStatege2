package com.example.user.moveappstage1.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 10/03/2018.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MoveDB.db";
    private static final int DATABASE_Version = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQl_CREATE_DATABASE = "CREATE TABLE " +
                MovieContract.MovieEntry.Table_Name + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_Title + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_ID_Movie + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RelaseDate + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MoviePoster + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_VoteAverage + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_Plot_Synopsis + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_Videos + " TEXT," +
                MovieContract.MovieEntry.COLUMN_Revieows + " TEXT" +
                ");";
        sqLiteDatabase.execSQL(SQl_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {
          final   String   DROP_TABE = "ALTER TABEL IF EXISTS" + MovieContract.MovieEntry.Table_Name;
            sqLiteDatabase.execSQL(DROP_TABE);
        }
        onCreate(sqLiteDatabase);
    }
}
