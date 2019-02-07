package edu.ucsd.cse110.googlefitapp;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDateTime;

public class WalkRun {
    //variables used to make conversions and calculate distance
    private final static double strideMultiplier = .413;
    private final static int inchesToFeet = 12;
    private final static int feetToMiles = 5280;

    LocalDateTime startTime;
    LocalDateTime endTime;

    int startSteps;
    int endSteps;

    int height;

    boolean started = false;
    boolean ok = false;

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
        //every start must be met with an end
        if(!started) {
            startTime = LocalDateTime.now();
            startSteps = initSteps;

            started = true;
            ok = false;
        }
        else {
            throw new Exception("Invalid: this WalkRun has already started");
        }
    }

    /* End the walk/run */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void endWalkRun(int finalSteps) throws Exception {
        //can only end WalkRun that has already started
        if(started) {
            //cannot decrease the amount of steps taken on a walk
            if(endSteps > startSteps) {
                endTime = LocalDateTime.now();
                endSteps = finalSteps;
                started = false;
                ok = true;
            }
            else {
                throw new Exception("Invalid: number of steps DECREASED on walk/run");
            }
        }
        else {
            throw new Exception("Invalid: attempt to end WalkRun before starting");
        }
    }

    /* Return the number of steps taken on this walk/run */
    public int getNumSteps() throws Exception {
        if(ok) {
            return endSteps - startSteps;
        }
        else {
            throw new Exception("Invalid: WalkRun has not been completed");
        }
    }

    /* Return the distance walked in miles */
    public double getDistance() throws Exception {
        if(ok) {
            double stride = height * strideMultiplier;
            double distanceFeet = (stride * getNumSteps()) / inchesToFeet;
            double distanceMiles = distanceFeet / feetToMiles;
            return distanceMiles;
        }
        else {
            throw new Exception("Invalid: WalkRun has not been completed");
        }
    }

    public double getSpeed() {
        return 0.0;
    }

    public int minutesWalked() {
        return 0;
    }
}
