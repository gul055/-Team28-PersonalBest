//import Goals.java;
//import Steps.java;
package edu.ucsd.cse110.googlefitapp.stepupdaters;

import android.content.Context;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class StepUpdater {
    Steps totalSteps;
    Steps dailySteps;
    Goals dailyGoal;
    boolean isOnDaily;

    public StepUpdater(Context c) {
        totalSteps = new Steps(0);
        dailySteps = new Steps(0);
        dailyGoal = new Goals(SharedPreferencesUtil.loadLong(c, Constants.GOAL));
        isOnDaily = false;
    }

    public void setDailyGoal(long goal) {
        this.dailyGoal.setGoal(goal);
    }

    public void setTotalSteps(long totalSteps) {
        this.totalSteps.setSteps(totalSteps);
    }

    public long getTotalSteps() {
        return this.totalSteps.getSteps();
    }

    public long getDailySteps() {
        return this.dailySteps.getSteps();
    }

    public long getDailyGoal() {
        return this.dailyGoal.getGoal();
    }

    /*Grabs the total goal progress*/
    public long getGoalProgress() {
        return this.getDailyGoal() - this.dailySteps.getSteps();
    }

    /*Returns true if daily goal is reached, false otherwise*/
    public boolean updateDaily(long steps) {

        this.dailySteps.addSteps(steps);
        /*Checks if goal was met*/
        if (this.getGoalProgress() <= 0) {
            this.dailySteps.setSteps(0);
            return true;
        }
        return false;
    }

    public void resetDaily() {
        this.setDailyGoal(0);
    }
}