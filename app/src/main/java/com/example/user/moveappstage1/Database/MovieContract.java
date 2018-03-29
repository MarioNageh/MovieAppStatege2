package com.example.user.moveappstage1.Database;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by user on 10/03/2018.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.user.moveappstage1";
    public static final String SCHEME = "content://";
    public static final String DataBase_Path = "Movie";
    public static final Uri Base_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);
    public static final UriMatcher SURIMATcher = BuildMatcher();


    public static final int MOVIES_Matcher_ALL = 100;
    public static final int MOVIES_Matcher_With_ID = 101;

    public static UriMatcher BuildMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, DataBase_Path, MOVIES_Matcher_ALL);
        matcher.addURI(AUTHORITY, DataBase_Path + "/#", MOVIES_Matcher_With_ID);
        return matcher;
    }


    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Base_CONTENT_URI.buildUpon()
                .appendPath(DataBase_Path).build();


        public static final String Table_Name = "MovieDB";
        public static final String COLUMN_Title = "Title";
        public static final String COLUMN_RelaseDate = "RelaseDate";
        public static final String COLUMN_MoviePoster = "MoviPoster";
        public static final String COLUMN_VoteAverage = "MoviAvergae";
        public static final String COLUMN_Plot_Synopsis = "PlotSynopsis";
        public static final String COLUMN_Videos = "Videos";
        public static final String COLUMN_Revieows = "Reviews";
        public static final String COLUMN_ID_Movie = "IDMovie";
    }
}
