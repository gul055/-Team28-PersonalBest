package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import android.content.Context;

import edu.ucsd.cse110.googlefitapp.Utils.GoogleUserUtil;

public class SendData {

    Context context;
    String email;

    public SendData(Context c){
        context = c;
        email = GoogleUserUtil.getEmail(c);
    }

    // Sends user's step data to Firebase

}
