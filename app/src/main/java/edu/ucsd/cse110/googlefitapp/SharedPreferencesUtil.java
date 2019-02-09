package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

public class SharedPreferencesUtil {

    public static void saveString(Context c, String name, String value) {

        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String loadString(Context c, String name) {
        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(name, "");
        return s;
    }

    public static void saveInt(Context c, String name, int value) {

        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static int loadInt(Context c, String name) {
        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        int i = sharedPreferences.getInt(name, 0);
        return i;
    }
}
