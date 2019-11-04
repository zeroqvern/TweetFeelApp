package com.example.tweetfeel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tweetfeel.support.ApiClient;
import com.example.tweetfeel.support.ApiService;
import com.example.tweetfeel.support.tweetModel;
import com.example.tweetfeel.support.userDataModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SingleResultActivity extends AppCompatActivity {

    private TextView keywordTitle;
    private TextView tweetNum;

    private TextView displaySenti;
    private ImageView sentiRing;

    private TextView posResult;
    private TextView negResult;
    private TextView total;

    private Button viewBtn;
    private Button detailsBtn;
    private Button newBtn;

    private int pos;
    private int neg;
    private double overallSenti;
    private String keyword;

    private SharedPreferences pref;



    @SuppressLint("SetTextI18n")
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_single);



        pos = getIntent().getIntExtra("pos",-1);
        neg = getIntent().getIntExtra("neg", -1);
        overallSenti = getIntent().getDoubleExtra("overall", 0);
        keyword = getIntent().getStringExtra("keyword");


        keywordTitle = findViewById(R.id.keywordTitle);
        keywordTitle.setText(keyword);

        updateKeyword();

        tweetNum = findViewById(R.id.totalNum);
        String num = "100";
        tweetNum.setText(num);

        displaySenti = findViewById(R.id.displaySenti);
        displaySenti.setText(overallSenti + "");

        sentiRing = findViewById(R.id.sentiRing);

        if(overallSenti > 0.5) {
            GradientDrawable ring = (GradientDrawable) sentiRing.getBackground();
            ring.setStroke(50,getResources().getColor(R.color.colorAccent));
            displaySenti.setTextColor(getColor(R.color.colorAccent));
        }
        else {
            //if result less than 0.6, ring will be red
            GradientDrawable ring = (GradientDrawable) sentiRing.getBackground();
            ring.setStroke(50,getResources().getColor(R.color.colorPrimary));
            displaySenti.setTextColor(getColor(R.color.colorPrimary));

        }

        posResult = findViewById(R.id.posResult);
        posResult.setText(pos + "");

        negResult = findViewById(R.id.negResult);
        negResult.setText(neg + "");

        total = findViewById(R.id.totalNum);
        total.setText("100");

        viewBtn = findViewById(R.id.viewBtn);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(SingleResultActivity.this, TweetsActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            }
        });

        detailsBtn = findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleResultActivity.this, ResultsDetailsActivity.class);
                intent.putExtra("compare", 0);
                intent.putExtra("pos", pos);
                intent.putExtra("neg", neg);
                startActivity(intent);
            }
        });

        newBtn = findViewById(R.id.newBtn);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pop up window asking whether to go back or not
                startActivity(new Intent(SingleResultActivity.this, NewAnalysisActivity.class));
            }
        });


    }


    public void updateKeyword()
    {

        //update user's keyword
        pref = getSharedPreferences("userDataModel",  Context.MODE_PRIVATE);

        String id = pref.getString("id", "000000000000000000000000");
        Set<String> set = pref.getStringSet("keywords", null);
        ArrayList<String> keys;

        keys = new ArrayList<>(set);
        keys.add(keyword);

        Log.e("Updating", "keywords");

        Retrofit retrofit = new ApiClient().base();

        ApiService apiService = retrofit.create(ApiService.class);
        Single<userDataModel> user = apiService.updateKeywords(keys, id);
        user.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<userDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(userDataModel u) {
                        Log.e("update", "updating keywords");

                        String statusCode = u.getStatus();
                        Log.e("status", statusCode);


                        if (statusCode.equals("001")) {
                            Log.e("Update", "success!");


                        } else if (statusCode.equals("002")) {


                            Log.e("Update", "failed!");

                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Update", e.toString());

                    }
                });


        SharedPreferences.Editor editor = pref.edit();
        editor.remove("keywords");
        Set<String> keyset = new HashSet<>(keys);
        editor.putStringSet("keywords", keyset);
        editor.commit();


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Please click BACK again to go back to Home", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }

}
