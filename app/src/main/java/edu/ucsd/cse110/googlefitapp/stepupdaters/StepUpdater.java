//import Goals.java;
//import Steps.java;
package edu.ucsd.cse110.googlefitapp.stepupdaters;

import android.content.Context;
import android.util.Log;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.Utils.CalendarStringBuilderUtil;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class StepUpdater {
    Steps totalSteps;
    Steps goalSteps;
    Goals dailyGoal;
    boolean isOnDaily;
    Context c;

    public StepUpdater(Context c) {
        totalSteps = new Steps(0);
        goalSteps = new Steps(0);
        long goal = SharedPreferencesUtil.loadLong(c, Constants.GOAL);
        Log.d("STEPUPDATER_GOAL", String.valueOf(goal));
        if (goal == -1) {
            this.dailyGoal = new Goals(Constants.DEFAULT_GOAL);
        } else {
            this.dailyGoal = new Goals(SharedPreferencesUtil.loadLong(c, Constants.GOAL));
        }
        isOnDaily = false;
        this.c = c;
    }

    public void setTotalSteps(long totalSteps, AbstractCalendar calendar) {
        this.totalSteps.setSteps(totalSteps);
        this.writeSteps(totalSteps, calendar);
    }

    public void setGoalSteps(long goalSteps) {
        this.goalSteps.setSteps(goalSteps);
    }

    public long getTotalSteps() {
        return this.totalSteps.getSteps();
    }

    public long getDailyGoal() {
        return this.dailyGoal.getGoal();
    }

    public void setDailyGoal(long goal) {
        this.dailyGoal.setGoal(goal);
    }

    /*Grabs the total goal progress*/
    public long getGoalProgress() {
        return this.getDailyGoal() - this.goalSteps.getSteps();
    }

    public void writeSteps(long steps, AbstractCalendar calendar) {
        String key = CalendarStringBuilderUtil.stringBuilderCalendar(calendar, Constants.TOTAL_STEPS_TAG);
        Log.d("KEY_BUILT_STEPUPDATER", key + " " + steps);
        SharedPreferencesUtil.saveLong(c.getApplicationContext(), key, (int) steps);
    }

    public void addTotalSteps(long additionalStep) {

    }
}