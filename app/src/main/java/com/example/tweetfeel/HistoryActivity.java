package com.example.tweetfeel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tweetfeel.support.KeywordAdapter;
import com.example.tweetfeel.support.TweetAdapter;
import com.example.tweetfeel.support.tweetModel;

import java.util.ArrayList;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity extends AppCompatActivity {
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        pref = getSharedPreferences("userDataModel",  Context.MODE_PRIVATE);
        String id = pref.getString("id", "000000000000000000000000");


        Set<String> set = pref.getStringSet("keywords", null);
        ArrayList<String> keys = new ArrayList<>();


        if(set!= null)
        {

            keys = new ArrayList<>(set);

            if(keys.size() == 0)
            {
                keys.add("");
            }

        }
        else {

            Log.e("string set","null");
        }

        KeywordAdapter keywordAdapter = new KeywordAdapter(keys,id,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.keywordsList);;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setClickable(false);
        recyclerView.setAdapter(keywordAdapter);
    }


}

