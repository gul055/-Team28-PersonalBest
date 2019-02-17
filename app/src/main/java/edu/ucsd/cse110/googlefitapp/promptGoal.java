package edu.ucsd.cse110.googlefitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.ucsd.cse110.googlefitapp.stepupdaters.StepUpdater;

public class promptGoal extends AppCompatActivity {

    public StepUpdater stepProgress;
    public SharedPreferencesUtil prefUtil;
    private boolean notNowPress = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_goal);

        stepProgress = new StepUpdater(this);

        Button autoGoalBtn = (Button) findViewById(R.id.autoGoalBtn);
        Button notNowBtn = (Button) findViewById(R.id.notNowBtn);


        autoGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Goal setGoal = new SetGoal(getApplicationContext());
                long currGoal = stepProgress.getDailyGoal();
                boolean result = setGoal.set(currGoal + Constants.PRESET_INCREMENT);
                if (result) {
                    Toast.makeText(getApplicationContext(), Constants.SET_SUCCESS, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), Constants.SET_FAIL, Toast.LENGTH_LONG).show();
                }
                prefUtil.saveBoolean(getApplicationContext(), Constants.NOT_NOW_PRESS, false);
                prefUtil.saveLong(getApplicationContext(), Constants.GOAL_MET_TAG, 0);
                finish();
            }
        });

        notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefUtil.saveBoolean(getApplicationContext(), Constants.NOT_NOW_PRESS, !notNowPress);
                finish();
            }
        });
    }
}
