package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Utils.GoogleUserUtil;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class SendData {

    Context context;
    DataService adapter;
    String email;
    SharedPreferencesUtil sharedPref;

    public SendData(Context c, GoogleUserUtil gUtil, SharedPreferencesUtil sharedPref) {
        context = c;
        email = gUtil.getEmail(c);
        adapter = StepDataAdapter.getInstance(email);
        this.sharedPref = sharedPref;
        // new SendData(c, new GoogleUserUtil(), new SharedPreferencesUtil());
    }

    public SendData(Context c, GoogleUserUtil gUtil, SharedPreferencesUtil sharedPref, DataService adapter) {
        context = c;
        email = gUtil.getEmail(c);
        this.adapter = adapter;
        this.sharedPref = sharedPref;
    }

    // Given the key for SharedPreferences, sends user's step data to Firebase in the form of a long
    public void SendLong(String tag) {

        // Retrieve the corresponding data from SharedPreferences
        Long data = sharedPref.loadLongByEmail(context, email, tag);

        Map<String, Object> singleDataMap = new HashMap<>();
        singleDataMap.put(tag, data);

        adapter.addData(singleDataMap);
    }

    // Given string and long, send to Firebase
    public void SendLong(String tag, Long value) {

        Map<String, Object> singleDataMap = new HashMap<>();
        singleDataMap.put(tag, value);

        adapter.addData(singleDataMap);
    }

}
