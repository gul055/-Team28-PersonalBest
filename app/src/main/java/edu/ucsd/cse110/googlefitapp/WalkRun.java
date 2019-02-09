package edu.ucsd.cse110.googlefitapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;

public class WalkRun {
    //variables used to make conversions and calculate distance
    private final static double strideMultiplier = .413;
    private final static int inchesInFeet = 12;
    private final static int feetInMile = 5280;
    private final static int secondsInHour = 3600;
    private final static int secondsInMinute = 60;

    //boolean checks for valid WalkRun method calls
    boolean started = false;
    boolean ok = false;

    LocalDateTime startTime;
    LocalDateTime endTime;

    long walkRunTime;

    int startSteps;
    int endSteps;

    int height;

    /* Initialize a walk/run */
    public WalkRun(int userHeight) throws Exception {
        //negative or 0 height does not make sense
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
            if(initSteps >= 0) {
                startSteps = initSteps;
            }
            else {
                throw new Exception("Invalid: negative initial step count");
            }

            startTime = LocalDateTime.now();
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
            if(finalSteps < 0) {
                throw new Exception("Invalid: negative final step count");
            }
            //cannot decrease the amount of steps taken on a walk
            if(finalSteps >= startSteps) {
                endTime = LocalDateTime.now();
                endSteps = finalSteps;
                started = false;
                ok = true;
            }
            else {
                throw new Exception("Invalid: Steps DECREASED on WalkRun.\n" +
                        "Start steps: " + startSteps + ". End steps: " + endSteps);
            }
        }
        else {
            throw new Exception("Invalid: attempt to end WalkRun before starting");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String checkProgress(Context context) throws Exception {
        if(started) {
            ok = true;
            endTime = LocalDateTime.now();
            String progress = getStats(context);
            ok = false;
            return progress;
        }
        else {
            throw new Exception("Cannot check progress on a run that hasn't started");
        }
    }

    /* Return an String containing the steps, duration, speed, and distance of this WalkRun */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStats(Context context) throws Exception {
        if(ok) {
            //duration
            long seconds = secondsWalked();

            int hours = (int) seconds / secondsInHour;
            seconds = seconds - (hours * secondsInHour);

            int minutes = (int) seconds / secondsInMinute;

            seconds = seconds - (minutes * secondsInMinute);

            //number of steps
            int steps = getNumSteps();

            //speed
            double mph = getSpeed();

            //distance
            double distance = getDistance();

            return("Duration: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds\n"
                    + "Number of steps: " + steps
                    + "Speed: " + mph + " mph\n"
                    + "Distance: " + distance + " miles");
        }
        else {
            throw new Exception("Invalid: This instance of WalkRun has started but not stopped.");
        }
    }

    /* Return the number of steps taken on this walk/run */
    public int getNumSteps() throws Exception {
        return endSteps - startSteps;
    }

    /* Return the distance walked in miles */
    public double getDistance() throws Exception {
        double stride = height * strideMultiplier;
        double distanceFeet = stride * getNumSteps() / inchesInFeet;
        double distanceMiles = distanceFeet / feetInMile;
        return distanceMiles;
    }

    /* Get speed of WalkRun in miles per hour */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public double getSpeed() throws Exception {
        double hours = secondsWalked() / secondsInHour;
        double mph = getDistance() / hours;
        return mph;
    }

    /* Get duration of WalkRun in seconds */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long secondsWalked() throws Exception {
        walkRunTime = Duration.between(startTime, endTime).getSeconds();

        if(walkRunTime > 0) {
            return walkRunTime;
        }
        else {
            throw new Exception("Invalid: WalkRun end time before WalkRun start time");
        }
    }
}
