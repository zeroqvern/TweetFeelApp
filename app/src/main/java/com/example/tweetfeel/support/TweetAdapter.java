package com.example.tweetfeel.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tweetfeel.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetHolder> {

    private ArrayList<tweetModel> tweetData;
    private Context context;


    public class TweetHolder extends RecyclerView.ViewHolder{
        public TextView userName, content, dateTime;
        public LinearLayout bg;

        public TweetHolder(@NonNull View view) {

            super(view);
            userName = view.findViewById(R.id.username);
            content = view.findViewById(R.id.tweetContent);
            dateTime = view.findViewById(R.id.dateTime);
            bg = view.findViewById(R.id.bg);

        }
    }


    public TweetAdapter(ArrayList<tweetModel> data, Context context) {
        this.context = context;
        this.tweetData = data;
    }

    @NonNull
    @Override
    public TweetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet, parent, false);

        return new TweetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetHolder holder, int position) {
        tweetModel singleTweet = tweetData.get(position);
        holder.userName.setText(singleTweet.getUsername());
        holder.content.setText(singleTweet.getContent());
        holder.dateTime.setText(singleTweet.getDateTime().toString());

        int sentiment = singleTweet.getSentiment();
        if(sentiment == 0)
            holder.bg.setBackgroundColor(context.getColor(R.color.red));
        else
            holder.bg.setBackgroundColor(context.getColor(R.color.green));

    }

    @Override
    public int getItemCount() {
        return tweetData.size();
    }


}