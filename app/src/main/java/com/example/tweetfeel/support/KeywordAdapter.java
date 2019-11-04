package com.example.tweetfeel.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tweetfeel.HistoryActivity;
import com.example.tweetfeel.R;
import com.example.tweetfeel.SingleResultActivity;
import com.example.tweetfeel.TweetsActivity;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.KeywordHolder> {

    private ArrayList<String> keywords;
    private Context context;
    private String id;


    public class KeywordHolder extends RecyclerView.ViewHolder{
        public TextView keyString;
        public Button detailsBtn;
        public ProgressBar cpb;

        public KeywordHolder(@NonNull View view) {

            super(view);
            keyString = view.findViewById(R.id.keywordName);
            detailsBtn = view.findViewById(R.id.showDetailsBtn);
            cpb = view.findViewById(R.id.cpb);

        }
    }


    public KeywordAdapter(ArrayList<String> data, String id, Context context) {
        Log.e("size", "in adpater1");

        this.context = context;
        this.keywords = data;
        this.id = id;
    }

    @NonNull
    @Override
    public KeywordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        Log.e("size", "in adpater");

        if (keywords.size() != 0)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.keyword, parent, false);

            if(keywords.get(0).equals(""))
            {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.no_keyword, parent, false);
            }
        }
        else
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.no_keyword, parent, false);
        }

        return new KeywordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KeywordHolder holder, int position) {
        Log.e("size", "in adpater2");

        final String key = keywords.get(position);
        if(!key.equals("")) {
            holder.keyString.setText(key);
            final KeywordHolder h = holder;
            holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.cpb.setVisibility(View.VISIBLE);
                    holder.detailsBtn.setVisibility(View.GONE);
                    Retrofit retrofit = ApiClient.base();

                    ApiService apiService = retrofit.create(ApiService.class);
                    Single<userDataModel> tweetDetails = apiService.getTweetDetails(id, key);
                    tweetDetails.observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new SingleObserver<userDataModel>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(userDataModel t) {
                                    ArrayList<tweetModel> data = t.getTweetList();
                                    Log.e("message", data.size() + "");
                                    Intent intent = new Intent(context, SingleResultActivity.class);


                                    int pos = 0;
                                    int neg = 0;

                                    for (tweetModel tm : data) {
                                        if (tm.getSentiment() == 0)
                                            neg++;
                                        else
                                            pos++;
                                    }

                                    double avg = getAverage(pos, neg);

                                    intent.putExtra("pos", pos);
                                    intent.putExtra("neg", neg);
                                    intent.putExtra("overall", avg);
                                    intent.putExtra("keyword", key);

                                    context.startActivity(intent);

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("Error", e.toString());
                                }
                            });
                }
            });
        }

    }


    public double getAverage(int pos, int neg)
    {
        int all = pos + neg;
        if (all == 0)
            return 0;
        double avg = (double)pos / (double)all;

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        avg = Math.round(avg);
        Log.e("avg", avg + "");
        return avg;
    }
    @Override
    public int getItemCount() {
        return keywords.size();
    }


}