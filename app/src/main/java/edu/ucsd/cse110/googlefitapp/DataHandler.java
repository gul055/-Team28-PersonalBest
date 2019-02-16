package edu.ucsd.cse110.googlefitapp;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private List<BarEntry> steps;
    private List<BarEntry> goals;
    private BarDataSet stepSet;
    private BarDataSet goalSet;
    private static float[][] defaultSteps = {{5000f,500f},{5000f, 1000f},{3000f, 4000f},{0f, 7000f},{3000f, 1000f},{6500f, 1000f},{7000f, 600f}};
    private static float[] defaultGoals = {5000f,5000f,5000f,5500f,5500f,6000f,6000f};
    private static int[] colors = {Color.BLUE, Color.RED};
    private static String[] stepLabels = {Constants.RECORDED_STEP, Constants.INCIDENTAL_STEP};

    public DataHandler(float[][] stepData, float[] goalData, int startDate){
        steps = new ArrayList<>();
        goals = new ArrayList<>();

        for(int i = 0; i < stepData.length; i++){
            steps.add(new BarEntry(startDate + i, stepData[i]));
            goals.add(new BarEntry(startDate + i, goalData[i]));
        }

        packToSet();
    }

    public DataHandler(int startDate){
        this(defaultSteps, defaultGoals, startDate);
    }

    private void packToSet(){
        stepSet = new BarDataSet(steps, "");
        goalSet = new BarDataSet(goals, Constants.GOAL_LABEL);
        stepSet.setStackLabels(stepLabels);
        stepSet.setColors(colors);
        goalSet.setColor(Color.GREEN);
    }

    public BarDataSet getStepSet(){
        return stepSet;
    }

    public BarDataSet getGoalSet(){
        return goalSet;
    }

    // https://github.com/PhilJay/MPAndroidChart/wiki/Getting-Started
}
