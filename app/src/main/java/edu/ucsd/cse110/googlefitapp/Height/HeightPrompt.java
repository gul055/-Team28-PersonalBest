package edu.ucsd.cse110.googlefitapp.Height;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.ucsd.cse110.googlefitapp.R;
import edu.ucsd.cse110.googlefitapp.StepCountActivity;


public class HeightPrompt extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";
    public HeightLogger heightLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_prompt);

        heightLog = new HeightLogger(this);
        Button okButton = findViewById(R.id.button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText EditFeet = findViewById(R.id.userFeet);
                EditText EditInches = findViewById(R.id.userInches);
                try {
                    Long feet = Long.parseLong(EditFeet.getText().toString());
                    Long inches = Long.parseLong(EditInches.getText().toString());
                    if (heightLog.writeHeight(feet, inches)) {
                        launchStepCountActivity();
                        Toast.makeText(HeightPrompt.this, "Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HeightPrompt.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ex) {
                    Toast.makeText(HeightPrompt.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, StepCountActivity.class);
        intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        startActivity(intent);
    }

}
