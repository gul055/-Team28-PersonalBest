package edu.ucsd.cse110.googlefitapp.stepupdaters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

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
    public static Calendar calendar;
    public static boolean debug;

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
        calendar = Calendar.getInstance();
        debug = false;
    }


    public long getCurrSteps() {
        return stepUpdater.getDailySteps();
    }

    public EncourageMsg getPastEncouragement() {
        return pastEncouragement;
    }

    public EncourageMsg getCurrEncouragement() {
        return currEncouragement;
    }

    public boolean isPreviousEncourageGiven() {
        return PreviousEncourageGiven;
    }

    public boolean isSubEncourageGiven() {
        return SubEncourageGiven;
    }

    public boolean isMainEncourageGiven() {
        return MainEncourageGiven;
    }

    public void setPrevSteps(long steps) {
        prevSteps = steps;
    }

    public long getPrevSteps() {
        return prevSteps;
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
        return ((stepUpdater.getDailySteps() - prevSteps) / MINIMUM_SUB_GOAL) * MINIMUM_SUB_GOAL;
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
        if (stepUpdater.getDailySteps() == 0) { // The goal was just reached
            currEncouragement = encourageFactory.buildMsg(MAIN, stepUpdater, prevSteps);
            Log.d("ENCOURAGEMENT_MADE", "Made " +
                    currEncouragement.getClass().toString() +
                    "for date:" +
                    currEncouragement.getDate().toString());
        }
        // If the sub goal was just met or improved -> Create/update a SubEncourageMsg and set as currEncouragement
        else if (stepUpdater.getDailySteps() >= prevSteps + MINIMUM_SUB_GOAL &&
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
     * 1. Displays at most 1 Main or Sub goal if met on any previous day
     * 2. Displays at most 1 Main goal met if on the same day
     * 3. Displays as most 1 Sub goal met if on the same day
     */
    public void giveEncouragement() {
        if (pastEncouragement != null) {
            if (!PreviousEncourageGiven && pastEncouragement != null && beforePrevTimeLimit()) {
                Toast.makeText(context, pastEncouragement.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("PAST_ENCOURAGEMENT", "Previous day encouragement given");
                pastEncouragement = null;
                PreviousEncourageGiven = true;
            }
        }
        if (currEncouragement != null) {
            if (!MainEncourageGiven && currEncouragement.getClass() == MainEncourageMsg.class) {
                Toast.makeText(context, currEncouragement.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("MAIN_ENCOURAGEMENT", "Main goal encouragement given");
                currEncouragement = null;
                MainEncourageGiven = true;
            } else if (!SubEncourageGiven && currEncouragement.getClass() == SubEncourageMsg.class
                    && afterSubTimeLimit()) {
                Toast.makeText(context, currEncouragement.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("SUB_ENCOURAGEMENT", "Subgoal encouragement given");
                currEncouragement = null;
                SubEncourageGiven = true;
            }
        }
    }
}