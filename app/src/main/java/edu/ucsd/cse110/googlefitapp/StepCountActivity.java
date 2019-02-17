package edu.ucsd.cse110.googlefitapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

import static edu.ucsd.cse110.googlefitapp.Constants.DAILY_STEPS_TAG;
import static edu.ucsd.cse110.googlefitapp.Constants.GOAL;
import static edu.ucsd.cse110.googlefitapp.Constants.LAST_UPDATE_TAG;
import static edu.ucsd.cse110.googlefitapp.Constants.PRESET_INCREMENT;
import static edu.ucsd.cse110.googlefitapp.Constants.TOTAL_STEPS_TAG;

public class StepCountActivity extends AppCompatActivity {

    public StepLogger stepLogger;
    public static StepUpdater stepProgress = new StepUpdater();

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

        fitnessService.setup();

        // Create all buttons
        final Button startStopBtn = (Button) findViewById(R.id.startStopBtn);
        Button setGoalBtn = (Button) findViewById(R.id.setGoalBtn);

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    @Override
    protected void onResume() {
        super.onResume();
        fitnessService.updateStepCount();
        long goalSet = SharedPreferencesUtil.loadLong(this, Constants.GOAL);

        /*Set goal*/
        //TODO: SRP this pls
        if(goalSet == 0){
            stepProgress.setDailyGoal(5000);
        }
        else{
            stepProgress.setDailyGoal(goalSet);
        }

        /*Set the steps for stepUpdater*/
        stepProgress.setTotalSteps(SharedPreferencesUtil.loadLong(this, Constants.TOTAL_STEPS_TAG));

        /*Set and calculate goal progress*/
        long startSteps = SharedPreferencesUtil.loadLong(this, Constants.STARTSTEPS_TAG);
        stepProgress.updateDaily(stepProgress.getTotalSteps() - startSteps);
        textGoal.setText(String.valueOf(stepProgress.getGoalProgress()));
        Log.d("GOAL ON RESUME", String.valueOf(stepProgress.getDailyGoal()));
        Log.d("GOAL_PROGRESS", String.valueOf(String.valueOf(stepProgress.getGoalProgress())));
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
        stepProgress.setTotalSteps(stepCount);
        textSteps.setText(String.valueOf(stepCount));
        SharedPreferencesUtil.saveLong(this, TOTAL_STEPS_TAG, stepCount);
    }

    /*ALL CREDIT FOR THE FOLLOWING ASYNCTASK CODE GOES TO THE TUTSPLUS TUTORIAL ON GOOGLE FIT API
    Title: Google Fit for Android: History API
    https://code.tutsplus.com/tutorials/google-fit-for-android-history-api--cms-25856
    Captured: 2/15/2019
    How the source was used: Copied code
    K.D.
    */
    //TODO: CALL THIS FROM SOMEWHERE.
    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("INASYNC", "In task");
            fitnessService.getWeeklyData();
            return null;
        }
    }

}