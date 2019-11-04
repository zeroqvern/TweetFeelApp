package com.example.tweetfeel;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class test extends AppCompatActivity {

    private Button exportBtn;
    private BarChart barChart;
    private PieChart pieChart;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_graph );

        exportBtn = findViewById(R.id.exportBtn);
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);

        barSetup();
        pieSetup();

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(test.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                    }
                    else
                    {
                        saveImage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });



    }

    private void saveImage()
    {
        LinearLayout Blayout = findViewById(R.id.barLayout);



//        int measureWidth = View.MeasureSpec.makeMeasureSpec(Blayout.getWidth(), View.MeasureSpec.EXACTLY);
//        int measureHeight = View.MeasureSpec.makeMeasureSpec(Blayout.getHeight(), View.MeasureSpec.EXACTLY);
//
//        Blayout.measure(measureWidth,measureHeight);
//        Blayout.layout(0,0, Blayout.getMeasuredWidth(), Blayout.getMeasuredHeight());
//
        Bitmap bm = Bitmap.createBitmap(1080, 350,
                    Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
//        Blayout.layout(Blayout.getLeft(),Blayout.getTop(),
//                Blayout.getRight(), Blayout.getBottom());
        Blayout.layout(0,0, Blayout.getLayoutParams().width,Blayout.getLayoutParams().height);
        Log.e("msgg", Blayout.getLayoutParams().width + " " + Blayout.getLayoutParams().height);
        Log.e("msgg", Blayout.getWidth() + " " + Blayout.getHeight());
        Log.e("msgg", Blayout.getMeasuredWidth() + " " + Blayout.getMeasuredHeight());
        Blayout.draw(c);

        FileOutputStream fos = null;

        String mPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toString() + "/barchart.png";
        Context context = test.this;

        ImageView imageView = findViewById(R.id.image);
        if(bm == null)
        {
            Log.e("msg", "null");
        }
        else {
            Log.e("msg", bm.toString());

        }
        imageView.setImageBitmap(bm);



//        try {
//            File f = new File(mPath);
//            f.createNewFile();
//
//            fos = new FileOutputStream(f, true);
//            fos = context.openFileOutput("barchart.png", Context.MODE_PRIVATE);
//            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            fos.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//        }


    }


    private void pieSetup()
    {

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);

        ArrayList <PieEntry> values = new ArrayList<>();

        values.add(new PieEntry(80,"Positive"));
        values.add(new PieEntry(20,"Negative"));

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

    private void barSetup()
    {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 80));
//        barEntries.add(new BarEntry(2, 20));

        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        barEntries1.add(new BarEntry(1,20));

//         String[] sentiment = new String[]{"Positive", "Negative"};

        BarDataSet barDataSet = new BarDataSet(barEntries, "Positive");
        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Negative");

        int green = getResources().getColor(R.color.green);
        int red = getResources().getColor(R.color.red);
        int blue = getResources().getColor(R.color.colorAccent);

        barDataSet.setColors(green);
        barDataSet1.setColor(red);

        BarData data = new BarData(barDataSet, barDataSet1);
        barChart.setData(data);

        String[] tweet = new String[]{" ","Tweets"};
        String[] sentiment1 = new String[]{"Negative"};

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

        float barSpace = 0.5f;
        float groupSpace = 0.5f;
//        data.setBarWidth(1f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(3);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setAxisMaximum(100);

        barChart.groupBars(0,groupSpace,barSpace);

        barChart.invalidate();
    }

}
