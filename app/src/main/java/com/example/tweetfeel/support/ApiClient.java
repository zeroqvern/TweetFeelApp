package com.example.tweetfeel.support;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    private static final String NBBASE_URL = "https://nbmodel-api.herokuapp.com/";
    private static final String BASE_URL = "https://userdata-api.herokuapp.com/";


    public static Retrofit base(){
//        if(retrofit == null)
//        {
            Log.e("message", "in retro");
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
//        }

        return retrofit;

    }

    public static Retrofit NBbase() {
//        if(retrofit == null)
//        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(240, TimeUnit.SECONDS)
                    .readTimeout(240, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(NBBASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
//        }

        return retrofit;
    }


}

