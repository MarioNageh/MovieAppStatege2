package com.example.user.moveappstage1.JsonParse;


import android.util.Log;

import com.example.user.moveappstage1.Constats;
import com.example.user.moveappstage1.Modules.Reviews;
import com.example.user.moveappstage1.Modules.Videos;
import com.example.user.moveappstage1.Movies;
import com.example.user.moveappstage1.PagesState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 27/02/2018.
 */

public class JsonAnalysis {
    ///////////////////////////////////////////////////////////
    private static final String title = "title";
    private static final String release_date = "release_date";
    private static final String poster_path = "poster_path";
    private static final String vote_average = "vote_average";
    private static final String Plot_synopsis = "overview";
    private static final String Resuls = "results";
    private static final String page = "page";
    private static final String total_results = "total_results";
    private static final String total_pages = "total_pages";
    private static final String id = "id";
    ////////////////////////////////////////////////////////////
    private static String title_Value;
    private static String id_Value;
    private static String release_date_Value;
    private static String poster_path_Value;
    private static String vote_Value;
    private static String Plot_synopsis_Value;
    private static String page_Value;
    private static String total_results_Value;
    private static String total_pages_Value;
    /////////////////////////////////////////////////////////
    private static final String Iamge_Url = "http://image.tmdb.org/t/p/w185";
    private static ArrayList<Movies> Results=new ArrayList<>();
    private static PagesState Pages;
    /////////////////////////////////
    private static final String Review_Author = "author";
    private static final String Review_Content = "content";
    private static String Author;
    private static String Content;
    ////////////////////
    private static final String Videos_Name = "name";
    private static final String Videos_Key = "key";
    private static String Video_Name;
    private static String Video_Key;
    private static String Video_Iamge;
    private static String Video_Link;
    ////////////////////
    private static final String FavouriteValue = "unFavurite";


    public static ArrayList<Movies> parseMoviesJson(String Json ) {
        ArrayList<Movies> retuner=null;
        try {
            JSONObject json_Movies = new JSONObject(Json);
            JSONArray jsonArray=json_Movies.getJSONArray(Resuls);
            Results=GetListFromJsonArray(jsonArray);
            return Results;
        } catch (JSONException e) {
            Log.e("JSONException: ", e.getMessage().toString());
        }
        return null;
    }





    public static PagesState MakePageState(String s){
        try {
            JSONObject PageState_Json = new JSONObject(s);
            page_Value=PageState_Json.getString(page);
            total_results_Value=PageState_Json.getString(total_results);
            total_pages_Value=PageState_Json.getString(total_pages);
            Pages=new PagesState(page_Value,total_results_Value,total_pages_Value);
            return Pages;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MakePosterUrl(String s) {
        // add poster url to full link
        StringBuilder builder = new StringBuilder();
        builder.append(Iamge_Url);
        builder.append(s);
        return builder.toString();
    }

    public static ArrayList<Movies> GetListFromJsonArray(JSONArray array) {
        ArrayList<Movies> Return_List = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject json_Movie = new JSONObject(array.get(i).toString());
                title_Value = json_Movie.getString(title);
                release_date_Value = json_Movie.getString(release_date);
                poster_path_Value = MakePosterUrl(json_Movie.getString(poster_path));
                vote_Value = json_Movie.getString(vote_average);
                Plot_synopsis_Value = json_Movie.getString(Plot_synopsis);
                id_Value= json_Movie.getString(id);
                //////////////////////////Put Valuse////
                Movies movie = new Movies(title_Value, release_date_Value, poster_path_Value, vote_Value, Plot_synopsis_Value,id_Value);
                Return_List.add(movie);

            } catch (JSONException e) {
                Log.e("JSONException: ", e.getMessage().toString());
            }
        }

        return Return_List;
    }

/////////ReView Parsing
    public static ArrayList<Reviews> parseReviewsJson(String Json ) {
        try {
            JSONObject json_Movies = new JSONObject(Json);
            JSONArray jsonArray=json_Movies.getJSONArray(Resuls);
            return GetListFromJsonArrayReviews(jsonArray);
        } catch (JSONException e) {
            Log.e("JSONException: ", e.getMessage().toString());
        }
        return null;
    }
    public static ArrayList<Reviews> GetListFromJsonArrayReviews(JSONArray array) {
        ArrayList<Reviews> Return_List = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject json_Review = new JSONObject(array.get(i).toString());
                Log.i("LOOGer",json_Review.toString());
                Author = json_Review.getString(Review_Author);
                Content = json_Review.getString(Review_Content);
                //////////////////////////Put Valuse////
                Reviews review = new Reviews(Author,Content);
                Return_List.add(review);

            } catch (JSONException e) {
                Log.e("JSONException: ", e.getMessage().toString());
            }
        }
        return Return_List;

    }


    /////////     Video Parsing
    public static String GetVideoLinks(String Key){
        String retunner="";
        StringBuilder builder =new StringBuilder();
        if(Key!=null || Key.equals("")){
            if(Key!=null || Key.equals("")){
                builder =new StringBuilder();
                builder.append(Constats.YouTube_Link);
                builder.append(Key);
                retunner=builder.toString();
            }
        }
        return retunner;
    }
    public static String GetVideoIamges(String Key){
        String retunner="";
        StringBuilder builder =new StringBuilder();
            if(Key!=null || Key.equals("")){
                builder =new StringBuilder();
                builder.append(Constats.YouTube_ImageLink);
                builder.append(Key);
                builder.append(Constats.YouTube_ImageLinkComplete);
                retunner=   builder.toString();
            }

        return retunner;
    }




    public static ArrayList<Videos> parseVideoJson(String Json ) {
        try {
            JSONObject json_Movies = new JSONObject(Json);
            JSONArray jsonArray=json_Movies.getJSONArray(Resuls);
            return GetListFromJsonArrayVideos(jsonArray);
        } catch (JSONException e) {
            Log.e("JSONException: ", e.getMessage().toString());
        }
        return null;
    }
    public static ArrayList<Videos> GetListFromJsonArrayVideos(JSONArray array) {
        ArrayList<Videos> Return_List = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject json_Review = new JSONObject(array.get(i).toString());
                Video_Name = json_Review.getString(Videos_Name);
                Video_Key = json_Review.getString(Videos_Key);
                Video_Link=GetVideoLinks(Video_Key);
                Video_Iamge=GetVideoIamges(Video_Key);
                //////////////////////////Put Valuse////


                Videos viodes = new Videos(Video_Key,Video_Name,Video_Iamge,Video_Link);
                Log.e("VideoName: ", viodes.getTrailer_name());
                Return_List.add(viodes);

            } catch (JSONException e) {
                Log.e("JSONException: ", e.getMessage().toString());
            }
        }
        return Return_List;

    }

}
