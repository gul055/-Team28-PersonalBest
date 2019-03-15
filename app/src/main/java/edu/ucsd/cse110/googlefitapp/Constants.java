package edu.ucsd.cse110.googlefitapp;

public final class Constants {
    //database reference keys
    public static final String FRIEND_COLLECTION_KEY = "friends";

    // Log tags
    public static final String FRIEND_TAG = "FRIEND";
    public static final String ON_WALK_TAG = "on_walk";
    public static final String DAILY_STEPS_TAG = "daily_steps";
    public static final String TOTAL_STEPS_TAG = "total_steps";
    public static final String GOAL_MET_TAG = "goal_met";
    public static final String FRIEND_UPDATER_TAG = "FRIENDUPDATER";
    public static final String FRIEND_TEST_TAG = "FRIENDTEST";

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
    public static final String STARTSTEPS_TAG = "startSteps";
    public static final String DAILY_STEP_KEY = "daily_step";
    public static final String HEIGHT_PREF = "height_data";
    public static final String HEIGHT = "height";
    public static final String WALKRUN_PREF = "walkrun_data";
    public static final String INTENTIONAL = "intentional_steps";
    public static final String NOT_NOW_PRESS = "notNowListener";
    public static final String ADDITIONAL_STEPS = "additional_steps";
    public static final String FRIEND_PREF = "friend_list";

    /*WalkRun SharedPref Tags*/
    public static final String HEIGHT_TAG = "height";
    public static final String OK_TAG = "ok";
    public static final String STARTED_TAG = "started";
    public static final String STARTTIME_TAG = "startTime";
    public static final String ENDTIME_TAG = "endTime";
    public static final String ENDSTEPS_TAG = "endSteps";
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
    public static final int PRESET_INCREMENT = 500;

    //Default goal size
    public static final int DEFAULT_GOAL = 5000;

    // Graph constant
    public static final float GROUP_SPACE = 0.06f;
    public static final float BAR_SPACE = 0.02f;
    public static final float BAR_WIDTH = 0.45f;
    public static final String RECORDED_STEP = "Recorded Steps";
    public static final String INCIDENTAL_STEP = "Incidental Steps";
    public static final String GOAL_LABEL = "Daily Goals";
    public static final float GRANULARITY = 1f;
    public static final String GRAPH_USER = "graphuser";

    // Email constant
    public static final String EXAMPLE_EMAIL = "arrestrose@gmail.com";

    // FirebaseFirestore constant
    public static final String STEPDATA = "stepdata";

    // Chat Constants
    public static final String COLLECTION_KEY = "chats";
    public static final String MESSAGES_KEY = "messages";
    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String FROM_KEY = "from";
    public static final String TEXT_KEY = "text";
    public static final String FRIENDPAIR = "friendPair";
}
