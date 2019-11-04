package com.example.tweetfeel.support;

import com.google.gson.annotations.SerializedName;

public class analysisModel {
    @SerializedName("Positive")
    private int posTweets;

    @SerializedName("Negative")
    private int negTweets;

    @SerializedName("Overall Sentiment")
    private double OverallSentiment;


    public int getPosTweets() {
        return posTweets;
    }

    public int getNegTweets() {
        return negTweets;
    }

    public double getOverallSentiment() {
        return OverallSentiment;
    }
}
