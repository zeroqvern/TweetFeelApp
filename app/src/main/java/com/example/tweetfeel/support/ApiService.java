package com.example.tweetfeel.support;


import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {


    @GET("Login")
    Single<userDataModel> login(@Query("email") String email, @Query("pass") String password);
//    Single <analysisModel> getAnalysis(@Query("keyword") String keyword);

    @POST("Register")
    Single<userDataModel> register(@Query("username") String username, @Query("email") String email,
                                   @Query("pass") String password);


    @GET("GetTweetDetails")
    Single<userDataModel> getTweetDetails(@Query("id") String userid, @Query("keyword") String keyword);

    @GET("analysis")
    Single<analysisModel> getAnalysis(@Query("keyword") String keyword, @Query("userid") String userid);

    @POST("UpdateKeywords")
    Single<userDataModel> updateKeywords(@Query("keywords") ArrayList<String> keywords, @Query("id") String userid);


}