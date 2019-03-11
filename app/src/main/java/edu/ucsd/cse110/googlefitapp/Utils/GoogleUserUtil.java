package edu.ucsd.cse110.googlefitapp.Utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import edu.ucsd.cse110.googlefitapp.Constants;

public class GoogleUserUtil {

    // By default, this returns my (Derek's) personal email
    public static String getEmail(Context c){
        GoogleSignInAccount account = getAccount(c);

        if(account == null){
            return Constants.EXAMPLE_EMAIL;
        }
        return account.getEmail();
    }

    // Simply gets the account; can use this for mocking purposes
    public static GoogleSignInAccount getAccount(Context c){
        return GoogleSignIn.getLastSignedInAccount(c);
    }
}
