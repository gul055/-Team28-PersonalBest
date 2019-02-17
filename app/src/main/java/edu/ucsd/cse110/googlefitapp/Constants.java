package edu.ucsd.cse110.googlefitapp;

public final class Constants {
    public static final int MINIMUM_VALID_GOAL = 100;
    public static final String GOAL = "goal";
    public static final String SET_SUCCESS = "New goal set!";
    public static final String SET_FAIL = "Invalid goal. Please try again.";
    public static final String NO_GOAL = "No goal inputted!";
    public static final String START_WALK = "Start Walk/Run";
    public static final String STOP_WALK = "Stop Walk/Run";
    public static final String STARTSTEPS_TAG = "startSteps";
    public static final String DAILY_STEPS_TAG = "daily_steps";
    public static final String TOTAL_STEPS_TAG = "total_steps";
    public static final String LAST_UPDATE_TAG = "last_update";
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
    public static final String REAL_CALENDAR = "real";
    public static final String LOCK_TO_FIRST_DAY_OF_WEEK = "lock";
    public static final String FAKE_CALENDAR = "fake";
    public static final boolean WITHOUT_YEAR = false;
    public static final boolean WITH_YEAR = true;

    // Step increment
    public static final int STEP_INCREMENT = 500;

    //Default step size
    public static final int DEFAULT_GOAL = 5000;
  
    // Graph constant
    public static final float GROUP_SPACE = 0.06f;
    public static final float BAR_SPACE = 0.02f;
    public static final float BAR_WIDTH = 0.45f;
    public static final String RECORDED_STEP = "Recorded Steps";
    public static final String INCIDENTAL_STEP = "Incidental Steps";
    public static final String GOAL_LABEL = "Daily Goals";
    public static final float GRANULARITY = 1f;
}
