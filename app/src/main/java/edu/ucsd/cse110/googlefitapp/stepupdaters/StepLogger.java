package edu.ucsd.cse110.googlefitapp.stepupdaters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import static android.content.Context.*;

public class StepLogger {

    SharedPreferences sharedPref;

    StepLogger(Context context){
        sharedPref = context.getSharedPreferences("step_data", MODE_PRIVATE);
    }

    /*Writes all step data to logger*/
    public void writeSteps(int dailySteps, int totalSteps, int lastStepUpdate){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("daily_steps", dailySteps);
        editor.putInt("total_steps", totalSteps);
        editor.putInt("last_update", lastStepUpdate);
        editor.apply();
    }

    public int readDaily(){
        return sharedPref.getInt("daily_steps", 0);
    }

    public int readTotal(){
        return sharedPref.getInt("total_steps", 0);
    }

    public int readLastStep(){
        return sharedPref.getInt("last_update", 0);
    }
}
