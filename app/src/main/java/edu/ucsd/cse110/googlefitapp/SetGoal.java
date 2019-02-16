package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.util.Log;

public class SetGoal implements Goal {
    Context context;

    public SetGoal(Context c) {
        context = c;
    }

    private boolean isValidGoal(long goalCandidate) {
        return goalCandidate >= Constants.MINIMUM_VALID_GOAL;
    }

    public boolean set(long goalCandidate) {
        if (isValidGoal(goalCandidate)) {
            SharedPreferencesUtil.saveLong(context, Constants.GOAL, goalCandidate);
            Log.d("GOAL", "New goal set: " + goalCandidate);
            return true;
        } else {
            Log.d("GOAL", "Did not set goal");
            return false;
        }
    }
}
