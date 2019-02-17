package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

    public static void saveLong(Context c, String name, long value) {

        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(name, value);
        Log.d("SharedPreferencesUtil","Saving long:" + name + " = " + value + "");
        editor.apply();
    }

    public static long loadLong(Context c, String name) {
        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        long i = sharedPreferences.getLong(name, 0);
        Log.d("SharedPreferencesUtil","Loading long:" + name + " = " + i + "");
        return i;
    }

    //addBoolean
    public static boolean loadBoolean(Context c, String name) {
        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        boolean i = sharedPreferences.getBoolean(name, false);
        Log.d("SharedPreferencesUtil","Loading boolean:" + name + " = " + i + "");
        return i;
    }

    public static void saveBoolean(Context c, String name, boolean value) {
        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, value);
        Log.d("SharedPreferencesUtil","Saving boolean:" + name + " = " + value + "");
        editor.apply();
    }

}
