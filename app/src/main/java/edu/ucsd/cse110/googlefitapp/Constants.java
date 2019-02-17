package edu.ucsd.cse110.googlefitapp;

public final class Constants {
    public static final int MINIMUM_VALID_GOAL = 100;
    public static final int MINIMUM_SUB_GOAL = 500;
    public static final String GOAL = "goal";
    public static final String SET_SUCCESS = "New goal set!";
    public static final String SET_FAIL = "Invalid goal. Please try again.";
    public static final String NO_GOAL = "No goal inputted!";
    public static final String START_WALK = "Start Walk/Run";
    public static final String STOP_WALK = "Stop Walk/Run";
    public static final String MAIN_ENCOURAGEMENT = "Congratulations, you reached your daily step Goal!";
    public static final String SUB_ENCOURAGEMENT1 = "You've increased your daily steps by over ";
    public static final String SUB_ENCOURAGEMENT2 = " steps. Keep up the good work!";
    public static final int MAIN = 0;
    public static final int SUB = 1;
    public static final int SUB_MSG_TIME_LIMIT = 20; //8pm
    public static final int PREV_MSG_TIME_LIMIT = 12; //12am
    public static final String DAILY_STEPS_TAG = "daily_steps";
    public static final String TOTAL_STEPS_TAG = "total_steps";
    public static final String LAST_UPDATE_TAG = "last_update";
    public static final String GOAL_TAG = "goal";
    public static final String GOAL_MET_TAG = "goal_met";
    public static final String DAILY_STEP_KEY = "daily_step";
    public static final String ON_WALK_TAG = "on_walk";
    public static final int PRESET_INCREMENT = 500;
    public static final String HEIGHT_PREF = "height_data";
    public static final String HEIGHT = "height";
    public static final String WALKRUN_PREF = "walkrun_data";
    public static final String INTENTIONAL = "intentional_steps";
    public static final String NOT_NOW_PRESS = "notNowListener";
    public static final int FIRST_MEET_GOAL = 1;
    public static final int MULTIPLY_MEET_GOAL = 2;

    // Specific requestor code for setting goal
    public static final int GOAL_ACTIVITY_CODE = 1;

    // Calendar constant
    public static final int WEEK_SIZE = 7;

    // Step increment
    public static final int STEP_INCREMENT = 500;
}
