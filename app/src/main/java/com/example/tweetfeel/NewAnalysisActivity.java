package com.example.tweetfeel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tweetfeel.support.ApiClient;
import com.example.tweetfeel.support.ApiService;
import com.example.tweetfeel.support.analysisModel;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewAnalysisActivity extends AppCompatActivity {

    private EditText input;
    private Button compareBtn;
    private EditText comInput;

    private Button cancelBtn;
    private Button okBtn;

    private SharedPreferences pref;

    private LinearLayout keywordLayout;
    private LinearLayout loadingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_keyword);

        pref = getSharedPreferences("userDataModel",  Context.MODE_PRIVATE);

        keywordLayout = findViewById(R.id.keywordLayout);
        loadingLayout = findViewById(R.id.loadingLayout);


        input = findViewById(R.id.keyword);
        comInput = findViewById(R.id.comKeyword);

        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewAnalysisActivity.this, HomeActivity.class));
            }
        });


        compareBtn = findViewById(R.id.compareBtn);
        compareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comInput.setVisibility(View.VISIBLE);
            }
        });

        okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new ApiClient().NBbase();
                ApiService apiService = retrofit.create(ApiService.class);

                final String keyword = input.getText().toString();
                final String comKeyword = comInput.getText().toString();
                String id = pref.getString("id", null);

                Log.e("Processing", "Getting response from API");
                Log.e("Processing", id);

                final boolean[] analysisCheck = {false, false};
                View focusView = null;

                if(!TextUtils.isEmpty(keyword))
                //do work
                {
                    keywordLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.VISIBLE);

                    //do prediction

                    if(comKeyword == null|| comKeyword.isEmpty()) {
                        Single<analysisModel> analysis = apiService.getAnalysis(keyword,id);
                        analysis.observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new SingleObserver<analysisModel>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(analysisModel a) {
                                        Intent intent = new Intent(
                                                NewAnalysisActivity.this, SingleResultActivity.class);

                                        Toast.makeText(getApplicationContext(),
                                                "Displaying results",
                                                Toast.LENGTH_SHORT).show();

                                        //go to activity
                                        intent.putExtra("keyword", keyword);
                                        intent.putExtra("pos", a.getPosTweets());
                                        intent.putExtra("neg", a.getNegTweets());
                                        intent.putExtra("overall", a.getOverallSentiment());

                                        Log.e("msg", a.getOverallSentiment() + "");
                                        Log.e("msg", a.getPosTweets() + "");
                                        Log.e("msg", a.getNegTweets() + "");

                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("error:", e.toString());
                                        Toast.makeText(getApplicationContext(),"Error. Check connection.", Toast.LENGTH_SHORT).show();

                                        //go to activity
                                        startActivity(new Intent(NewAnalysisActivity.this, HomeActivity.class));

                                    }
                                });
                    }
                    else
                    {
                        final Intent intent = new Intent(NewAnalysisActivity.this, CompareResultsActivity.class);

                        Single<analysisModel> firstAnalysis = apiService.getAnalysis(keyword,id);
                        firstAnalysis.observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new SingleObserver<analysisModel>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(analysisModel a) {
                                        analysisCheck[0] = true;
                                        intent.putExtra("keyword_1", keyword);
                                        intent.putExtra("pos_1", a.getPosTweets());
                                        intent.putExtra("neg_1", a.getNegTweets());
                                        intent.putExtra("overall_1", a.getOverallSentiment());

                                        Log.e("firstAnalysis", "pass " + a.getOverallSentiment());
                                        if(analysisCheck[1])
                                        {
                                            Toast.makeText(getApplicationContext(),"Displaying results", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }
                                        Log.e("FirstAnalysis", a.getPosTweets() + " " + a.getNegTweets());


                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        analysisCheck[0] = false;
                                        Log.e("Error", e.toString());
                                        Toast.makeText(getApplicationContext(),"Error. Check connection.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                        Single<analysisModel> secondAnalysis = apiService.getAnalysis(comKeyword,id);
                        secondAnalysis.observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new SingleObserver<analysisModel>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(analysisModel a) {
                                        analysisCheck[1] = true;
                                        intent.putExtra("keyword_2", comKeyword);
                                        intent.putExtra("pos_2", a.getPosTweets());
                                        intent.putExtra("neg_2", a.getNegTweets());
                                        intent.putExtra("overall_2", a.getOverallSentiment());

                                        if(analysisCheck[0])
                                        {
                                            Toast.makeText(getApplicationContext(),"Displaying results", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }

                                        Log.e("SecondAnalysis", a.getPosTweets() + " " + a.getNegTweets());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        analysisCheck[1] = false;
                                        Log.e("Error", e.toString());
                                        Toast.makeText(getApplicationContext(),"Error. Check connection.", Toast.LENGTH_SHORT).show();

                                    }
                                });



                    }
                }
                else
                {
                    input.setError("Required field");
                    focusView = input;
                    focusView.requestFocus();
                }
            }
        });


    }

    private boolean checkAnalysis (boolean[] analysisCheck)
    {
        for(boolean check : analysisCheck)
        {
            if (!check)
            {
                return false;
            }
        }
        return true;
    }
}
