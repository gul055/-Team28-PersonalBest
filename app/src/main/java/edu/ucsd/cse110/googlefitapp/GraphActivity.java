package edu.ucsd.cse110.googlefitapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Calendars.LockedCalendar;
import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.Graph.DataGetter;
import edu.ucsd.cse110.googlefitapp.Graph.DataHandler;
import edu.ucsd.cse110.googlefitapp.Graph.DateAxisValueFormatter;
import edu.ucsd.cse110.googlefitapp.R;

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

        AbstractCalendar calendar = new LockedCalendar();
        String[] weekString = calendar.getWeek(Constants.WITH_YEAR);

        BarChart chart = findViewById(R.id.chart);

        DataGetter getter = new DataGetter(getApplicationContext());
        float[][] stepDataArray = getter.get2DArrayData(weekString, Constants.INTENTIONAL, Constants.TOTAL_STEPS_TAG);
        float[] goalArray = getter.getArrayData(weekString, Constants.GOAL);

        // This is using fake data right now
        DataHandler dataHandler = new DataHandler(stepDataArray, goalArray);

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
