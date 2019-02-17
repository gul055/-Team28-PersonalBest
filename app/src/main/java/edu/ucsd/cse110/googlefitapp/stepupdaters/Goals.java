package edu.ucsd.cse110.googlefitapp.stepupdaters;

public class Goals {
    private final static int PRESETINCREASE = 500;
    private long stepGoal;

    Goals(long goal) {
        this.stepGoal = goal;
    }

    public void setGoal(long goalAmt) {
        this.stepGoal = goalAmt;
    }

    public long getGoal() {
        return this.stepGoal;
    }

}