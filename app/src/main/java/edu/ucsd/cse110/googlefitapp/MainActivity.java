package edu.ucsd.cse110.googlefitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.ucsd.cse110.googlefitapp.R;
import edu.ucsd.cse110.googlefitapp.StepCountActivity;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessService;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.googlefitapp.fitness.GoogleFitAdapter;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepProgress;

public class MainActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";

    public StepProgress stepProgress = new StepProgress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create all buttons
        Button startStopBtn = (Button) findViewById(R.id.startStopBtn);
        Button setGoalBtn = (Button) findViewById(R.id.setGoalBtn);
        Button showStepsBtn = (Button) findViewById(R.id.showStepsBtn);

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Turn fitness steps tracking on/off
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
                // Display the stepCount in stepView TextView
            }
        });


        /*btnGoToSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStepCountActivity();
            }
        });
*/

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(StepCountActivity stepCountActivity) {
                return new GoogleFitAdapter(stepCountActivity);
            }
        });
    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, StepCountActivity.class);
        intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        startActivity(intent);
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }
}
