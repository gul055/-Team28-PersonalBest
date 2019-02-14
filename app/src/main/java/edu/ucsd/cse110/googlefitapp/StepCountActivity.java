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
import static edu.ucsd.cse110.googlefitapp.Constants.TOTAL_STEPS_TAG;


public class StepCountActivity extends AppCompatActivity {

    public StepLogger stepLogger;
    public HeightLogger heightLogger;
    long height;
    public SharedPreferencesUtil prefUtil;
    public StepUpdater stepProgress = new StepUpdater();

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private FitnessService fitnessService;

    private TextView textSteps, textGoal, textHeight, textProgress;

    SharedPreferences heightSharedPref;
    SharedPreferences walkRunSharedPref;

    Button startStopBtn;
    Button setGoalBtn;
    Button showStepsBtn;

    WalkRun myWalkRun;

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        
        // request height for first sign in
        heightLogger = new HeightLogger(this);
        height = heightSharedPref.getLong("height", 0);
        heightSharedPref = getApplicationContext().getSharedPreferences("height_data", MODE_PRIVATE);
        walkRunSharedPref = getApplicationContext().getSharedPreferences("walk_run", MODE_PRIVATE);
        textSteps = findViewById(R.id.textSteps);
        textGoal = findViewById(R.id.textGoal);
        textHeight = findViewById(R.id.textHeight);
        textProgress = findViewById(R.id.textProgress);
        final String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        stepLogger = new StepLogger(this);
        prefUtil = new SharedPreferencesUtil();

        fitnessService.setup();

        // Create all buttons
        startStopBtn = (Button) findViewById(R.id.startStopBtn);
        setGoalBtn = (Button) findViewById(R.id.setGoalBtn);
        showStepsBtn = (Button) findViewById(R.id.showStepsBtn);

        if (stepLogger.readOnDaily() == false) {
            stepProgress.setOnDaily(false);
            startStopBtn.setBackgroundColor(Color.GREEN);
            startStopBtn.setText(Constants.START_WALK);
        } else {
            stepProgress.setOnDaily(true);
            startStopBtn.setBackgroundColor(Color.RED);
            startStopBtn.setText(Constants.STOP_WALK);
        }

        startStopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (stepProgress.getOnDaily() == true) {
                    stepLogger.writeOnDaily(false);
                    stepProgress.setOnDaily(false);
                    startStopBtn.setBackgroundColor(Color.GREEN);
                    startStopBtn.setText(Constants.START_WALK);

                    //end the walk/run
                    try {
                        myWalkRun.endWalkRun(Math.toIntExact(stepProgress.getTotalSteps()));
                        textProgress.setText("");
                        String stats = myWalkRun.getStats();

                        AlertDialog.Builder statsBuilder = new AlertDialog.Builder(getApplicationContext());
                        statsBuilder.setMessage(stats);
                        statsBuilder.setCancelable(true);

                        statsBuilder.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog statsAlert = statsBuilder.create();
                        statsAlert.show();
                    }

                    catch (Exception e) {
                        Log.e(TAG,"STOP WALK/RUN FAILED: Already started? " + walkRunSharedPref.getBoolean("started", false));
                        e.printStackTrace();
                    }

                } else {
                    stepLogger.writeOnDaily(true);
                    stepProgress.setOnDaily(true);
                    startStopBtn.setBackgroundColor(Color.RED);
                    startStopBtn.setText(Constants.STOP_WALK);

                    //start the walk/run
                    try {
                        myWalkRun.startWalkRun(Math.toIntExact(stepProgress.getTotalSteps()));
                    }
                    catch (Exception e) {
                        Log.e(TAG,"START WALK/RUN FAILED. Already started?: " + walkRunSharedPref.getBoolean("started", true));
                        e.printStackTrace();
                    }
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

        /*
        showStepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fitnessService.updateStepCount();
            }
        });


        Button setGoalButton = findViewById(R.id.setGoal);
        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        */
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

        height = heightSharedPref.getLong("height", 0);
        textHeight.setText(String.valueOf(height));

        if(myWalkRun == null) {
            try {
                myWalkRun = new WalkRun(this, Math.toIntExact(height));
            } catch (Exception e) {
                Log.e(TAG, "BAD WALKRUN HEIGHT" + height);
                e.printStackTrace();
            }
        }
        else {
            if(startStopBtn.getText() == Constants.START_WALK) {
                myWalkRun.reset();
            }
            else {
                try {
                    String progress = myWalkRun.checkProgress(Math.toIntExact(stepProgress.getTotalSteps()));
                    textProgress.setText(progress);
                } catch (Exception e) {
                    Log.e(TAG, "CANNOT CHECK WALK/RUN PROGRESS. Started? " + walkRunSharedPref.getBoolean("started", true));
                    e.printStackTrace();
                }
            }
        }



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
        long lastSteps = 0;
        long dailyProgress = 0;
        long dailyGoal = 0;
        prefUtil.saveLong(this, LAST_UPDATE_TAG, lastSteps);
        prefUtil.saveLong(this, DAILY_STEPS_TAG, dailyProgress);
        prefUtil.saveLong(this, GOAL_TAG, dailyGoal);
        Log.d("CURRENT", String.valueOf(stepCount));
        Log.d("LAST", String.valueOf(lastSteps));

        long stepDifference = stepCount - lastSteps;

        stepProgress.setTotalSteps(prefUtil.loadLong(this, TOTAL_STEPS_TAG));

        boolean isOnDaily = stepProgress.getOnDaily();
        Log.d("ON_DAILY", String.valueOf(isOnDaily));

        stepProgress.setDailyGoal(dailyGoal);
        stepProgress.setDailySteps(dailyProgress);

        /*Updates daily*/
        if (isOnDaily) {
            stepProgress.updateDaily(false, stepDifference);
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
