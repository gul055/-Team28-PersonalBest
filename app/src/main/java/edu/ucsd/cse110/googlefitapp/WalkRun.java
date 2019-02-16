package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static android.content.Context.MODE_PRIVATE;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WalkRun {
    //variables used to make conversions and calculate distance
    private final static double strideMultiplier = .413;
    private final static int inchesInFeet = 12;
    private final static int feetInMile = 5280;
    private final static int secondsInHour = 3600;
    private final static int secondsInMinute = 60;

    SharedPreferences sharedPref;

    LocalDateTime refTime = LocalDateTime.of(2016, Month.JANUARY, 1, 0, 0, 0);

    /* Initialize a walk/run */
    public WalkRun(Context context, int userHeight) throws Exception {
        //negative or 0 height does not make sense
        if (userHeight > 0) {
            sharedPref = context.getSharedPreferences("walkrun_data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("height", userHeight);
            editor.putBoolean("ok", false);
            editor.putBoolean("started", false);
            editor.apply();

        } else {
            throw new Exception("Invalid height");
        }
    }

    /* Start the walk/run */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startWalkRun(int initSteps) throws Exception {
        //every start must be met with an end
        if (sharedPref.getBoolean("started", false)) {
            SharedPreferences.Editor editor = sharedPref.edit();

            //initial step count must be valid
            if (initSteps >= 0) {
                editor.putInt("startSteps", initSteps);
                editor.putLong("startTime", Duration.between(refTime, LocalDateTime.now()).getSeconds());
                editor.putBoolean("started", true);
                editor.putBoolean("ok", false);
                editor.apply();
            } else {
                throw new Exception("Invalid: negative initial step count");
            }
        } else {
            throw new Exception("Invalid: this WalkRun has already started");
        }
    }

    /* End the walk/run */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void endWalkRun(int finalSteps) throws Exception {
        //can only end WalkRun that has already started
        if (sharedPref.getBoolean("started", false)) {
            if (finalSteps < 0) {
                throw new Exception("Invalid: negative final step count");
            }
            //cannot decrease the amount of steps taken on a walk
            if (finalSteps >= sharedPref.getInt("startSteps", Integer.MAX_VALUE)) {

                long start = sharedPref.getLong("startTime", 0);
                long end = Duration.between(refTime, LocalDateTime.now()).getSeconds();

                //cannot end walk at a time before it is started
                if (end - start < 0) {
                    throw new Exception("Invalid: End time < start time");
                }
                else {
                    SharedPreferences.Editor editor = sharedPref.edit();

                    //update the WalkRun
                    editor.putInt("endSteps", finalSteps);
                    editor.putLong("endTime", end);
                    editor.putBoolean("ok", true);

                    editor.apply();

                    reset();
                }
            } else {
                throw new Exception("Invalid: Steps DECREASED on WalkRun.\n");
            }
        } else {
            throw new Exception("Invalid: attempt to end WalkRun before starting: sharedPref boolean is " + sharedPref.getBoolean("started", false));
        }
    }

    /* Check the statistics so far of this WalkRun */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String checkProgress(int pSteps) throws Exception {
        if (!sharedPref.getBoolean("started", false)) {
            if (!sharedPref.getBoolean("ok", false)) {
                SharedPreferences.Editor editor = sharedPref.edit();

                //temporarily make it ok to get stats
                editor.putBoolean("ok", true);
                long end = Duration.between(refTime, LocalDateTime.now()).getSeconds();

                //update WalkRun prefs with the progress time and steps
                editor.putLong("endTime", end);
                editor.putInt("endSteps", pSteps);
                editor.apply();

                String progress = getStats();

                //WalkRun is still incomplete
                editor.putBoolean("ok", false);
                editor.putBoolean("started", true);
                editor.apply();

                //Progress return string with stats
                String progressHeader = "Walk/Run progress:\n";
                return progressHeader + progress;
            } else {
                throw new Exception("No WalkRun in progress");
            }
        } else {
            throw new Exception("No WalkRun in progress");
        }
    }

    /* Return an String containing the steps, duration, speed, and distance of this WalkRun */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStats() throws Exception {
        if (sharedPref.getBoolean("ok", false)) {
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

            reset();

            return ("Duration: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds\n"
                    + "Number of steps: " + steps
                    + "Speed: " + mph + " mph\n"
                    + "Distance: " + distance + " miles");
        } else {
            throw new Exception("Invalid: This instance of WalkRun has started but not stopped.");
        }
    }

    /* Return the number of steps taken on this walk/run */
    public int getNumSteps() throws Exception {
        if (sharedPref.getBoolean("ok", false)) {
            return (sharedPref.getInt("endSteps", -1) - sharedPref.getInt("startSteps", Integer.MAX_VALUE));
        }
        else {
            throw new Exception("Invalid: Cannot getNumSteps for incomplete WalkRun");
        }
    }

    /* Return the distance walked in miles, rounded to one decimal place */
    public double getDistance() throws Exception {
        if (sharedPref.getBoolean("ok", false)) {
            double stride = sharedPref.getInt("height", -1) * strideMultiplier;
            double distanceFeet = stride * getNumSteps() / inchesInFeet;
            double distanceMiles = distanceFeet / feetInMile;
            return Math.round(distanceMiles * 10) / 10.0;
        }
        else {
            throw new Exception("Invalid: Cannot getDistance for incomplete WalkRun");
        }
    }

    /* Get speed of WalkRun in miles per hour, rounded to one decimal place */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public double getSpeed() throws Exception {
        double hours = (double) secondsWalked() / secondsInHour;
        double mph = getDistance() / hours;
        return Math.round(mph * 10) / 10.0;
    }

    /* Get duration of WalkRun in seconds */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long secondsWalked() throws Exception {
        long diff = sharedPref.getLong("endTime", -1) - sharedPref.getLong("startTime", 0);

        if (diff > 0) {
            return diff;
        } else {
            throw new Exception("Invalid: WalkRun end time before WalkRun start time");
        }
    }

    /* Called to reset WalkRun preferences, ready to go on another walk/run */
    public void reset() {
        SharedPreferences.Editor editor = sharedPref.edit();

        //ready to start new WalkRun
        editor.putBoolean("started", false);

        editor.apply();
    }
}