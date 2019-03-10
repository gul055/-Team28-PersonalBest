package edu.ucsd.cse110.googlefitapp.Utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import edu.ucsd.cse110.googlefitapp.Constants;

public class GoogleUserUtil {

    // By default, this returns my (Derek's) personal email
    public static String getEmail(Context c){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(c);

        if(account == null){
            return Constants.EXAMPLE_EMAIL;
        }
        return account.getEmail();
    }
}
