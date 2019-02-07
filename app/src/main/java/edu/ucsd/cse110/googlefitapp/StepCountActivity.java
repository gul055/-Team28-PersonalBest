package edu.ucsd.cse110.googlefitapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class StepCountActivity extends AppCompatActivity {

    public StepLogger stepLogger;
    public StepUpdater stepProgress = new StepUpdater();

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private FitnessService fitnessService;

    // Code for lab 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        stepLogger = new StepLogger(this);

        fitnessService.setup();

        TextView textSteps = (TextView) findViewById(R.id.textSteps);
        final TextView textGoal = (TextView) findViewById(R.id.textGoal);

        textSteps.setText(String.valueOf(stepProgress.getTotalSteps()));
        textGoal.setText(String.valueOf(stepProgress.getGoalProgress()));

        // Create all buttons
        final Button startStopBtn = (Button) findViewById(R.id.startStopBtn);
        Button setGoalBtn = (Button) findViewById(R.id.setGoalBtn);
        Button showStepsBtn = (Button) findViewById(R.id.showStepsBtn);

        if(stepLogger.readOnDaily() == false) {
            stepLogger.writeOnDaily(false);
            startStopBtn.setBackgroundColor(Color.GREEN);
            startStopBtn.setText("Start Walk/Run");
        }
        else {
            stepLogger.writeOnDaily(true);
            startStopBtn.setBackgroundColor(Color.RED);
            startStopBtn.setText("Stop Walk/Run");
        }

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stepLogger.readOnDaily() == true) {
                    stepLogger.writeOnDaily(false);
                    startStopBtn.setBackgroundColor(Color.RED);
                    startStopBtn.setText("Stop Walk/Run");
                }
                else {
                    stepLogger.writeOnDaily(true);
                    startStopBtn.setBackgroundColor(Color.GREEN);
                    startStopBtn.setText("Start Walk/Run");
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
                //fitnessService.updateStepCount();
                TextView textSteps = (TextView) findViewById(R.id.textSteps);
                TextView textGoal = (TextView) findViewById(R.id.textGoal);
                //textSteps.setText((String.valueOf(stepProgress.getTotalSteps())));
                //textGoal.setText((String.valueOf(stepProgress.getGoalProgress())));
                textSteps.setText(String.valueOf(stepProgress.getTotalSteps()));
                textGoal.setText(String.valueOf(stepProgress.getGoalProgress()));
                //showEncouragement();
            }
        });


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
        long dailySteps = stepLogger.readDaily();
        long totalSteps = stepLogger.readTotal();
        long lastSteps = stepLogger.readLastStep();
        long dailyGoal = stepLogger.readGoal();
        boolean isOnDaily = stepLogger.readOnDaily();
        long stepDifference = stepCount - lastSteps;

        stepProgress.setDailyGoal(dailyGoal);

        /*Updates daily*/
        if(isOnDaily){
            stepProgress.updateDaily(false, stepDifference);
        }

        /*Updates total step progress*/
        stepProgress.updateProgress(stepDifference);

        /*Updates step progress to determine if on daily or not*/
        if(stepProgress.getOnDaily() != isOnDaily)
            stepProgress.setOnDaily(isOnDaily);

        /*After all updates have finished, write to logger*/
        stepLogger.writeSteps(stepProgress.getDailySteps(),stepCount,totalSteps, stepProgress.getDailyGoal());

    }

    // Code for part 3 of the lab
   /* public void showEncouragement() {
        Context context = getApplicationContext();
        int steps = Integer.parseInt(textSteps.toString());
        int percentSteps = (int)  (steps / 100);
        CharSequence text = "Good job! You're already at" +  percentSteps + "% of the daily recommended number of steps.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);

        // Determine if the toast should be shown
        if( steps >= 1000) {
            toast.show();
        }
    }*/

}