package edu.ucsd.cse110.googlefitapp;

public final class Constants {
    public static final int MINIMUM_VALID_GOAL = 100;
    public static final String GOAL = "goal";
    public static final String SET_SUCCESS = "New goal set!";
    public static final String SET_FAIL = "Invalid goal. Please try again.";
    public static final String NO_GOAL = "No goal inputted!";
    public static final String START_WALK = "Start Walk/Run";
    public static final String STOP_WALK = "Stop Walk/Run";

    // Specific requestor code for setting goal
    public static final int GOAL_ACTIVITY_CODE = 1;

    // Calendar constant
    public static final int WEEK_SIZE = 7;

    // Step increment
    public static final int STEP_INCREMENT = 500;

    // Graph constant
    public static final float GROUP_SPACE = 0.06f;
    public static final float BAR_SPACE = 0.02f;
    public static final float BAR_WIDTH = 0.45f;
}
