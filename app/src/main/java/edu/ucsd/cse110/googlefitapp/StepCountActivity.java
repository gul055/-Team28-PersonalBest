package edu.ucsd.cse110.googlefitapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucsd.cse110.googlefitapp.fitness.FitnessService;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepLogger;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepUpdater;

import static edu.ucsd.cse110.googlefitapp.Constants.DAILY_STEPS_TAG;
import static edu.ucsd.cse110.googlefitapp.Constants.GOAL;
import static edu.ucsd.cse110.googlefitapp.Constants.GOAL_TAG;
import static edu.ucsd.cse110.googlefitapp.Constants.LAST_UPDATE_TAG;
import static edu.ucsd.cse110.googlefitapp.Constants.PRESET_INCREMENT;
import static edu.ucsd.cse110.googlefitapp.Constants.TOTAL_STEPS_TAG;

public class StepCountActivity extends AppCompatActivity {

    public StepLogger stepLogger;
    public HeightLogger heightLogger;
    public SharedPreferencesUtil prefUtil;
    public static StepUpdater stepProgress = new StepUpdater();

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private FitnessService fitnessService;

    private TextView textSteps, textGoal;

    SharedPreferences heightSharedPref;
    SharedPreferences walkRunSharedPref;

    Button startStopBtn;
    Button setGoalBtn;

    WalkRun myWalkRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);

        // request height for first sign in
        heightLogger = new HeightLogger(this);
        heightSharedPref = getApplicationContext().getSharedPreferences("height_data", MODE_PRIVATE);
        textSteps = findViewById(R.id.textSteps);
        textGoal = findViewById(R.id.textGoal);
        final String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        stepLogger = new StepLogger(this);
        prefUtil = new SharedPreferencesUtil();

        fitnessService.setup();

        // Create all buttons
        startStopBtn = (Button) findViewById(R.id.startStopBtn);
        setGoalBtn = (Button) findViewById(R.id.setGoalBtn);

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.i("IN_WALK_BUTTON","Clicked Walk/Run Button!");
                if(startStopBtn.getText() == Constants.STOP_WALK) {
                    //Stop walk/run
                    Log.i("IN_STOP_BUTTON","Clicked stop button!");
                    Toast.makeText(StepCountActivity.this, "Clicked stop!", Toast.LENGTH_SHORT).show();
                    try {
                        myWalkRun.endWalkRun(Math.toIntExact(stepProgress.getTotalSteps()));
                        Toast.makeText(StepCountActivity.this, "Walk/Run ended!", Toast.LENGTH_SHORT).show();
                        Log.i("STOP_WALKRUN","Ended walk/run!");

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

                            //CALL THIS TO GET NUMBER OF STEPS FROM INTENTIONAL WALK/RUN.
                            //int intentionalSteps = myWalkRun.getNumSteps();
                        }
                        catch (Exception e) {
                            Log.d("WALKRUN_PROGRESS_CATCH", "WalkRun started?: " + walkRunSharedPref.getBoolean("started", true));
                            e.printStackTrace();
                        }
                    } catch (Exception e)
                    {
                        Log.e("END_WALKRUN_CATCH", "FAIL TO END WALK/RUN.\nWalk/Run already started?: " + walkRunSharedPref.getBoolean("started", false));
                        Toast.makeText(StepCountActivity.this, "FAIL TO END WALK/RUN. \nWalk/Run already started?: " + walkRunSharedPref.getBoolean("started", true), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    myWalkRun.reset();
                    startStopBtn.setBackgroundColor(Color.GREEN);
                    startStopBtn.setText(Constants.START_WALK);
                    SharedPreferencesUtil.saveBoolean(StepCountActivity.this, Constants.ON_WALK_TAG, false);
                }
                else {
                    //Start walk/run
                    Log.i("IN_START_BUTTON","Clicked start button!");
                    Toast.makeText(StepCountActivity.this, "Clicked start!", Toast.LENGTH_SHORT).show();
                    try {
                        myWalkRun.startWalkRun(Math.toIntExact(stepProgress.getTotalSteps()));
                        Toast.makeText(StepCountActivity.this, "Walk/Run started!", Toast.LENGTH_SHORT).show();
                        Log.i("START_WALKRUN","Started walk/run!");

                    } catch (Exception e)
                    {
                        Toast.makeText(StepCountActivity.this, "FAIL TO START WALK/RUN.\\nWalk/Run already started?: " + walkRunSharedPref.getBoolean("started", true), Toast.LENGTH_SHORT).show();
                        Log.e("START_WALKRUN_CATCH", "FAIL TO START WALK/RUN.\nWalk/Run started?: " + walkRunSharedPref.getBoolean("started", true));
                        e.printStackTrace();
                    }
                    startStopBtn.setBackgroundColor(Color.RED);
                    startStopBtn.setText(Constants.STOP_WALK);
                    SharedPreferencesUtil.saveBoolean(StepCountActivity.this, Constants.ON_WALK_TAG, true);
                }

                /*
                if (stepProgress.getOnDaily() == true) {
                    stepLogger.writeOnDaily(false);
                    stepProgress.setOnDaily(false);
                    startStopBtn.setBackgroundColor(Color.GREEN);
                    startStopBtn.setText(Constants.START_WALK);
                    Toast.makeText(StepCountActivity.this, "Walk/Run stopped", Toast.LENGTH_LONG).show();

                } else {
                    stepLogger.writeOnDaily(true);
                    stepProgress.setOnDaily(true);
                    startStopBtn.setBackgroundColor(Color.RED);
                    startStopBtn.setText(Constants.STOP_WALK);
                    Toast.makeText(StepCountActivity.this, "Walk/Run started", Toast.LENGTH_LONG).show();
                }
                */
            }
        });

        setGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SetGoalActivity.class);
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
            Toast.makeText(StepCountActivity.this, "You Have Not Assign Height Yet", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(StepCountActivity.this, HeightPrompt.class);
            startActivity(intent);
            return;
        }

        long height = heightSharedPref.getLong("height", 0);

        //Toast.makeText(StepCountActivity.this, "Height: " + height, Toast.LENGTH_SHORT).show();
        //instantiate a WalkRun instance using height
        if(myWalkRun == null) {
            try {
                myWalkRun = new WalkRun(StepCountActivity.this, Math.toIntExact(height));
                walkRunSharedPref = getApplicationContext().getSharedPreferences("walkrun_data", MODE_PRIVATE);
                Toast.makeText(StepCountActivity.this, "WalkRun started?: " + walkRunSharedPref.getBoolean("started", true), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("BAD_WALKRUN_HEIGHT", String.valueOf(height));
                e.printStackTrace();
            }
        }

        /*Initialize Walk/Run button with appropriate color and text*/
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

                String progress = myWalkRun.checkProgress(Math.toIntExact(stepProgress.getTotalSteps()));

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
            }
            catch (Exception e) {
                Log.d("WALKRUN_PROGRESS_CATCH", "WalkRun started?: " + walkRunSharedPref.getBoolean("started", true));
                e.printStackTrace();
            }
        }

        long goalSet = prefUtil.loadLong(this, Constants.GOAL_TAG);
        stepProgress.setDailyGoal(goalSet);
        Log.d("GOAL ON RESUME", String.valueOf(stepProgress.getDailyGoal()));
        fitnessService.updateStepCount();
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

    public void setStepCount(long stepCount) {
        /*Grabs all relevant values from local file*/

        /*
        long lastSteps = stepLogger.readLastStep();
        long dailyGoal = stepLogger.readGoal();
        long dailyProgress = stepLogger.readDaily();
        */
        long lastSteps = prefUtil.loadLong(this, LAST_UPDATE_TAG);
        long dailyProgress = prefUtil.loadLong(this, DAILY_STEPS_TAG);
        long dailyGoal = prefUtil.loadLong(this, GOAL_TAG);
        Log.d("CURRENT", String.valueOf(stepCount));
        Log.d("LAST", String.valueOf(lastSteps));
        Log.d("DAILY GOAL", String.valueOf(dailyGoal));

        long stepDifference = stepCount - lastSteps;

        stepProgress.setTotalSteps(prefUtil.loadLong(this, TOTAL_STEPS_TAG));

        boolean isOnDaily = stepProgress.getOnDaily();
        Log.d("ON_DAILY", String.valueOf(isOnDaily));

        stepProgress.setDailyGoal(dailyGoal);
        stepProgress.setDailySteps(dailyProgress);

        /*Updates daily*/
        if (isOnDaily) {
            if(stepProgress.updateDaily(stepDifference)){
                //Add prompt here to assign new goal or to continue with preset.
                stepProgress.setDailyGoal(stepProgress.getDailyGoal() + Constants.PRESET_INCREMENT);
            }
            else {
                int timesGoalMet = SharedPreferencesUtil.loadInt(this, Constants.GOAL_MET_TAG);
                SharedPreferencesUtil.saveInt(this, Constants.GOAL_MET_TAG, timesGoalMet + 1);
                // TODO: ADD PROMPTS

                stepProgress.resetDaily();
            }
        }

        /*Updates total step progress*/
        stepProgress.updateProgress(stepDifference);

        /*Updates step progress to determine if on daily or not*/
        if (stepProgress.getOnDaily() != isOnDaily)
            stepProgress.setOnDaily(isOnDaily);

        //Set text of strings.
        Log.d("TOTAL_STEPS", String.valueOf(stepProgress.getTotalSteps()));
        Log.d("GOAL_PROGRESS", String.valueOf(stepProgress.getGoalProgress()));
        textSteps.setText(String.valueOf(stepProgress.getTotalSteps()));
        textGoal.setText(String.valueOf(stepProgress.getGoalProgress()));

        /*After all updates have finished, write to logger*/
        //stepLogger.writeSteps(stepProgress.getDailySteps(), stepProgress.getTotalSteps(), stepCount, stepProgress.getDailyGoal());
        prefUtil.saveLong(this, DAILY_STEPS_TAG, stepProgress.getDailySteps());
        prefUtil.saveLong(this, TOTAL_STEPS_TAG, stepProgress.getTotalSteps());
        prefUtil.saveLong(this, LAST_UPDATE_TAG, stepCount);
        prefUtil.saveLong(this, GOAL_TAG, stepProgress.getDailyGoal());
    }

}
