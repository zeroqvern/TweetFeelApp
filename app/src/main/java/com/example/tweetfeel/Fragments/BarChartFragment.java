package com.example.tweetfeel.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tweetfeel.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class BarChartFragment extends Fragment {

    public BarChartFragment() {

    }


    private BarChart barChart;
    private Button shotBtn;
    private LinearLayout header;
    private LinearLayout background;
    private LinearLayout layoutHide;
    private boolean single = false;
    private boolean compare = false;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bar_chart,container,false);
        header = getActivity().findViewById(R.id.header);
        background = getActivity().findViewById(R.id.background);
        layoutHide = getActivity().findViewById(R.id.layoutHide);

        int compareK = getArguments().getInt("compare");
        if(compareK == 0)
            single = true;
        else
            compare = true;

//        single = true;


        barChart = view.findViewById(R.id.barChart);

        barSetup();

        shotBtn = view.findViewById(R.id.shotBtn);
        shotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"You may screenshot now, press back to go back",
                                Toast.LENGTH_LONG).show();
                header.setVisibility(View.GONE);
                layoutHide.setVisibility(View.VISIBLE);

            }
        });
        return view;


    }

    private void barSetup()
    {
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        ArrayList<BarEntry> barEntries2 = new ArrayList<>();

        float barSpace = 0;
        float groupSpace = 0;
        float barWidth = 0;
        String[] tweet;

        if(single)
        {
            int pos = getArguments().getInt("pos", -1);
            int neg = getArguments().getInt("neg", -1);
            Log.e("pos", pos+"");

            barEntries1.add(new BarEntry(1, pos));
            barEntries2.add(new BarEntry(1, neg));


            barSpace = 0.5f;
            groupSpace = 0.5f;
            barWidth = 0.5f;

            barChart.getXAxis().setAxisMinimum(0);
            barChart.getXAxis().setAxisMaximum(3);
            tweet = new String[]{"","tweets"};

        }
        else
        {
            int pos1 = getArguments().getInt("pos1", -1);
            int pos2 = getArguments().getInt("pos2", -1);
            int neg1 = getArguments().getInt("neg1", -1);
            int neg2 = getArguments().getInt("neg2", -1);

            barEntries1.add(new BarEntry(1,pos1));
            barEntries1.add(new BarEntry(2,pos2));
            barEntries2.add(new BarEntry(1,neg1));
            barEntries2.add(new BarEntry(2,neg2));

            barSpace = 0.1f;
            groupSpace = 0.4f;
            barWidth = 0.2f;


            barChart.getXAxis().setAxisMinimum(0);
            barChart.getXAxis().setAxisMaximum(2);

            String key1 = getArguments().getString("key1", "");
            String key2 = getArguments().getString("key2", "");

            tweet = new String[]{ key1, key2};

        }

        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Positive");
        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Negative");

        int green = getResources().getColor(R.color.green);
        int red = getResources().getColor(R.color.red);
        int blue = getResources().getColor(R.color.colorAccent);

        barDataSet1.setColors(green);
        barDataSet2.setColor(red);

        BarData data = new BarData(barDataSet1, barDataSet2);
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(tweet));
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisLineWidth(2f);
        xAxis.setAxisLineColor(blue);

        YAxis yAxisR = barChart.getAxisRight();
        yAxisR.setEnabled(false);

        YAxis yAxisL = barChart.getAxisLeft();
        yAxisL.setAxisLineWidth(2f);
        yAxisL.setAxisLineColor(blue);
        yAxisL.setGridColor(blue);
        barChart.animateY(1000);

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        barChart.setDragEnabled(false);

        data.setBarWidth(barWidth);


        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setAxisMaximum(100);

        barChart.groupBars(0,groupSpace,barSpace);

        barChart.invalidate();
    }
}
