package edu.ucsd.cse110.googlefitapp.stepupdaters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.googlefitapp.Friends.FirebaseFriendList;
import edu.ucsd.cse110.googlefitapp.Friends.IFriendObserver;
import edu.ucsd.cse110.googlefitapp.Friends.MyFriendList;

import static edu.ucsd.cse110.googlefitapp.Constants.MAIN;
import static edu.ucsd.cse110.googlefitapp.Constants.MINIMUM_SUB_GOAL;
import static edu.ucsd.cse110.googlefitapp.Constants.PREV_MSG_TIME_LIMIT;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB_MSG_TIME_LIMIT;

public class EncourageHandler {
    private static Context context;
    private static boolean MainEncourageGiven;
    private static boolean SubEncourageGiven;
    private static boolean PreviousEncourageGiven;
    private static EncourageMsg pastEncouragement;
    private static EncourageMsg currEncouragement;
    private static StepUpdater stepUpdater;
    private static EncourageFactory encourageFactory;
    private static long prevSteps;
    public static boolean debug;
    private static Calendar calendar;
    private static MyFriendList myFriendList;
    private static FirebaseFriendList firebaseFriendList;

    public EncourageHandler(Context context, StepUpdater stepUpdater, MyFriendList myFriendList,
                            FirebaseFriendList firebaseFriendList) {
        this.context = context;
        this.stepUpdater = stepUpdater;
        encourageFactory = new EncourageFactory();
        currEncouragement = null;
        pastEncouragement = null;
        MainEncourageGiven = false;
        SubEncourageGiven = false;
        PreviousEncourageGiven = false;
        prevSteps = 0;
        this.myFriendList = myFriendList;
        this.firebaseFriendList = firebaseFriendList;
        firebaseFriendList.register((IFriendObserver) myFriendList);
        firebaseFriendList.loadFriends();
        calendar = Calendar.getInstance();
        debug = false;
    }


    public long getCurrSteps() {
        return stepUpdater.getTotalSteps();
    }

    public EncourageMsg getCurrEncouragement() {
        return currEncouragement;
    }

    public boolean isMainEncourageGiven() {
        return MainEncourageGiven;
    }

    public void setHour(int hour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
    }

    public void setDebug(boolean state) {
        debug = state;
    }


    /**
     * prevMsgTimeLimit() - determines if a previous sub-goal message should be displayed based on
     * the current time : < PREV_MSG_TIME_LIMIT = 12 = 12pm
     */
    public boolean beforePrevTimeLimit() {
        if (calendar.get(Calendar.HOUR_OF_DAY) < PREV_MSG_TIME_LIMIT) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * subMsgTimeLimit() - determines if a previous sub-goal message should be displayed based on
     * the current time : >= SUB_MSG_TIME_LIMIT = 20 = 8pm
     */
    public boolean afterSubTimeLimit() {
        if (calendar.get(Calendar.HOUR_OF_DAY) >= SUB_MSG_TIME_LIMIT) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * calcSubImprovement() - calculates the approximate step improvement and reports the closest
     * interval of 500 steps past the last day's total steps
     */
    public long calcSubImprovement() {
        return ((stepUpdater.getTotalSteps() - prevSteps) / MINIMUM_SUB_GOAL) * MINIMUM_SUB_GOAL;
    }

    /**
     * update() - contains logic
     **/
    public void update() {
        //Update the calendar
        if(!debug) {
            calendar = Calendar.getInstance();
        }
        // If a new day -> update the handler
        if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) > calendar.get(Calendar.DAY_OF_YEAR)) {
            resetForNewDay();
        }

        // If the goal was just met -> Create a MainEncourageMsg and set as currEncouragement
        if (stepUpdater.getTotalSteps() >= stepUpdater.getDailyGoal()) { // The goal was just reached
            currEncouragement = encourageFactory.buildMsg(MAIN, stepUpdater, prevSteps);
            Log.d("ENCOURAGEMENT_MADE", "Made " +
                    currEncouragement.getClass().toString() +
                    "for date:" +
                    currEncouragement.getDate().toString());
        }
        // If the sub goal was just met or improved -> Create/update a SubEncourageMsg and set as currEncouragement
        else if (stepUpdater.getTotalSteps() >= prevSteps + MINIMUM_SUB_GOAL &&
                currEncouragement == null || currEncouragement.getClass() != MainEncourageMsg.class) {
            currEncouragement = encourageFactory.buildMsg(SUB, stepUpdater, prevSteps);
            Log.d("ENCOURAGEMENT_MADE", "Made " +
                    currEncouragement.getClass().toString() +
                    "for date:" +
                    currEncouragement.getDate().toString());
        }

        giveEncouragement();
    }

    /**
     * resetForNewDay - performs the following functions:
     * 1. resets all flags for encouragements given
     * 2. sets today's total steps as prevSteps
     * 3. updates the handler's calendar
     * 4. sets any ungiven encouragement message to a prevEncouragement
     */
    public void resetForNewDay() {
        MainEncourageGiven = false;
        SubEncourageGiven = false;
        PreviousEncourageGiven = false;
        prevSteps = stepUpdater.getTotalSteps(); //???
        calendar = Calendar.getInstance();

        // If an encouragement was queued but not given yesterday make it a past encouragement
        if (currEncouragement != null && (!MainEncourageGiven || !SubEncourageGiven)) {
            pastEncouragement = currEncouragement;
            currEncouragement = null;
        }
        Log.d("RESET_ENC_HANDLER", "Reset the encourage handler for date:"
                + Calendar.getInstance().getTime().toString());
    }

    /**
     * giveEncouragement() - creates Toasts in the given
     * context according to the following rules and resets afterwards:
     *  1. this encouragement is current for this day
     *  2. this user has at least one friend
     *  3. this is a MainEncouragement message type
     *  4. this is the only encouragement message that has been give today
     */
    public void giveEncouragement() {
        if (currEncouragement != null && myFriendList.getFriendList().size() != 0) {
            if (!MainEncourageGiven && currEncouragement.getClass() == MainEncourageMsg.class) {
                Toast.makeText(context, currEncouragement.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("MAIN_ENCOURAGEMENT", "Main goal encouragement given");
                currEncouragement = null;
                MainEncourageGiven = true;
            }
        }
    }
}