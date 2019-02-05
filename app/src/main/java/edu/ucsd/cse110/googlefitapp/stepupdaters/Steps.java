package edu.ucsd.cse110.googlefitapp.stepupdaters;

public class Steps {
    private int stepCount;
    Steps(int stepCount){
        this.stepCount = stepCount;
    }

    public int getSteps(){
        return this.stepCount;
    }

    public void addSteps(int steps){
        this.stepCount += steps;
    }

    public void setSteps(int steps){
        this.stepCount = steps;
    }
}