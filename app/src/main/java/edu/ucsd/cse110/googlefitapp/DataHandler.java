package edu.ucsd.cse110.googlefitapp;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class StepsGraph {

    private List<BarEntry> steps;
    private List<BarEntry> goals;
    private static float[][] defaultSteps = {{5000f,500f},{5000f, 1000f},{3000f, 4000f},{0f, 7000f},{3000f, 1000f},{6500f, 1000f},{7000f, 600f}};
    private static float[] defaultGoals = {5000f,5000f,5000f,5500f,5500f,6000f,6000f};

    public StepsGraph(float[][] stepData, float[] goalData){
        steps = new ArrayList<>();
        goals = new ArrayList<>();

        for(int i = 0; i < stepData.length; i++){
            steps.add(new BarEntry(i, stepData[i]));
            goals.add(new BarEntry(i, goalData[i]));
        }
    }

    public StepsGraph(){
        this(defaultSteps, defaultGoals);
    }

    // https://github.com/PhilJay/MPAndroidChart/wiki/Getting-Started
}
