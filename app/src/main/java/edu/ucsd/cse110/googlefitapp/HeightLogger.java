package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class HeightLogger {
    SharedPreferences sharedPref;
    public HeightLogger(Context context){
        sharedPref = context.getSharedPreferences("height_data", MODE_PRIVATE);
    }

    /*Returns false if invalid input, true otherwise*/
    public boolean writeHeight(long feet, long inches){
        if(feet < 0 || inches < 0){
            return false;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        long height = this.convertHeight(feet, inches);
        editor.putLong("height", height);
        editor.apply();
        return true;
    }

    public long readHeight(){
        return sharedPref.getLong("height", 0);
    }

    public long convertHeight(long feet, long inches){
        return inches + (feet * 12);
    }
}
