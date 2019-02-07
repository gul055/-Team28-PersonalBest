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

    boolean started = false;

    /* Initialize a walk/run */
    public WalkRun(int userHeight) throws Exception {
        if(userHeight > 0) {
            height = userHeight;
        }
        else {
            throw new Exception("Invalid height");
        }
    }

    /* Start the walk/run */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startWalkRun(int initSteps) throws Exception {
        if(!started) {
            startTime = LocalDateTime.now();
            startSteps = initSteps;

            started = true;
        }
        else {
            throw new Exception("Invalid: this WalkRun has already started");
        }
    }

    /* End the walk/run */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void endWalkRun(int finalSteps) throws Exception {
        if(started) {
            endTime = LocalDateTime.now();
            endSteps = finalSteps;
            started = false;
        }
        else {
            throw new Exception("Invalid: attempt to end WalkRun before starting");
        }
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

    public double getSpeed() {
        return 0.0;
    }

    public int minutesWalked() {
        return 0;
    }
}
