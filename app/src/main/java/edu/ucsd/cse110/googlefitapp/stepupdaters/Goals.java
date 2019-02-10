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

    /*goalAmt is -1 if non-manual goal, otherwise manual goal*/
    public void calculateGoal(long goalAmt) {
        if (goalAmt == -1) {
            this.setGoal(this.stepGoal + PRESETINCREASE);
        } else {
            this.setGoal(goalAmt);
        }
    }
}