package edu.ucsd.cse110.googlefitapp;

import android.content.Context;

public class SetGoal {
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
            return true;
        } else {
            return false;
        }
    }
}
