package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import android.content.Context;
import android.util.Log;

import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class ReceiveData {

    Context context;
    DataService adapter;
    String email;
    SharedPreferencesUtil sharedPref;

    public ReceiveData(Context c, String email, SharedPreferencesUtil sharedPref) {
        context = c;
        this.email = email;
        adapter = StepDataAdapter.getInstance(email);
        this.sharedPref = sharedPref;
    }

    public ReceiveData(Context c, String email, SharedPreferencesUtil sharedPref, DataService adapter) {
        context = c;
        this.email = email;
        this.adapter = adapter;
        this.sharedPref = sharedPref;
    }

    // Saves a friend's data to our own SharedPreferences
    // Returns the Long itself for potential use
    public Long receiveLong(String tag) {
        Long result = adapter.getLong(tag);
        Log.d("Tag being used to receive", tag);
        Log.d("Result of receive", result + "");
        sharedPref.saveLongByEmail(context, email, tag, result);
        return result;
    }
}
