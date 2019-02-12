package edu.ucsd.cse110.googlefitapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ucsd.cse110.googlefitapp.fitness.FitnessService;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepLogger;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepUpdater;

public class StepCountActivity extends AppCompatActivity {

    public StepLogger stepLogger;
    public HeightLogger heightLogger;
    public StepUpdater stepProgress = new StepUpdater();

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private FitnessService fitnessService;

    private TextView textSteps, textGoal, textHeight;

    SharedPreferences sharedPref;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        
        // request height for first sign in
        heightLogger = new HeightLogger(this);
        if (heightLogger.readHeight() == 0) {
            Toast.makeText(StepCountActivity.this, "You Have Not Assign Height Yet", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(StepCountActivity.this, HeightPrompt.class);
            startActivity(intent);
            return;
        }

        sharedPref = getApplicationContext().getSharedPreferences("height_data", MODE_PRIVATE);
        long height = sharedPref.getLong("height", 0);
        textSteps = findViewById(R.id.textSteps);
        textGoal = findViewById(R.id.textGoal);
        textHeight = findViewById(R.id.textHeight);
        textHeight.setText(Long.toString(height));
        final String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        stepLogger = new StepLogger(this);

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
        /*Grabs all relevant values from local file*/
        long lastSteps = stepLogger.readLastStep();
        long dailyGoal = stepLogger.readGoal();
        long dailyProgress = stepLogger.readDaily();

        Log.d("CURRENT", String.valueOf(stepCount));
        Log.d("LAST", String.valueOf(lastSteps));
        long stepDifference = stepCount - lastSteps;

        stepProgress.setTotalSteps(stepLogger.readTotal());

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

        //.setText(String.valueOf(stepCount));
        Log.d("TOTAL_STEPS", String.valueOf(stepProgress.getTotalSteps()));
        Log.d("GOAL_PROGRESS", String.valueOf(stepProgress.getGoalProgress()));
        textSteps.setText(String.valueOf(stepProgress.getTotalSteps()));
        textGoal.setText(String.valueOf(stepProgress.getGoalProgress()));

        /*After all updates have finished, write to logger*/
        stepLogger.writeSteps(stepProgress.getDailySteps(), stepProgress.getTotalSteps(), stepCount, stepProgress.getDailyGoal());

    }

}
