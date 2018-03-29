package com.example.user.moveappstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 27/02/2018.
 */

public class Movies implements Parcelable {

    private String Title;
    private String Relase_Date;
    private String Movie_Poster;
    private String Vote_average;
    private String Plot_synopsis;
    private String ID;

    public Movies(String title, String relase_Date, String movie_Poster, String vote_average, String plot_synopsis, String ID) {
        Title = title;
        Relase_Date = relase_Date;
        Movie_Poster = movie_Poster;
        Vote_average = vote_average;
        Plot_synopsis = plot_synopsis;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getRelase_Date() {
        return Relase_Date;
    }

    public void setRelase_Date(String relase_Date) {
        Relase_Date = relase_Date;
    }

    public String getMovie_Poster() {
        return Movie_Poster;
    }

    public void setMovie_Poster(String movie_Poster) {
        Movie_Poster = movie_Poster;
    }

    public String getVote_average() {
        return Vote_average;
    }

    public void setVote_average(String vote_average) {
        Vote_average = vote_average;
    }

    public String getPlot_synopsis() {
        return Plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        Plot_synopsis = plot_synopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Title);
        parcel.writeString(Relase_Date);
        parcel.writeString(Movie_Poster);
        parcel.writeString(Vote_average);
        parcel.writeString(Plot_synopsis);
        parcel.writeString(ID);

    }

    private Movies(Parcel in) {
        Title = in.readString();
        Relase_Date = in.readString();
        Movie_Poster = in.readString();
        Vote_average = in.readString();
        Plot_synopsis = in.readString();
        ID = in.readString();

    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

}
