package edu.ucsd.cse110.googlefitapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    public static void saveLong(Context c, String name, long value) {

        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences(new GoogleUserUtil().getEmail(c), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public static long loadLong(Context c, String name) {
        // TODO: Remove the hardcoded string
        SharedPreferences sharedPreferences = c.getSharedPreferences(new GoogleUserUtil().getEmail(c), Context.MODE_PRIVATE);
        long i = sharedPreferences.getLong(name, -1);
        return i;
    }

    public static void saveInt(Context c, String name, int value) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(new GoogleUserUtil().getEmail(c), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static int loadInt(Context c, String name) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(new GoogleUserUtil().getEmail(c), Context.MODE_PRIVATE);
        int i = sharedPreferences.getInt(name, -1);
        return i;
    }

    public static void saveBoolean(Context c, String name, boolean bool) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(new GoogleUserUtil().getEmail(c), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, bool);
        editor.apply();
    }

    public static boolean loadBoolean(Context c, String name) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(new GoogleUserUtil().getEmail(c), Context.MODE_PRIVATE);
        boolean bool = sharedPreferences.getBoolean(name, false);
        return bool;
    }

    public void saveLongByEmail(Context c, String email, String name, long value) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(email, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public long loadLongByEmail(Context c, String email, String name) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(email, Context.MODE_PRIVATE);
        long i = sharedPreferences.getLong(name, 0);
        return i;
    }
}
