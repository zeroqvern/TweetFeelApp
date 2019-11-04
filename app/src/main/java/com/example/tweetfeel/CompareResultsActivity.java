package com.example.tweetfeel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tweetfeel.support.ApiClient;
import com.example.tweetfeel.support.ApiService;
import com.example.tweetfeel.support.userDataModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CompareResultsActivity extends AppCompatActivity {

    private TextView keywordsTitle;
    private TextView tweetsNum;

    private ImageView sentiRing_1;
    private ImageView sentiRing_2;

    private TextView displaySenti_1;
    private TextView displaySenti_2;

    private TextView posResult_1;
    private TextView posResult_2;
    private TextView negResult_1;
    private TextView negResult_2;
    private TextView overall_1;
    private TextView overall_2;

    private Button viewBtn;
    private Button detailsBtn;
    private Button newBtn;

    private int pos_1;
    private int neg_1;
    private double overallSenti_1;

    private int pos_2;
    private int neg_2;
    private double overallSenti_2;

    private SharedPreferences pref;

    public String keyword_1;
    public String keyword_2;

    @SuppressLint("SetTextI18n")
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_vs);

        keyword_1 = getIntent().getStringExtra("keyword_1");
        keyword_2 = getIntent().getStringExtra("keyword_2");
        String title = keyword_1 + " vs " + keyword_2;
        keywordsTitle = findViewById(R.id.keywordTitle);
        keywordsTitle.setText(title);


        updateKeyword();

        pos_1 = getIntent().getIntExtra("pos_1", -1);
        neg_1 = getIntent().getIntExtra("neg_1", -1);
        overallSenti_1= getIntent().getDoubleExtra("overall_1", -1.0);

        pos_2 = getIntent().getIntExtra("pos_2", -1);
        neg_2 = getIntent().getIntExtra("neg_2", -1);
        overallSenti_2= getIntent().getDoubleExtra("overall_2", -1.0);



        sentiRing_1 = findViewById(R.id.sentiRing_1);
        sentiRing_2 = findViewById(R.id.sentiRing_2);
        displaySenti_1 = findViewById(R.id.displaySenti_1);
        displaySenti_2 = findViewById(R.id.displaySenti_2);

        //change color accordingly
        if(overallSenti_1 > 0.5) {
            GradientDrawable ring = (GradientDrawable) sentiRing_1.getBackground();
            ring.setStroke(50,getResources().getColor(R.color.colorAccent));
            displaySenti_1.setTextColor(getColor(R.color.colorAccent));
        }
        else {
            //if result less than 0.6, ring will be red
            GradientDrawable ring = (GradientDrawable) sentiRing_1.getBackground();
            ring.setStroke(50,getResources().getColor(R.color.colorPrimary));
            displaySenti_1.setTextColor(getColor(R.color.colorPrimary));
        }

        //
        if(overallSenti_2 > 0.5) {
            GradientDrawable ring = (GradientDrawable) sentiRing_2.getBackground();
            ring.setStroke(50,getResources().getColor(R.color.colorAccent));
            displaySenti_2.setTextColor(getColor(R.color.colorAccent));
        }
        else {
            //if result less than 0.6, ring will be red
            GradientDrawable ring = (GradientDrawable) sentiRing_2.getBackground();
            ring.setStroke(50,getResources().getColor(R.color.colorPrimary));
            displaySenti_2.setTextColor(getColor(R.color.colorPrimary));

        }

        displaySenti_1.setText(overallSenti_1 + "");
        displaySenti_2.setText(overallSenti_2 + "");


        posResult_1 = findViewById(R.id.posResult_1);
        posResult_1.setText(pos_1 + "");
        negResult_1 = findViewById(R.id.negResult_1);
        negResult_1.setText(neg_1 + "");
        overall_1 = findViewById(R.id.totalResult_1);
        overall_1.setText("100");

        posResult_2 = findViewById(R.id.posResult_2);
        posResult_2.setText(pos_2 + "");
        negResult_2 = findViewById(R.id.negResult_2);
        negResult_2.setText(neg_2 + "");
        overall_2 = findViewById(R.id.totalResult_2);
        overall_2.setText("100");

        viewBtn = findViewById(R.id.viewBtn);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(CompareResultsActivity.this, ChooseKeywordActivity.class);
                intent.putExtra("keyword_1", keyword_1);
                intent.putExtra("keyword_2", keyword_2);
                startActivity(intent);
            }
        });

        detailsBtn = findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompareResultsActivity.this, ResultsDetailsActivity.class);
                intent.putExtra("compare", 1);
                intent.putExtra("pos1", pos_1);
                intent.putExtra("neg1", neg_1);

                intent.putExtra("pos2", pos_2);
                intent.putExtra("neg2", neg_2);

                intent.putExtra("key1", keyword_1);
                intent.putExtra("key2", keyword_2);
                startActivity(intent);
            }
        });

        newBtn = findViewById(R.id.newBtn);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompareResultsActivity.this, NewAnalysisActivity.class));
            }
        });

    }


    public void updateKeyword()
    {

        //update user's keyword
        pref = getSharedPreferences("userDataModel",  Context.MODE_PRIVATE);

        String id = pref.getString("id", "000000000000000000000000");
        Set<String> set = pref.getStringSet("keywords", null);
        ArrayList<String> keys = new ArrayList<>();
        keys = new ArrayList<>(set);
        keys.add(keyword_1);
        keys.add(keyword_2);


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
