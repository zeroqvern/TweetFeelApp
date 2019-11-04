package com.example.tweetfeel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.tweetfeel.Fragments.AllTweetsFragment;
import com.example.tweetfeel.Fragments.NegTweetsFragment;
import com.example.tweetfeel.Fragments.PosTweetsFragment;
import com.example.tweetfeel.support.ApiClient;
import com.example.tweetfeel.support.ApiService;
import com.example.tweetfeel.support.tweetModel;
import com.example.tweetfeel.support.userDataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TweetsActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL= 1;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TabItem tabAll;
    private TabItem tabPos;
    private TabItem tabNeg;
    private ViewPager viewPager;

    private Bundle bundle;
    private SharedPreferences pref;

    private String id;
    private String keyword;

    private LinearLayout loading;
    private TextView loadingText;

    private FloatingActionButton fab;
    public static final String CHANNEL_1_ID = "channel1";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    private ArrayList<tweetModel> tweetModels;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweets_details);

        pref = getSharedPreferences("userDataModel", Context.MODE_PRIVATE);
        id = pref.getString("id", "000000000000000000000000");

        keyword = getIntent().getStringExtra("keyword");

        Log.e("message", id + " " + keyword);
//        id = "5dadec848d3277e8e3a266d4";
//        keyword = "Mcdonald";

        viewPager = findViewById(R.id.viewPager);

        tabLayout = findViewById(R.id.tabLayout);
        tabAll = findViewById(R.id.allTweets);
        tabPos = findViewById(R.id.posTweets);
        tabNeg = findViewById(R.id.negTweets);
        tabLayout.setupWithViewPager(viewPager);
        loading = findViewById(R.id.loadingLayout);

        loadingText = findViewById(R.id.loadingText);
        String text = "Retrieving tweets data...";
        loadingText.setText(text);

        Retrofit retrofit = ApiClient.base();

        ApiService apiService = retrofit.create(ApiService.class);
        Single<userDataModel> tweetDetails = apiService.getTweetDetails(id, keyword);
        tweetDetails.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<userDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(userDataModel t) {
                        ArrayList<tweetModel> data = t.getTweetList();
                        tweetModels = t.getTweetList();
                        Log.e("message", data.size() + "");
                        bundle = new Bundle();
                        bundle.putParcelableArrayList("data", data);

                        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                        viewPager.setAdapter(pageAdapter);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        viewPager.setOffscreenPageLimit(1);
                        tabLayout.getTabAt(0).setText(R.string.allTitle);
                        tabLayout.getTabAt(1).setText(R.string.posTitle);
                        tabLayout.getTabAt(2).setText(R.string.negTitle);

                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", e.toString());
                    }
                });



        fab = findViewById(R.id.exportFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification();

                //export to csv
                try {
                    if (ActivityCompat.checkSelfPermission(TweetsActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(TweetsActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_EXTERNAL);
                    }
                    else
                    {
                        exportDataToCSV();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notificationBuilder.setContentText("Export completed")
                        .setProgress(0,0,false);
                notificationManager.notify(1,notificationBuilder.build());
            }
        });

    }

    public class PageAdapter extends FragmentPagerAdapter {


        private int numOfTabs;

        public PageAdapter (FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    AllTweetsFragment allTweetsFragment = new AllTweetsFragment();
                    allTweetsFragment.setArguments(bundle);
                    return allTweetsFragment;
                case 1:
                    PosTweetsFragment posTweetsFragment = new PosTweetsFragment();
                    posTweetsFragment.setArguments(bundle);
                    return posTweetsFragment;
                case 2:
                    NegTweetsFragment negTweetsFragment = new NegTweetsFragment();
                    negTweetsFragment.setArguments(bundle);
                    return negTweetsFragment;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    exportDataToCSV();
                }
                break;
        }
    }

    public void exportDataToCSV(){
        File folder = new File
                (Environment.getExternalStorageDirectory()
                        + "/TweetFeel");

        boolean var = false;
        if(!folder.exists())
            var = folder.mkdir();
        Log.e("export", var + "");

        String ext = ".csv";
        String fileName = "/TweetsRawData_" + keyword;
        final String filePath = folder.toString() + fileName + ext;
        File file = new File(filePath);

        for (int i = 1; file.exists(); i++)
        {
            file = new File(folder.toString() + fileName + "(" + i + ")" + ext);
        }

        CSVWriter writer = null;
        try{
            writer = new CSVWriter(new FileWriter(file));

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"KEYWORD:", keyword});
            data.add(new String[]{"username", "dateTime", "tweet", "sentiment"});
            for (tweetModel tweet : tweetModels) {
                data.add(new String[] {
                        tweet.getUsername(),
                        tweet.getDateTime(),
                        tweet.getContent(),
                        tweet.getSentiment() +""
                });
            }

            //add to csv file
            writer.writeAll(data);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void showNotification(){
//        File folder = new File(Environment.getExternalStorageDirectory() + "/TweetFeel");
//        File folder = new File(Environment.getExternalStorageDirectory().getPath());
//        Uri path = Uri.parse(folder.getPath());

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(path, "*/TweetsRawData.csv");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, 0);

        String heading = "Export";
        String description = "Exporting file...";
        notificationBuilder = new NotificationCompat.Builder(this,"channelID")
                .setSmallIcon(R.drawable.ic_export)
                .setContentTitle(heading)
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setProgress(0,0,true);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        createChannel();
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public void createChannel(){
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationChannel channel = new NotificationChannel
                ("channelID","name",
                        NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Description");
        notificationManager.createNotificationChannel(channel);
    }

}
