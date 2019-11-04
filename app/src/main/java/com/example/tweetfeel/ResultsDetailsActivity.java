package com.example.tweetfeel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.tweetfeel.Fragments.BarChartFragment;
import com.example.tweetfeel.Fragments.PieChartFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ResultsDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout layoutHide;
    private LinearLayout header;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_details);




        viewPager = findViewById(R.id.viewPager);
        layoutHide = findViewById(R.id.layoutHide);
        header = findViewById(R.id.header);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.getTabAt(0).setText(R.string.bar);
        tabLayout.getTabAt(1).setText(R.string.pie);


//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                tabLayout.setBackgroundColor(getColor(R.color.bright));
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
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
            Bundle bundle = new Bundle();
            int compare = getIntent().getIntExtra("compare", -1);

            if(compare == 0)
            {
                Log.e("msg", "passing single bundle");

                int pos = getIntent().getIntExtra("pos",-1);
                int neg = getIntent().getIntExtra("neg", -1);

                bundle.putInt("pos", pos);
                bundle.putInt("neg", neg);
                bundle.putInt("compare", compare);

                switch (position) {
                    case 0:
                        BarChartFragment barChartFragment = new BarChartFragment();
                        barChartFragment.setArguments(bundle);
                        return barChartFragment;
                    case 1:
                        PieChartFragment pieChartFragment = new PieChartFragment();
                        pieChartFragment.setArguments(bundle);
                        return pieChartFragment;

                    default:
                        return null;
                }
            }
            else
            {
                Log.e("msg", "passing compare bundle");
                int pos1 = getIntent().getIntExtra("pos1",-1);
                int neg1 = getIntent().getIntExtra("neg1", -1);
                int pos2 = getIntent().getIntExtra("pos2",-1);
                int neg2 = getIntent().getIntExtra("neg2", -1);

                String key1 = getIntent().getStringExtra("key1");
                String key2 = getIntent().getStringExtra("key2");


                bundle.putInt("pos1", pos1);
                bundle.putInt("neg1", neg1);

                bundle.putInt("pos2", pos2);
                bundle.putInt("neg2", neg2);

                bundle.putString("key1", key1);
                bundle.putString("key2", key2);
                bundle.putInt("compare", compare);


                switch (position) {
                    case 0:
                        BarChartFragment barChartFragment = new BarChartFragment();
                        barChartFragment.setArguments(bundle);
                        return barChartFragment;
                    case 1:
                        PieChartFragment pieChartFragment = new PieChartFragment();
                        pieChartFragment.setArguments(bundle);
                        return pieChartFragment;

                    default:
                        return null;
                }
            }



        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }



    @Override
    public void onBackPressed() {
        if (layoutHide.getVisibility() == View.VISIBLE) {
            layoutHide.setVisibility(View.INVISIBLE);
            header.setVisibility(View.VISIBLE);
            return;
        }
        else
        {
            this.finish();
        }


    }


}
