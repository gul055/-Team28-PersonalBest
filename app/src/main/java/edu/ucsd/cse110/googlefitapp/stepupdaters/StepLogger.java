package edu.ucsd.cse110.googlefitapp.stepupdaters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import static android.content.Context.*;

public class StepLogger {

    SharedPreferences sharedPref;

    public StepLogger(Context context) {
        sharedPref = context.getSharedPreferences("step_data", MODE_PRIVATE);
    }

    /*Writes all step data to logger*/
    public void writeSteps(long dailySteps, long totalSteps, long lastStepUpdate, long goal) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("daily_steps", dailySteps);
        editor.putLong("total_steps", totalSteps);
        editor.putLong("last_update", lastStepUpdate);
        editor.putLong("goal", goal);
        editor.apply();
    }

    public void writeOnDaily(boolean onDaily) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("on_daily", onDaily);
        editor.apply();
    }

    public long readDaily() {
        return sharedPref.getLong("daily_steps", 0);
    }

    public long readTotal() {
        return sharedPref.getLong("total_steps", 0);
    }

    public long readLastStep() {
        return sharedPref.getLong("last_update", 0);
    }

    public long readGoal() {
        return sharedPref.getLong("goal", 5000);
    }

    public boolean readOnDaily() {
        return sharedPref.getBoolean("on_daily", false);
    }
}
