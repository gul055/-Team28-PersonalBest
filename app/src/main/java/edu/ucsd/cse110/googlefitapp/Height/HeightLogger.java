package edu.ucsd.cse110.googlefitapp.Height;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static edu.ucsd.cse110.googlefitapp.Constants.HEIGHT;
import static edu.ucsd.cse110.googlefitapp.Constants.HEIGHT_PREF;

public class HeightLogger {
    SharedPreferences sharedPref;
    private final static int inchesInFeet = 12;

    public HeightLogger(Context context) {
        sharedPref = context.getSharedPreferences(HEIGHT_PREF, MODE_PRIVATE);
    }

    /*Returns false if invalid input, true otherwise*/
    public boolean writeHeight(long feet, long inches) {
        if (feet < 0 || inches < 0) {
            return false;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        long height = this.convertHeight(feet, inches);
        editor.putLong(HEIGHT, height);
        editor.apply();
        return true;
    }

    public long readHeight() {
        return sharedPref.getLong(HEIGHT, 0);
    }

    public long convertHeight(long feet, long inches) {
        return inches + (feet * inchesInFeet);
    }
}
