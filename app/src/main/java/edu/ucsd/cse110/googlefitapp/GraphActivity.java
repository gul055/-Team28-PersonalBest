package edu.ucsd.cse110.googlefitapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

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

        AbstractCalendar calendar = new LockedCalendar();

        BarChart chart = findViewById(R.id.chart);

        // This is using fake data right now
        DataHandler dataHandler = new DataHandler();

        BarData data = new BarData(dataHandler.getStepSet(), dataHandler.getGoalSet());
        chart.setData(data);

        // Bunch of initializations of graph
        data.setBarWidth(Constants.BAR_WIDTH);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(Constants.GRANULARITY);
        xAxis.setValueFormatter(new DateAxisValueFormatter(calendar));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(chart.getBarData().getGroupWidth(Constants.GROUP_SPACE, Constants.BAR_SPACE) * Constants.WEEK_SIZE);
        xAxis.setCenterAxisLabels(true);
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.groupBars(0f, Constants.GROUP_SPACE, Constants.BAR_SPACE);
        chart.invalidate();
    }
}
