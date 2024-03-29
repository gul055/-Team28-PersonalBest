package edu.ucsd.cse110.googlefitapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import java.time.LocalDateTime;
import java.util.Calendar;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Calendars.CalendarAdapter;
import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.FakeUserData;
import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.ReceiveData;
import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.SendData;
import edu.ucsd.cse110.googlefitapp.Goals.SetGoalActivity;
import edu.ucsd.cse110.googlefitapp.Goals.promptGoal;
import edu.ucsd.cse110.googlefitapp.Graph.GraphActivity;
import edu.ucsd.cse110.googlefitapp.Height.HeightLogger;
import edu.ucsd.cse110.googlefitapp.Height.HeightPrompt;
import edu.ucsd.cse110.googlefitapp.Utils.GoogleUserUtil;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessService;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.googlefitapp.stepupdaters.MockStepUpdater;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepLogger;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepUpdater;

import static edu.ucsd.cse110.googlefitapp.Constants.HEIGHT;
import static edu.ucsd.cse110.googlefitapp.Constants.HEIGHT_PREF;
import static edu.ucsd.cse110.googlefitapp.Constants.INTENTIONAL;
import static edu.ucsd.cse110.googlefitapp.Constants.STARTED_TAG;
import static edu.ucsd.cse110.googlefitapp.Constants.WALKRUN_PREF;
import static edu.ucsd.cse110.googlefitapp.Utils.CalendarStringBuilderUtil.stringBuilderCalendar;

public class StepCountActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "StepCountActivity";
    public static StepUpdater stepProgress;
    public StepLogger stepLogger;
    public HeightLogger heightLogger;
    SharedPreferences heightSharedPref, walkRunSharedPref;
    SendData DataCollector;
    Button startStopBtn;
    Button setGoalBtn;
    Button mockSteps;
    Button setTime;
    Button weeklySnapshot;
    Button goToFriends;
    Button chatButton;
    WalkRun myWalkRun;
    private AbstractCalendar calendar;
    private FitnessService fitnessService;
    private TextView textSteps, textGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        FirebaseApp.initializeApp(this);
        /*GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            Toast.makeText(this, personEmail, Toast.LENGTH_LONG).show();
        }*/

        stepProgress = new MockStepUpdater(getApplicationContext());

        /*
        Button fakeButton = findViewById(R.id.fake);
        fakeButton.setOnClickListener(v ->
        {
            new FakeUserData().sendData();
        });
        */

        chatButton = findViewById(R.id.chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FriendMessagesViewActivity.class);
                startActivity(intent);
            }
        });

        // request height for first sign in

        heightLogger = new HeightLogger(this);
        if (heightLogger.readHeight() == 0) {
            //Toast.makeText(StepCountActivity.this, "Height not set.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(StepCountActivity.this, HeightPrompt.class);
            startActivity(intent);
            return;
        }

        heightSharedPref = getApplicationContext().getSharedPreferences(HEIGHT_PREF, MODE_PRIVATE);
        textSteps = findViewById(R.id.textSteps);
        textGoal = findViewById(R.id.textGoal);
        final String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
        stepLogger = new StepLogger(this);
        calendar = new CalendarAdapter();


        // Create all buttons
        startStopBtn = (Button) findViewById(R.id.startStopBtn);
        setGoalBtn = (Button) findViewById(R.id.setGoalBtn);
        mockSteps = findViewById(R.id.mock_steps);
        setTime = findViewById(R.id.set_time);
        weeklySnapshot = findViewById(R.id.weekly_snapshot);
        goToFriends = findViewById(R.id.goToFriendsBtn);

        //Construct Data Sender
        DataCollector = new SendData(this, new GoogleUserUtil(), new SharedPreferencesUtil());


        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.i("IN_WALK_BUTTON", "Clicked Walk/Run Button!");
                int currSteps = (int) SharedPreferencesUtil.loadLong(StepCountActivity.this, Constants.DAILY_STEP_KEY);
                Log.d("CURRSTEPS_PRESSEDBUTTON", String.valueOf(currSteps));
                if (startStopBtn.getText() == Constants.STOP_WALK) {
                    fitnessService.updateStepCount();

                    //Stop walk/run
                    Log.i("IN_STOP_BUTTON", "Clicked stop button!");
                    try {
                        myWalkRun.endWalkRun(currSteps);
                        Toast.makeText(StepCountActivity.this, "Walk/Run ended!", Toast.LENGTH_SHORT).show();
                        Log.i("STOP_WALKRUN", "Ended walk/run!");

                        //display stats of walk/run
                        try {
                            String stats = myWalkRun.getStats();

                            //alert box to display walk/run progress
                            AlertDialog.Builder builder = new AlertDialog.Builder(StepCountActivity.this);
                            builder.setMessage(stats);
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog statAlert = builder.create();
                            statAlert.show();

                            //GET THE DATE STRING + NUMBER OF STEPS FROM INTENTIONAL WALK/RUN.
                            LocalDateTime date = LocalDateTime.now();
                            int year = date.getYear();
                            int day = date.getDayOfMonth();
                            int month = date.getMonthValue();

                            String dateSteps = year + "-" + month + "-" + day + INTENTIONAL;
                            Log.d("dateSteps", dateSteps);
                            long intentionalSteps = myWalkRun.getNumSteps() + SharedPreferencesUtil.
                                    loadLong(StepCountActivity.this, dateSteps);
                            Log.d("intentionalSteps", intentionalSteps + "");
                            //int OldintentionalSteps = myWalkRun.getNumSteps();
                            SharedPreferencesUtil.saveLong(StepCountActivity.this, dateSteps, (long) intentionalSteps);
                            updateGoal();
                            //Toast.makeText(StepCountActivity.this, year + "-" + month + "-" + day + intentionalSteps, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.d("WALKRUN_PROGRESS_CATCH", "WalkRun started?: " + walkRunSharedPref.getBoolean("started", true));
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        Log.e("END_WALKRUN_CATCH", "FAIL TO END WALK/RUN.\nWalk/Run already started?: " + walkRunSharedPref.getBoolean("started", false));
                        e.printStackTrace();
                    }

                    myWalkRun.reset();
                    startStopBtn.setBackgroundColor(Color.GREEN);
                    startStopBtn.setText(Constants.START_WALK);
                    SharedPreferencesUtil.saveBoolean(StepCountActivity.this, Constants.ON_WALK_TAG, false);

                    //get how many time user meets its goal and what date is today
                    long goalMet = SharedPreferencesUtil.loadLong(getApplicationContext(), Constants.GOAL_MET_TAG);
                    boolean notNowPress = SharedPreferencesUtil.loadBoolean(getApplicationContext(), Constants.NOT_NOW_PRESS);

                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                    Log.d("NOTNOWPRESS", String.valueOf(notNowPress));
                    Log.d("GOAL_MET_TAG", String.valueOf(goalMet));

                    //check if the user meets its goal
                    if (goalMet == Constants.FIRST_MEET_GOAL && !notNowPress) {
                        Intent i = new Intent(getApplicationContext(), promptGoal.class);
                        startActivity(i);
                    } else if (goalMet >= Constants.MULTIPLY_MEET_GOAL && notNowPress && dayOfWeek == Calendar.SATURDAY) {
                        Intent i = new Intent(getApplicationContext(), promptGoal.class);
                        startActivity(i);
                    }

                    // sending data to firebase
                    DataCollector.SendLong(stringBuilderCalendar(calendar, Constants.GOAL));
                    DataCollector.SendLong(stringBuilderCalendar(calendar, Constants.INTENTIONAL));
                    DataCollector.SendLong(stringBuilderCalendar(calendar, Constants.TOTAL_STEPS_TAG));

                } else {
                    //Start walk/run
                    Log.i("IN_START_BUTTON", "Clicked start button!");
                    try {
                        myWalkRun.startWalkRun(currSteps);
                        Toast.makeText(StepCountActivity.this, "Walk/Run started!", Toast.LENGTH_SHORT).show();
                        Log.i("START_WALKRUN", "Started walk/run!");

                    } catch (Exception e) {
                        Log.e("START_WALKRUN_CATCH", "FAIL TO START WALK/RUN.\nWalk/Run started?: " + walkRunSharedPref.getBoolean("started", true));
                        e.printStackTrace();
                    }
                    startStopBtn.setBackgroundColor(Color.RED);
                    startStopBtn.setText(Constants.STOP_WALK);
                    SharedPreferencesUtil.saveBoolean(StepCountActivity.this, Constants.ON_WALK_TAG, true);
                }
            }
        });

        setGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SetGoalActivity.class);
                startActivity(intent);
            }
        });

        mockSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepProgress.addTotalSteps(Constants.PRESET_INCREMENT);
                textSteps.setText(String.valueOf(stepProgress.getTotalSteps()));
                updateGoal();
            }
        });

        weeklySnapshot.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
            }
        });

        goToFriends = findViewById(R.id.goToFriendsBtn);
        goToFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FriendViewActivity.class);
                startActivity(intent);
            }
        });

        fitnessService.setup();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        if (heightLogger.readHeight() == 0) {
            Intent intent = new Intent(StepCountActivity.this, HeightPrompt.class);
            startActivity(intent);
            return;
        }

        long height = heightSharedPref.getLong(HEIGHT, 0);
        fitnessService.updateStepCount();
        long goalSet = SharedPreferencesUtil.loadLong(this, Constants.GOAL);

        /*Set goal*/
        //TODO: SRP this pls
        if (goalSet == -1) {
            stepProgress.setDailyGoal(Constants.DEFAULT_GOAL);
            SharedPreferencesUtil.saveLong(this, Constants.GOAL, Constants.DEFAULT_GOAL);
        } else {
            stepProgress.setDailyGoal(goalSet);
        }

        String goalTag = stringBuilderCalendar(calendar, Constants.GOAL);
        Log.d("GOAL_KEY", goalTag);
        SharedPreferencesUtil.saveLong(this, goalTag, stepProgress.getDailyGoal());

        if (myWalkRun == null) {
            try {
                myWalkRun = new WalkRun(StepCountActivity.this, Math.toIntExact(height));
                walkRunSharedPref = getApplicationContext().getSharedPreferences(WALKRUN_PREF, MODE_PRIVATE);
            } catch (Exception e) {
                Log.e("BAD_WALKRUN_HEIGHT", String.valueOf(height));
                e.printStackTrace();
            }
        }

        stepProgress.setDailyGoal(goalSet);
        Log.d("BEFORE UPDATE", stepProgress.getDailyGoal() + "");
        Log.d("AFTER UPDATE", stepProgress.getDailyGoal() + "");

        int currSteps = Math.toIntExact(SharedPreferencesUtil.loadLong(this, Constants.DAILY_STEP_KEY));
        Log.d("AFTERSTEPS_ONRESUME", String.valueOf(currSteps));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setStepCount(long stepCount) {
        Log.d("STEPCOUNT", String.valueOf(stepCount));
        SharedPreferencesUtil.saveLong(this, Constants.DAILY_STEP_KEY, stepCount);
        stepProgress.setTotalSteps(stepCount, calendar);
        textSteps.setText(String.valueOf(stepProgress.getTotalSteps()));
        Log.d("LOAD_UTILDAILYSTEP", String.valueOf(SharedPreferencesUtil.loadLong(this, Constants.DAILY_STEP_KEY)));
        displayStepData();
        updateGoal();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayStepData() {
        boolean onWalkRun = SharedPreferencesUtil.loadBoolean(this, Constants.ON_WALK_TAG);
        if (!onWalkRun) {
            startStopBtn.setBackgroundColor(Color.GREEN);
            startStopBtn.setText(Constants.START_WALK);
            myWalkRun.reset();
        } else {
            startStopBtn.setBackgroundColor(Color.RED);
            startStopBtn.setText(Constants.STOP_WALK);
            //walk/run is in progress, display progress
            try {

                int currSteps = Math.toIntExact(SharedPreferencesUtil.loadLong(this, Constants.DAILY_STEP_KEY));
                Log.d("STEPS_ONRESUME", String.valueOf(currSteps));
                String progress = myWalkRun.checkProgress(currSteps);

                //alert box to display walk/run progress
                AlertDialog.Builder builder = new AlertDialog.Builder(StepCountActivity.this);
                builder.setMessage(progress);
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog progressAlert = builder.create();
                progressAlert.show();
            } catch (Exception e) {
                Log.d("WALKRUN_PROGRESS_CATCH", "WalkRun started?: " + walkRunSharedPref.getBoolean("started", true));
                e.printStackTrace();
            }
        }
    }

    public void updateGoal() {
        boolean isStarted = SharedPreferencesUtil.loadBoolean(this, STARTED_TAG);
        if (isStarted) {
            Log.d("STEPPROGRESS TOTALSTEPS", String.valueOf(stepProgress.getTotalSteps()));

            if (stepProgress.getTotalSteps() >= stepProgress.getDailyGoal()) {
                //Ends the run!
                long timesGoalMet = SharedPreferencesUtil.loadLong(this, Constants.GOAL_MET_TAG);
                SharedPreferencesUtil.saveLong(this, Constants.GOAL_MET_TAG, timesGoalMet + 1);
                startStopBtn.performClick();
            }
            Log.d("GOAL_ON_RESUME", String.valueOf(stepProgress.getDailyGoal()));
            Log.d("GOAL_PROGRESS", String.valueOf(stepProgress.getGoalProgress()));
        } else {
            long goal = stepProgress.getDailyGoal();
            /*Check if first timer*/
            if (goal == -1) {
                textGoal.setText(String.valueOf(Constants.DEFAULT_GOAL));
            } else {
                textGoal.setText(String.valueOf(goal));
            }
        }
    }

    public void setCalendar(AbstractCalendar c) {
        calendar = c;
    }

    /*ALL CREDIT FOR THE FOLLOWING ASYNCTASK CODE GOES TO THE TUTSPLUS TUTORIAL ON GOOGLE FIT API
    Title: Google Fit for Android: History API
    https://code.tutsplus.com/tutorials/google-fit-for-android-history-api--cms-25856
    Captured: 2/15/2019
    How the source was used: Copied code
    K.D.
    */
    //TODO: CALL THIS FROM SOMEWHERE.
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("INASYNC", "In task");

            AbstractCalendar calendar = new CalendarAdapter();
            ReceiveData receiver = new ReceiveData(getApplicationContext(), new GoogleUserUtil().getEmail(getApplicationContext()), new SharedPreferencesUtil());
            String[] days = calendar.getLastXDays(Constants.WITH_YEAR, 28);
            for (String day : days) {
                receiver.receiveLong(day + Constants.GOAL);
                receiver.receiveLong(day + Constants.INTENTIONAL);
                receiver.receiveLong(day + Constants.TOTAL_STEPS_TAG);
            }

            //fitnessService.getWeeklyData();
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
            intent.putExtra(Constants.GRAPH_USER, new GoogleUserUtil().getEmail(getApplicationContext()));
            startActivity(intent);
        }
    }
}
