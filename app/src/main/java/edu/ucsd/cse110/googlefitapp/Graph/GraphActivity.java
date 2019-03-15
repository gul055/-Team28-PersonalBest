package edu.ucsd.cse110.googlefitapp.Graph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Calendars.CalendarAdapter;
import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.R;

public class GraphActivity extends AppCompatActivity {

    // https://github.com/PhilJay/MPAndroidChart/wiki/Getting-Started
    BarChart chart;
    AbstractCalendar calendar;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Log.i("GraphActivity", "Entering graph");

        userEmail = getIntent().getStringExtra(Constants.GRAPH_USER);

        Button back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());

        //AbstractCalendar calendar = new LockedCalendar();
        calendar = new CalendarAdapter();
        chart = findViewById(R.id.chart);

        //String[] weekString = calendar.getWeek(Constants.WITH_YEAR);
        refreshGraph();

        Button last7 = findViewById(R.id.last7);
        Button next7 = findViewById(R.id.next7);
        last7.setOnClickListener(v -> {
            calendar.rewindOneWeek();
            refreshGraph();
        });
        next7.setOnClickListener(v -> {
            calendar.addOneWeek();
            refreshGraph();
        });
    }

    public void refreshGraph() {

        Log.i("GraphActivity", "Building graph");

        String[] weekString = calendar.getLast7Days(Constants.WITH_YEAR);
        DataGetter getter = new DataGetter(getApplicationContext());
        float[][] stepDataArray = getter.get2DArrayData(weekString, userEmail, Constants.INTENTIONAL, Constants.TOTAL_STEPS_TAG);
        float[] goalArray = getter.getArrayData(weekString, userEmail, Constants.GOAL);

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
