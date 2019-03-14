package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WalkRun {
    //variables used to make conversions and calculate distance
    private final static double strideMultiplier = .413;
    private final static int inchesInFeet = 12;
    private final static int feetInMile = 5280;
    private final static int secondsInHour = 3600;
    private final static int secondsInMinute = 60;

    SharedPreferences sharedPref;
    Context context;
    LocalDateTime refTime = LocalDateTime.of(2016, Month.JANUARY, 1, 0, 0, 0);

    /* Initialize a walk/run */
    public WalkRun(Context context, int userHeight) throws Exception {
        this.context = context;
        //negative or 0 height does not make sense
        if (userHeight > 0) {
            SharedPreferencesUtil.saveInt(context, Constants.HEIGHT_TAG, userHeight);
            SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, false);
            SharedPreferencesUtil.saveBoolean(context, Constants.STARTED_TAG, false);

        } else {
            throw new Exception("Invalid height");
        }
    }

    /* Start the walk/run */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startWalkRun(int initSteps) throws Exception {
        //every start must be met with an end
        boolean isStarted = SharedPreferencesUtil.loadBoolean(context, Constants.STARTED_TAG);
        if (!isStarted) {
            //TODO:REMOVE AFTER TESTS
            //SharedPreferences.Editor editor = sharedPref.edit();

            //initial step count must be valid
            if (initSteps >= 0) {
                Log.d("INITSTEPS_STARTWALK", String.valueOf(initSteps));
                SharedPreferencesUtil.saveInt(context, Constants.STARTSTEPS_TAG, initSteps);
                SharedPreferencesUtil.saveLong(context, Constants.STARTTIME_TAG, Duration.between(refTime, LocalDateTime.now()).getSeconds());
                SharedPreferencesUtil.saveBoolean(context, Constants.STARTED_TAG, true);
                SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, false);

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
        boolean isStarted = SharedPreferencesUtil.loadBoolean(context, Constants.STARTED_TAG);
        if (isStarted) {
            if (finalSteps < 0) {
                throw new Exception("Invalid: negative final step count");
            }
            //cannot decrease the amount of steps taken on a walk
            int startSteps = SharedPreferencesUtil.loadInt(context, Constants.STARTSTEPS_TAG);
            if (finalSteps >= startSteps) {

                long start = SharedPreferencesUtil.loadLong(context, Constants.STARTTIME_TAG);
                //sharedPref.getLong("startTime", 0);
                long end = Duration.between(refTime, LocalDateTime.now()).getSeconds();

                //cannot end walk at a time before it is started
                if (end - start < 0) {
                    throw new Exception("Invalid: End time < start time");
                } else {
                    //TODO: REMOVE
                    //SharedPreferences.Editor editor = sharedPref.edit();

                    //update the WalkRun
                    Log.d("FINALSTEPS_ENDWALK", String.valueOf(finalSteps));

                    SharedPreferencesUtil.saveInt(context, Constants.ENDSTEPS_TAG, finalSteps);
                    SharedPreferencesUtil.saveLong(context, Constants.ENDTIME_TAG, end);
                    SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, true);
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
        Log.d("PROGRESS_STEPS", String.valueOf(pSteps));

        boolean isStarted = SharedPreferencesUtil.loadBoolean(context, Constants.STARTED_TAG);
        boolean isOk = SharedPreferencesUtil.loadBoolean(context, Constants.OK_TAG);
        if (isStarted) {
            if (!isOk) {
                //SharedPreferences.Editor editor = sharedPref.edit();

                //temporarily make it ok to get stats
                //editor.putBoolean("ok", true);
                SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, true);
                long end = Duration.between(refTime, LocalDateTime.now()).getSeconds();

                //update WalkRun prefs with the progress time and steps
                SharedPreferencesUtil.saveLong(context, Constants.ENDTIME_TAG, end);
                SharedPreferencesUtil.saveInt(context, Constants.ENDSTEPS_TAG, pSteps);

                int endSteps = SharedPreferencesUtil.loadInt(context, Constants.ENDSTEPS_TAG);
                //.getInt("endSteps", 0);
                Log.d("ENDSTEPS_CHECKPROG", String.valueOf(endSteps));
                String progress = getStats();

                //WalkRun is still incomplete

                SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, false);
                SharedPreferencesUtil.saveBoolean(context, Constants.STARTED_TAG, true);

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
        boolean isOk = SharedPreferencesUtil.loadBoolean(context, Constants.OK_TAG);
        if (isOk) {
            //duration
            long seconds = secondsWalked();

            int hours = (int) seconds / secondsInHour;
            seconds = seconds - (hours * secondsInHour);

            int minutes = (int) seconds / secondsInMinute;

            seconds = seconds - (minutes * secondsInMinute);

            //number of steps
            int steps = getNumSteps();
            Log.d("numSteps in stats", "" + steps);

            //speed
            double mph = getSpeed();
            Log.d("speed in stats", "" + mph);


            //distance
            double distance = getDistance();
            Log.d("distance in stats", "" + distance);

            reset();

            return ("Duration: " + hours + " hour(s), " + minutes + " minute(s), " + seconds + " second(s)\n"
                    + "Number of steps: " + steps + "\n"
                    + "Speed: " + mph + " mph\n"
                    + "Distance: " + distance + " miles");
        } else {
            throw new Exception("Invalid: This instance of WalkRun has started but not stopped.");
        }
    }

    /* Return the number of steps taken on this walk/run */
    public int getNumSteps() throws Exception {
        boolean isOk = SharedPreferencesUtil.loadBoolean(context, Constants.OK_TAG);
        if (isOk) {
            int endSteps = SharedPreferencesUtil.loadInt(context, Constants.ENDSTEPS_TAG);
            int startSteps = SharedPreferencesUtil.loadInt(context, Constants.STARTSTEPS_TAG);
            Log.d("STARTSTEPS_GETNUMSTEPS", String.valueOf(startSteps));
            Log.d("ENDSTEPS_GETNUMSTEPS", String.valueOf(endSteps));
            return (endSteps - startSteps);
        } else {
            throw new Exception("Invalid: Cannot getNumSteps for incomplete WalkRun");
        }
    }

    /* Return the distance walked in miles, rounded to one decimal place */
    public double getDistance() throws Exception {
        boolean isOk = SharedPreferencesUtil.loadBoolean(context, Constants.OK_TAG);
        if (isOk) {
            int height = SharedPreferencesUtil.loadInt(context, Constants.HEIGHT_TAG);
            double stride = height * strideMultiplier;
            double distanceFeet = stride * getNumSteps() / inchesInFeet;
            double distanceMiles = distanceFeet / feetInMile;
            return Math.round(distanceMiles * 10) / 10.0;
        } else {
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
        long endTime = SharedPreferencesUtil.loadLong(context, Constants.ENDTIME_TAG);
        long startTime = SharedPreferencesUtil.loadLong(context, Constants.STARTTIME_TAG);
        long diff = endTime - startTime;

        if (diff > 0) {
            return diff;
        } else {
            throw new Exception("Invalid: WalkRun end time before WalkRun start time");
        }
    }

    /* Called to reset WalkRun preferences, ready to go on another walk/run */
    public void reset() {
        SharedPreferencesUtil.saveBoolean(context, Constants.STARTED_TAG, false);

    }
}