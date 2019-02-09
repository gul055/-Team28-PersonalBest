package edu.ucsd.cse110.googlefitapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.SharedPreferencesUtils;

import edu.ucsd.cse110.googlefitapp.fitness.FitnessService;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessServiceFactory;

public class StepCountActivity extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private TextView textSteps;
    private FitnessService fitnessService;

    // Code for lab 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        textSteps = findViewById(R.id.textSteps);

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        Button btnUpdateSteps = findViewById(R.id.buttonUpdateSteps);
        btnUpdateSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount();
                showEncouragement();
            }
        });

        Button setGoalButton = findViewById(R.id.setGoal);
        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog setGoal = new GoalDialog(StepCountActivity.this, R.string.set_goal, R.string.goal_dialog, R.string.goal_hint, R.string.confirm, R.string.cancel);
                setGoal.show();
                int result = setGoal.getIntResult();
                if (result >= Constants.MINIMUM_VALID_GOAL) {
                    SharedPreferencesUtil.saveInt(StepCountActivity.this, Constants.GOAL, result);
                }
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

    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount));
        showEncouragement();
    }

    // Code for part 3 of the lab
    public void showEncouragement() {
        int steps = Integer.valueOf(textSteps.getText().toString());

        if (steps > 1000) {
            double percent = steps / 100;
            Toast toast = Toast.makeText(this, "Good job! You're already at " + percent + "% of the daily recommended number of steps.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
