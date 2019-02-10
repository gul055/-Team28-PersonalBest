//import Goals.java;
//import Steps.java;
package edu.ucsd.cse110.googlefitapp.stepupdaters;

public class StepUpdater {
    Steps totalSteps;
    Steps dailySteps;
    Goals dailyGoal;
    boolean isOnDaily;

    public StepUpdater() {
        totalSteps = new Steps(0);
        dailySteps = new Steps(0);
        dailyGoal = new Goals(5000);
        isOnDaily = false;
    }

    public void setOnDaily(boolean isOnDaily) {
        this.isOnDaily = isOnDaily;
    }

    public void setDailyGoal(long goal) {
        this.dailyGoal.setGoal(goal);
    }

    public void setTotalSteps(long totalSteps) {
        this.totalSteps.setSteps(totalSteps);
    }

    public void setDailySteps(long dailySteps) {
        this.dailySteps.setSteps(dailySteps);
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

    public boolean getOnDaily() {
        return this.isOnDaily;
    }

    /*Updates total step progress*/
    public void updateProgress(long steps) {
        this.totalSteps.addSteps(steps);
    }

    /*Returns true if daily goal is reached, false otherwise*/
    public boolean updateDaily(boolean reset, long steps) {

        /*Checks if we reset our steps*/
        if (reset) {
            this.dailySteps.setSteps(0);
            return false;
        }

        this.dailySteps.addSteps(steps);
        /*Checks if goal was met*/
        if (this.getGoalProgress() <= 0) {
            this.dailySteps.setSteps(0);
            return true;
        }
        return false;
    }
}