package com.example.tweetfeel.support;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class tweetModel implements Parcelable {

    @SerializedName("name")
    @Expose
    private String username;
    @SerializedName("text")
    @Expose
    private String content;
    @SerializedName("created at")
    @Expose
    private String dateTime;
    @SerializedName("sentiment")
    @Expose
    private int sentiment;

    public String getUsername() {
        return username;
    }
    public String getContent() {
        return content;
    }
    public String getDateTime() {
        return dateTime;
    }
    public int getSentiment() {
        return sentiment;
    }

    @Override
    public int describeContents (){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.username);
        dest.writeString(this.content);
        dest.writeString(this.dateTime);
        dest.writeInt(this.sentiment);
    }

    protected tweetModel (Parcel in) {
        this.username = in.readString();
        this.content = in.readString();
        this.dateTime = in.readString();
        this.sentiment = in.readInt();
    }

    public static final Parcelable.Creator<tweetModel> CREATOR
            = new Parcelable.Creator<tweetModel>() {
        public tweetModel createFromParcel(Parcel source) {
            return new tweetModel(source);
        }

        public tweetModel[] newArray(int size){
            return new tweetModel[size];
        }
    };

}
