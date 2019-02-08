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
    private boolean started = false;
    private boolean ok = false;

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
            throw new Exception("Invalid: cannot get number of steps of incomplete WalkRun");
        }
    }

    /* Return the distance walked in miles */
    public double getDistance() throws Exception {
        if(ok) {
            double stride = height * strideMultiplier;
            double distanceFeet = stride * getNumSteps() / inchesInFeet;
            double distanceMiles = distanceFeet / feetInMile;
            return distanceMiles;
        }
        else {
            throw new Exception("Invalid: cannot get distance of incomplete WalkRun");
        }
    }

    /* Get speed of WalkRun in miles per hour */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public double getSpeed() throws Exception {
        if(ok) {
            double hours = secondsWalked() / secondsInHour;
            double mph = getDistance() / hours;
            return mph;
        }
        else {
            throw new Exception("Invalid: cannot get duration of incomplete WalkRun");
        }
    }

    /* Get duration of WalkRun in seconds */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long secondsWalked() throws Exception {
        if(ok) {
            walkRunTime = Duration.between(startTime, endTime).getSeconds();

            if(walkRunTime > 0) {
                return walkRunTime;
            }
            else {
                throw new Exception("Invalid: WalkRun end time before WalkRun start time");
            }
        }
        else {
            throw new Exception("Invalid: cannot get duration of incomplete WalkRun");
        }
    }

    /* Return an AlertDialog containing the steps, duration, speed, and distance of this WalkRun */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public AlertDialog getStats(Context context) throws Exception {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Duration: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds\n"
        + "Number of steps: " + steps
        + "Speed: " + mph + " mph\n"
        + "Distance: " + distance + " miles");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        return alert;
    }
}
