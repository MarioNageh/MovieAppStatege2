package com.example.user.moveappstage1.Modules;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 08/03/2018.
 */

public class Reviews implements Parcelable {
    private String Author;
    private String Content;

    public Reviews(String author, String content) {
        Author = author;
        Content = content;
    }

    protected Reviews(Parcel in) {
        Author = in.readString();
        Content = in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Author);
        parcel.writeString(Content);
    }
}
