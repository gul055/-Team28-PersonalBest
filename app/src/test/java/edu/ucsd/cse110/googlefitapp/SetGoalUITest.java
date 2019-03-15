/*package edu.ucsd.cse110.googlefitapp;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

import edu.ucsd.cse110.googlefitapp.Goals.SetGoalActivity;

@RunWith(RobolectricTestRunner.class)
public class SetGoalUITest {

    private SetGoalActivity activity;
    private EditText goalField;
    private Button confirm;

    @Before
    public void setUp() {
        Intent intent = new Intent(RuntimeEnvironment.application, SetGoalActivity.class);
        activity = Robolectric.buildActivity(SetGoalActivity.class, intent).create().get();
        goalField = activity.findViewById(R.id.goalInput);
        confirm = activity.findViewById(R.id.buttonConfirm);
    }

    @Test
    public void testValidInputUI() {
        goalField.setText("1000");
        confirm.performClick();
        ShadowToast.getTextOfLatestToast().equals("New goal set.");
    }

    @Test
    public void testInvalidInputUI() {
        goalField.setText("0");
        confirm.performClick();
        ShadowToast.getTextOfLatestToast().equals("Invalid goal. Please try again.");
    }
}*/
