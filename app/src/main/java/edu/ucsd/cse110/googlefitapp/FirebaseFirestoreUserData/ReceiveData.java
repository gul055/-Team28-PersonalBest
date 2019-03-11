package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import android.content.Context;

public class ReceiveData {

    Context context;
    DataService adapter;

    public ReceiveData(Context c){
        context = c;
        adapter = StepDataAdapter.getInstance(c);
    }
}
