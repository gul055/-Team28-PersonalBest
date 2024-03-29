package edu.ucsd.cse110.googlefitapp.Goals;

import android.content.Context;
import android.util.Log;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Calendars.CalendarAdapter;
import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.SendData;
import edu.ucsd.cse110.googlefitapp.Utils.GoogleUserUtil;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

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
            if (SharedPreferencesUtil.loadLong(context, Constants.GOAL) == goalCandidate) {
                Log.d("PREV AND NEXT GOAL =", String.valueOf(goalCandidate));
                return false;
            }
            SharedPreferencesUtil.saveLong(context, Constants.GOAL, goalCandidate);
            SharedPreferencesUtil.saveLong(context, Constants.DAILY_STEPS_TAG, 0);

            Log.d("SUCCESSFUL GOAL UPDATE", String.valueOf(goalCandidate));
            SharedPreferencesUtil.saveLong(context, Constants.GOAL, goalCandidate);
            Log.d("GOAL", "New goal set: " + goalCandidate);

            AbstractCalendar calendar = new CalendarAdapter();
            SendData send = new SendData(context, new GoogleUserUtil(), new SharedPreferencesUtil());
            send.SendLong(calendar.getYearMonthDay() + Constants.GOAL, goalCandidate);

            return true;
        } else {
            Log.d("GOAL", "Did not set goal");
            return false;
        }
    }
}
