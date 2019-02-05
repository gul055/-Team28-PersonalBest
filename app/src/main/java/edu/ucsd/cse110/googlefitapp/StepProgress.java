public class StepProgress{
    Steps totalSteps;
    Steps dailySteps;
    Goals dailyGoal;

    boolean onDaily;
    StepProgress(){
        totalSteps = new Steps(0);
        dailySteps = new Steps(0);
        dailyGoal = new Goal(5000);
        onDaily = false;
    }

    public int getTotalSteps(){
        return this.totalSteps.getSteps();
    }

    public int getDailySteps(){
        return this.dailySteps.getSteps();
    }

    public int getDailyGoal(){
        return this.dailyGoal.getGoal();
    }

    /*Grabs the total goal progress*/
    public int getGoalProgress(){
        return this.dailySteps.getSteps() - this.getDailyGoal();
    }

    public boolean getOnDaily(){
        return this.onDaily;
    }

    public void setDaily(boolean isOnDaily){
        this.isOnDaily = isOnDaily;
    }

    /*Updates total step progress*/
    public void updateProgress(int steps){
        this.totalSteps.addSteps(steps);
    }

    /*Returns true if daily goal is reached, false otherwise*/
    public boolean updateDaily(boolean reset, int steps){
        /*Checks if we reset our steps*/
        if(reset){
            this.dailySteps.setSteps(0);
            return false;
        }
        this.dailySteps.addSteps(steps);
        /*Checks if goal was met*/
        if(this.getGoalProgress() <= 0) {
            this.dailySteps.setSteps(0);
            return true;
        }
        return false;
    }
}