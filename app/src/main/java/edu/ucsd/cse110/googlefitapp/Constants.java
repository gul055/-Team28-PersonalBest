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



    // Specific requestor code for setting goal
    public static final int GOAL_ACTIVITY_CODE = 1;
}
