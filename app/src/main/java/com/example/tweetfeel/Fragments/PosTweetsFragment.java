package com.example.tweetfeel.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tweetfeel.R;
import com.example.tweetfeel.support.TweetAdapter;
import com.example.tweetfeel.support.tweetModel;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PosTweetsFragment extends Fragment {

    public PosTweetsFragment() {

    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    private ArrayList<tweetModel> tweetData = new ArrayList<>();
    private ArrayList<tweetModel> posData = new ArrayList<>();
    private RecyclerView recyclerView;
    private TweetAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tweet_fragment, container, false);


        Log.e("msg", "pos");

        tweetData = getArguments().getParcelableArrayList("data");


        for (tweetModel t : tweetData)
        {
            if(t.getSentiment() == 1)
            {
                posData.add(t);
            }
        }

        recyclerView = view.findViewById(R.id.tweetList);
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView ()
    {
        adapter = new TweetAdapter(posData, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setClickable(false);
        recyclerView.setAdapter(adapter);
    }
}
