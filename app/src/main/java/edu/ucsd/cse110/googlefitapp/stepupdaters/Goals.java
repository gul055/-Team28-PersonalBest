package edu.ucsd.cse110.googlefitapp.stepupdaters;

public class Goals{
    private final static int PRESETINCREASE = 500;
    private int stepGoal;
    Goals(int goal){
        this.stepGoal = goal;
    }

    public void setGoal(int goalAmt){
        this.stepGoal = goalAmt;
    }

    public int getGoal(){
        return this.stepGoal;
    }

    /*goalAmt is -1 if non-manual goal, otherwise manual goal*/
    public void calculateGoal(int goalAmt){
        if(goalAmt == -1) {
            this.setGoal(this.stepGoal + PRESETINCREASE);
        }
        else{
            this.setGoal(goalAmt);
        }
    }
}