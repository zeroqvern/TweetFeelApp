package com.example.tweetfeel.support;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;

public class userDataModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("keywords")
    @Expose
    private ArrayList<String> keywords;
    @SerializedName("status")
    private String status;

    @SerializedName("tweets details")
    @Expose
    private ArrayList<tweetModel> tweetList;


    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public ArrayList<String> getKeywords() {
        return keywords;
    }
    public String getStatus() {
        return status;
    }
    public ArrayList<tweetModel> getTweetList() {
        return tweetList;
    }

}