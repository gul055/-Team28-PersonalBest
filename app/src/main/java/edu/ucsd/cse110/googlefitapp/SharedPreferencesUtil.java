package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.SharedPreferences;

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
        editor.apply();
    }

    public static long loadLong(Context c, String name) {
        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        long i = sharedPreferences.getLong(name, 0);
        return i;
    }

    public static void saveInt(Context c, String name, int value) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public static int loadInt(Context c, String name) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("step_count", Context.MODE_PRIVATE);
        int i = sharedPreferences.getInt(name, 0);
        return i;
    }
}
