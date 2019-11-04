package com.example.tweetfeel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseKeywordActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_keyword);

        final String keyword_1 = getIntent().getStringExtra("keyword_1");
        final String keyword_2 = getIntent().getStringExtra("keyword_2");

        Button keyword_1btn = findViewById(R.id.keyword_1btn);
        keyword_1btn.setText(keyword_1);

        Button keyword_2btn = findViewById(R.id.keyword_2btn);
        keyword_2btn.setText(keyword_2);

        keyword_1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ChooseKeywordActivity.this, TweetsActivity.class);
                intent.putExtra("keyword", keyword_1);
                startActivity(intent);
            }
        });

        keyword_2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ChooseKeywordActivity.this, TweetsActivity.class);
                intent.putExtra("keyword", keyword_2);
                startActivity(intent);
            }
        });


    }
}
