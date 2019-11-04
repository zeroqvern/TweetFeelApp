package com.example.tweetfeel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        SharedPreferences pref = getSharedPreferences("userDataModel",  Context.MODE_PRIVATE);

        String name = pref.getString("username", "username");
        String e = pref.getString("email", "email");
        Set<String> keys = pref.getStringSet("keywords", null);
        int num = 0;
        if(keys != null)
        {
            num = keys.size();
        }

        TextView username = findViewById(R.id.username);
        TextView email = findViewById(R.id.email);
        TextView keywordsNum = findViewById(R.id.keywordNum);

        username.setText(name);
        email.setText(e);
        keywordsNum.setText(num + "");

        Button changePassBtn = findViewById(R.id.changePassBtn);

    }
}
