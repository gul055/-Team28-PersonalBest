package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Utils.GoogleUserUtil;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class SendData {

    Context context;
    String email;

    public SendData(Context c){
        context = c;
        email = GoogleUserUtil.getEmail(c);
    }

    // Given the key for SharedPreferences, sends user's step data to Firebase in the form of a long
    public void SendLong(String tag){

        // Retrieve the corresponding data from SharedPreferences
        Long data = SharedPreferencesUtil.loadLong(context, tag);

        Map<String, Long> singleDataMap = new HashMap<>();
        singleDataMap.put(tag, data);


    }

}