package com.example.user.moveappstage1.Networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.user.moveappstage1.BuildConfig;
import com.example.user.moveappstage1.Constats;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by user on 23/02/2018.
 */

public class Network {
    //ToDo put Your Api Key Here
    public static String Api_Key = BuildConfig.API_KEY;
    public static String API_LINK_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    public static String API_LINK_Top_Related = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    public static String[] ApiLinks = new String[]{API_LINK_POPULAR, API_LINK_Top_Related};
    private static String Page_Query = "page";
    static boolean hasedited = false;

    public static void MakeUrlWithApikey() {
        if (!hasedited) {
            API_LINK_POPULAR += Api_Key;
            API_LINK_Top_Related += Api_Key;
            ApiLinks = new String[]{API_LINK_POPULAR, API_LINK_Top_Related};
            hasedited = true;
        }
    }

    public static String MakeUrlToReviewOrViews(String id, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constats.Main_Url);
        builder.append(id);
        builder.append(type);
        builder.append(Constats.Api_Key_String);
        builder.append(Api_Key);
        Log.i("String Builder", builder.toString());
        return builder.toString();
    }


    public static URL BuildURL(String Link) {
        //For Building The URL
        URL ReturnURl = null;
        try {
            ReturnURl = new URL(Link.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ReturnURl;
    }

    public static URL MakeUrlForPages(String Link, String Page) {
        Uri uri = Uri.parse(Link)
                .buildUpon().appendQueryParameter(Page_Query, Page).build();
        URL RetunerUrl = null;
        try {
            RetunerUrl = new URL(uri.toString());
            return RetunerUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String GetDataFromHtml(URL url) throws IOException {
        //For Getting The Data From Web Page
        HttpURLConnection HttpConnection = (HttpURLConnection) url.openConnection();
        try {
            // Read The Response Body
            InputStream stream = HttpConnection.getInputStream();
            // To Store Data
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            // is Ready From InComing Data ?
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.i("Loooooger", e.getMessage());
            return null;
        } finally {
            // Close Connection
            HttpConnection.disconnect();
        }
    }

    public static boolean TestNetwork(Context context) {
        // For Test Connection
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }
}
