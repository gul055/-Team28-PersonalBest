package edu.ucsd.cse110.googlefitapp.stepupdaters;

public class Steps {
    private long stepCount;

    Steps(long stepCount) {
        this.stepCount = stepCount;
    }

    public long getSteps() {
        return this.stepCount;
    }

    public void addSteps(long steps) {
        this.stepCount += steps;
    }

    public void setSteps(long steps) {
        this.stepCount = steps;
    }
}