package edu.ucsd.cse110.googlefitapp.stepupdaters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

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

    public EncourageHandler(Context context, StepUpdater stepUpdater) {
        this.context = context;
        this.stepUpdater = stepUpdater;
        encourageFactory = new EncourageFactory();
        currEncouragement = null;
        pastEncouragement = null;
        MainEncourageGiven = false;
        SubEncourageGiven = false;
        PreviousEncourageGiven = false;
        prevSteps = Long.MAX_VALUE;
    }


    /**
     * prevMsgTimeLimit() - determines if a previous sub-goal message should be displayed based on
     *                      the current time : < PREV_MSG_TIME_LIMIT = 12 = 12pm
     */
    public boolean beforePrevTimeLimit() {
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < PREV_MSG_TIME_LIMIT) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * subMsgTimeLimit() - determines if a previous sub-goal message should be displayed based on
     *                      the current time : >= SUB_MSG_TIME_LIMIT = 20 = 8pm
     */
    public boolean afterSubTimeLimit() {
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= SUB_MSG_TIME_LIMIT) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * update() - updates the handler's messages according to time and if a new message should
     *            be made. Lastly,
     **/
    public void update() {
        // If a new day -> update the handler
        if (Calendar.getInstance().getTime().after(currEncouragement.getDate())) {
            resetForNewDay();
        }

        // Create a new EncourageMsg if a sub/main goal has been met
        if( stepUpdater.getTotalSteps() >= stepUpdater.getDailyGoal() ) {
            currEncouragement = encourageFactory.buildMsg(MAIN, stepUpdater, prevSteps);
        }
        else if(stepUpdater.getTotalSteps() >= prevSteps + MINIMUM_SUB_GOAL ){
            currEncouragement = encourageFactory.buildMsg(SUB, stepUpdater, prevSteps);
        }
        Log.d("ENCOURAGEMENT_MADE", "Made " +
                                                    currEncouragement.getClass().toString() +
                                                    "for date:" +
                                                    currEncouragement.getDate().toString());
    }

    /**
     * resetForNewDay - resets all flags given and sets the day as the next
     */
    public void resetForNewDay() {
        MainEncourageGiven = false;
        SubEncourageGiven = false;
        PreviousEncourageGiven = false;
        prevSteps = stepUpdater.getTotalSteps();

        // If an encouragement was queued but not given yesterday make it a past encouragement
        if(currEncouragement != null && (!MainEncourageGiven || !SubEncourageGiven)) {
            pastEncouragement = currEncouragement;
            currEncouragement = null;
        }
        Log.d("RESET_ENC_HANDLER", "Reset the encourage handler for date:"
                + Calendar.getInstance().getTime().toString());
    }

    /**
     * giveEncouragement() - creates Toasts in the given
     *                       context according to the following rules and resets afterwards:
     *                       1. Displays at most 1 Main or Sub goal if met on any previous day
     *                       2. Displays at most 1 Main goal met if on the same day
     *                       3. Displays as most 1 Sub goal met if on the same day
     */
    public void giveEncouragement() {
        while(true) {
            if(pastEncouragement != null){
                if (!PreviousEncourageGiven && pastEncouragement != null && beforePrevTimeLimit()) {
                    Toast.makeText(context, pastEncouragement.getMessage(), Toast.LENGTH_LONG);
                    Log.d("PAST_ENCOURAGEMENT", "Previous day encouragement given");
                    pastEncouragement = null;
                    PreviousEncourageGiven = true;
                }
            }
            if(currEncouragement != null) {
                if (!MainEncourageGiven && currEncouragement.getClass() == MainEncourageMsg.class) {
                    Toast.makeText(context, currEncouragement.getMessage(), Toast.LENGTH_LONG);
                    Log.d("MAIN_ENCOURAGEMENT", "Main goal encouragement given");
                    currEncouragement = null;
                    MainEncourageGiven = true;
                } else if (!SubEncourageGiven && currEncouragement.getClass() == SubEncourageMsg.class
                        && afterSubTimeLimit()) {
                    Toast.makeText(context, currEncouragement.getMessage(), Toast.LENGTH_LONG);
                    Log.d("SUB_ENCOURAGEMENT", "Subgoal encouragement given");
                    currEncouragement = null;
                    SubEncourageGiven = true;
                }
            }
            else{
                // No more encouragements to give -> break loop
                break;
            }
        }
    }
}