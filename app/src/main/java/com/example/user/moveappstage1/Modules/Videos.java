package com.example.user.moveappstage1.Modules;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 08/03/2018.
 */

public class Videos implements Parcelable {
    private String Key;
    private String Trailer_name;
    private String VideoIamge;
    private String VideoLink;

    public Videos(String key, String trailer_name, String videoIamge, String videoLink) {
        Key = key;
        Trailer_name = trailer_name;
        VideoIamge = videoIamge;
        VideoLink = videoLink;
    }

    private Videos(Parcel in) {
        Key = in.readString();
        Trailer_name = in.readString();
        VideoIamge = in.readString();
        VideoLink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Key);
        dest.writeString(Trailer_name);
        dest.writeString(VideoIamge);
        dest.writeString(VideoLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Videos> CREATOR = new Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel in) {
            return new Videos(in);
        }

        @Override
        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getTrailer_name() {
        return Trailer_name;
    }

    public void setTrailer_name(String trailer_name) {
        Trailer_name = trailer_name;
    }

    public String getVideoIamge() {
        return VideoIamge;
    }

    public void setVideoIamge(String videoIamge) {
        VideoIamge = videoIamge;
    }

    public String getVideoLink() {
        return VideoLink;
    }

    public void setVideoLink(String videoLink) {
        VideoLink = videoLink;
    }
}
