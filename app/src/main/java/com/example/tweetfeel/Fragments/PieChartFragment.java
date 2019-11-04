package com.example.tweetfeel.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tweetfeel.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class PieChartFragment extends Fragment {

    public PieChartFragment() {

    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private PieChart pieChart;
    private PieChart pieChart2;
    private Button shotBtn;
    private LinearLayout header;
    private LinearLayout background;
    private LinearLayout layoutHide;

    private int pos;
    private int neg;

    private int pos2 = -1;
    private int neg2 = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pie_chart, container, false);

        header = getActivity().findViewById(R.id.header);
        background = getActivity().findViewById(R.id.background);
        layoutHide = getActivity().findViewById(R.id.layoutHide);

        int compare = getArguments().getInt("compare");

        if(compare == 0) {
            pos = getArguments().getInt("pos", -1);
            neg = getArguments().getInt("neg", -1);
        }
        else
        {
            pos = getArguments().getInt("pos1",-1);
            neg = getArguments().getInt("neg1",-1);
            pos2 = getArguments().getInt("pos2",-1);
            neg2 = getArguments().getInt("neg2",-1);
        }


        pieChart = view.findViewById(R.id.pieChart);
        pieSetup(pos, neg, pieChart);
        if(pos2 != -1)
        {
            pieChart2 = view.findViewById(R.id.pieChart2);
            pieSetup(pos2,neg2, pieChart2);
        }

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


    private void pieSetup(int pos, int neg, PieChart pieChart)
    {

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);

        ArrayList <PieEntry> values = new ArrayList<>();

        values.add(new PieEntry(pos,"Positive"));
        values.add(new PieEntry(neg,"Negative"));

        PieDataSet dataSet = new PieDataSet(values, "tweets");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        int green = getResources().getColor(R.color.green);
        int red = getResources().getColor(R.color.red);

        dataSet.setColors(green,red);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);

    }
}
