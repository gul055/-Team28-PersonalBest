package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.util.Log;

import static edu.ucsd.cse110.googlefitapp.StepCountActivity.stepProgress;

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
            if(SharedPreferencesUtil.loadLong(context, Constants.GOAL_TAG) == goalCandidate) {
                Log.d("PREV AND NEXT GOAL =", String.valueOf(goalCandidate));
                return false;
            }
            SharedPreferencesUtil.saveLong(context, Constants.GOAL_TAG, goalCandidate);
            SharedPreferencesUtil.saveLong(context, Constants.DAILY_STEPS_TAG, 0);

            stepProgress.resetDaily();
            Log.d("SUCCESSFUL GOAL UPDATE", String.valueOf(goalCandidate));
            return true;
        } else {
            Log.d("INVALID GOAL", String.valueOf(goalCandidate));
            return false;
        }
    }
}
