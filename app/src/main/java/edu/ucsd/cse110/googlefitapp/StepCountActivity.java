package edu.ucsd.cse110.googlefitapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Calendar;

import edu.ucsd.cse110.googlefitapp.fitness.FitnessService;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepLogger;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepUpdater;

import static edu.ucsd.cse110.googlefitapp.Constants.GOAL_TAG;
import static edu.ucsd.cse110.googlefitapp.Constants.TOTAL_STEPS_TAG;

public class StepCountActivity extends AppCompatActivity {

    public StepLogger stepLogger;
    public SharedPreferencesUtil prefUtil;
    public StepUpdater stepProgress;

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private FitnessService fitnessService;

    private TextView textSteps, textGoal;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);

        textSteps = findViewById(R.id.textSteps);
        textGoal = findViewById(R.id.textGoal);
        final String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        stepLogger = new StepLogger(this);
        stepProgress = new StepUpdater(this);

        fitnessService.setup();

        // Create all buttons
        final Button startStopBtn = (Button) findViewById(R.id.startStopBtn);
        Button setGoalBtn = (Button) findViewById(R.id.setGoalBtn);
        Button showStepsBtn = (Button) findViewById(R.id.showStepsBtn);

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

                    //get how many time user meets its goal and what date is today
                    long goalMet = prefUtil.loadLong(getApplicationContext(), Constants.GOAL_MET_TAG);
                    boolean notNowPress = prefUtil.loadBoolean(getApplicationContext(), Constants.NOT_NOW_PRESS);
                    int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

                    //check if the user meets its goal
                    if (goalMet == Constants.FIRST_MEET_GOAL && !notNowPress) {
                        Intent i = new Intent(getApplicationContext(), promptGoal.class);
                        startActivity(i);
                    }
                    else if (goalMet >= Constants.MULTIPLY_MEET_GOAL && notNowPress && dayOfWeek == Calendar.SATURDAY) {
                        Intent i = new Intent(getApplicationContext(), promptGoal.class);
                        startActivity(i);
                    }

                } else {
                    stepLogger.writeOnDaily(true);
                    stepProgress.setOnDaily(true);
                    startStopBtn.setBackgroundColor(Color.RED);
                    startStopBtn.setText(Constants.STOP_WALK);

                }

            }
        });

        setGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the next goal steps

                /*THIS IS NOT A NEXT GOAL STEPS!
                Test if the promptCoal window works

                Intent i = new Intent(getApplicationContext(), promptGoal.class);
                startActivity(i);

                should be deleted after it can be call by time or user reach the gaol*/
            }
        });

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
                Intent intent = new Intent(getApplicationContext(), SetGoalActivity.class);
                startActivity(intent);
            }
        });

        fitnessService.setup();

    }

    @Override
    protected void onResume() {
        super.onResume();
        long goalSet = prefUtil.loadLong(this, Constants.GOAL);
        stepProgress.setDailyGoal(goalSet);
        Log.d("GOAL ON RESUME", String.valueOf(stepProgress.getDailyGoal()));
        textGoal.setText(String.valueOf(stepProgress.getGoalProgress()));
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

    public void setDailyStatus() {
        boolean isOnDaily = stepLogger.readOnDaily();
        stepLogger.writeOnDaily(isOnDaily);
    }

    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount));
        prefUtil.saveLong(this, TOTAL_STEPS_TAG, stepCount);

        /*Grabs all relevant values from local file*/
        /*
        long lastSteps = stepLogger.readLastStep();
        long dailyGoal = prefUtil.loadLong(this, Constants.GOAL);
        long dailyProgress = stepLogger.readDaily();

        Log.d("CURRENT", String.valueOf(stepCount));
        Log.d("LAST", String.valueOf(lastSteps));
        long stepDifference = stepCount - lastSteps;

        stepProgress.setTotalSteps(stepLogger.readTotal());

        boolean isOnDaily = stepProgress.getOnDaily();
        Log.d("ON_DAILY", String.valueOf(isOnDaily));

        stepProgress.setDailyGoal(dailyGoal);
        stepProgress.setDailySteps(dailyProgress);

        /*Updates daily
        if (isOnDaily) {
            stepProgress.updateDaily(false, stepDifference);
        }

        /*Updates total step progress
        stepProgress.updateProgress(stepDifference);

        /*Updates step progress to determine if on daily or not
        if (stepProgress.getOnDaily() != isOnDaily)
            stepProgress.setOnDaily(isOnDaily);

        //.setText(String.valueOf(stepCount));
        Log.d("TOTAL_STEPS", String.valueOf(stepProgress.getTotalSteps()));
        Log.d("GOAL_PROGRESS", String.valueOf(stepProgress.getGoalProgress()));
        textSteps.setText(String.valueOf(stepProgress.getTotalSteps()));
        textGoal.setText(String.valueOf(dailyGoal));

        /*After all updates have finished, write to logger
        stepLogger.writeSteps(stepProgress.getDailySteps(), stepProgress.getTotalSteps(), stepCount, stepProgress.getDailyGoal());
        */
    }

}