package com.example.user.moveappstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 28/02/2018.
 */

public class PagesState implements Parcelable {
    private String page;
    private String total_results;
    private String total_pages;

    public PagesState(String page, String total_results, String total_pages) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public PagesState() {

    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal_results() {
        return total_results;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(page);
        parcel.writeString(total_results);
        parcel.writeString(total_pages);
    }

    private PagesState(Parcel in) {
        page = in.readString();
        total_results = in.readString();
        total_pages = in.readString();
    }

    public static final Creator<PagesState> CREATOR = new Creator<PagesState>() {
        @Override
        public PagesState createFromParcel(Parcel in) {
            return new PagesState(in);
        }

        @Override
        public PagesState[] newArray(int size) {
            return new PagesState[size];
        }
    };
}
