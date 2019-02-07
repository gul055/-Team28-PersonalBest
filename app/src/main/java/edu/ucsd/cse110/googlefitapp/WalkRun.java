package edu.ucsd.cse110.googlefitapp;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDateTime;

public class WalkRun {
    final static double strideMultipilier = .413;
    final static int inchesToFeet = 12;
    final static int feetToMiles = 5280;

    LocalDateTime startTime;
    LocalDateTime endTime;

    int startSteps;
    int endSteps;

    int height;

    /* Initialize a walk/run */
    public WalkRun(int userHeight) {
        height = userHeight;
    }

    /* Start the walk/run */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startWalkRun(int initSteps) {
        startTime = LocalDateTime.now();
        startSteps = initSteps;
    }

    /* End the walk/run */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void endWalkRun(int finalSteps) {
        endTime = LocalDateTime.now();
        endSteps = finalSteps;
    }

    /* Return the number of steps taken on this walk/run */
    public int getNumSteps() {
        return endSteps - startSteps;
    }

    /* Return the distance walked in miles */
    public double getDistance() {
        double stride = height * strideMultipilier;
        double distanceFeet = (stride * getNumSteps()) / inchesToFeet;
        double distanceMiles = distanceFeet / feetToMiles;
        return distanceMiles;
    }
}
