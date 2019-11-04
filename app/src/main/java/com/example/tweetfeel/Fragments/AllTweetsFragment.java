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

public class AllTweetsFragment extends Fragment {

    public AllTweetsFragment() {

    }


    private ArrayList<tweetModel> tweetData = new ArrayList<>();
    private RecyclerView recyclerView;
    private TweetAdapter adapter;

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tweet_fragment, container, false);
        Log.e("msg", "all");

        tweetData = getArguments().getParcelableArrayList("data");

        recyclerView = view.findViewById(R.id.tweetList);
        setupRecyclerView();




        return view;
    }

    private void setupRecyclerView ()
    {
        adapter = new TweetAdapter(tweetData, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setClickable(false);
        recyclerView.setAdapter(adapter);
    }
}
