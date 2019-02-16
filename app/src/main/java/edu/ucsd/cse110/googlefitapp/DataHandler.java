package edu.ucsd.cse110.googlefitapp;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private List<BarEntry> steps;
    private List<BarEntry> goals;
    private BarDataSet stepSet;
    private BarDataSet goalSet;
    private static float[][] defaultSteps = {{5000f,500f},{5000f, 1000f},{3000f, 4000f},{0f, 7000f},{3000f, 1000f},{6500f, 1000f},{7000f, 600f}};
    private static float[] defaultGoals = {5000f,5000f,5000f,5500f,5500f,6000f,6000f};

    public DataHandler(float[][] stepData, float[] goalData){
        steps = new ArrayList<>();
        goals = new ArrayList<>();

        for(int i = 0; i < stepData.length; i++){
            steps.add(new BarEntry(i, stepData[i]));
            goals.add(new BarEntry(i, goalData[i]));
        }

        packToSet();
    }

    public DataHandler(){
        this(defaultSteps, defaultGoals);
    }

    private void packToSet(){
        stepSet = new BarDataSet(steps, "Steps");
        goalSet = new BarDataSet(goals, "Goals");
    }

    public BarDataSet getStepSet(){
        return stepSet;
    }

    public BarDataSet getGoalSet(){
        return goalSet;
    }

    // https://github.com/PhilJay/MPAndroidChart/wiki/Getting-Started
}
