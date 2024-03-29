package edu.ucsd.cse110.googlefitapp.Goals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.R;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class SetGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

        Button confirmButton = findViewById(R.id.buttonConfirm);
        Button cancelButton = findViewById(R.id.buttonCancel);
        final EditText goal = findViewById(R.id.goalInput);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long goalNum = 0;
                Goal setGoal = new SetGoal(getApplicationContext());
                try {
                    goalNum = Long.parseLong(goal.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), Constants.NO_GOAL, Toast.LENGTH_LONG).show();
                    finish();
                }
                boolean result = setGoal.set(goalNum);
                if (result == true) {
                    Toast.makeText(getApplicationContext(), Constants.SET_SUCCESS, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), Constants.SET_FAIL, Toast.LENGTH_LONG).show();
                }
                SharedPreferencesUtil.saveBoolean(getApplicationContext(), Constants.NOT_NOW_PRESS, false);
                SharedPreferencesUtil.saveLong(getApplicationContext(), Constants.GOAL_MET_TAG, 0);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
