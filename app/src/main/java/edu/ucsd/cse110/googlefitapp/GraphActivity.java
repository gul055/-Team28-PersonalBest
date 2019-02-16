package edu.ucsd.cse110.googlefitapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
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

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Insert code for retrieving data here
        // Each data point should be a set of floats

        FitCalendar calendar = new FitCalendar();
        int firstDayOfWeek = calendar.getFirstDayOfWeek();

        BarChart chart = findViewById(R.id.chart);
        DataHandler dataHandler = new DataHandler(firstDayOfWeek);

        BarData data = new BarData(dataHandler.getStepSet(), dataHandler.getGoalSet());
        // This is using fake data right now

        // Bunch of initializations of graph
        data.setBarWidth(Constants.BAR_WIDTH);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        chart.groupBars(firstDayOfWeek, Constants.GROUP_SPACE, Constants.BAR_SPACE);
        chart.invalidate();
    }
}
