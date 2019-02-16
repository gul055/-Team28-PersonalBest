package edu.ucsd.cse110.googlefitapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    // https://github.com/PhilJay/MPAndroidChart/wiki/Getting-Started

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Insert code for retrieving data here
        // Each data point should be a set of floats

        BarChart chart = findViewById(R.id.chart);
        DataHandler dataHandler = new DataHandler();

        BarData data = new BarData(dataHandler.getStepSet(), dataHandler.getGoalSet());
        FitCalendar calendar = new FitCalendar();
        data.setBarWidth(Constants.BAR_WIDTH);
        chart.setData(data);
        chart.groupBars(calendar.getDayOfMonth(), Constants.GROUP_SPACE, Constants.BAR_SPACE);
        chart.invalidate();
    }
}
