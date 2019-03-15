/*package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import edu.ucsd.cse110.googlefitapp.Goals.Goal;
import edu.ucsd.cse110.googlefitapp.Goals.SetGoal;
import edu.ucsd.cse110.googlefitapp.Goals.SetGoalActivity;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SetGoalUnitTest {

    private Goal setGoal;
    private SetGoalActivity activity;
    private Context fakeContext;
    private EditText goalField;

    @Before
    public void setUp() {
        Intent intent = new Intent(RuntimeEnvironment.application, SetGoalActivity.class);
        activity = Robolectric.buildActivity(SetGoalActivity.class, intent).create().get();
        fakeContext = activity.getApplicationContext();
        setGoal = new SetGoal(fakeContext);
        goalField = activity.findViewById(R.id.goalInput);
    }

    @Test
    public void testValidInput() {
        boolean result = setGoal.set(1000);
        assertTrue(result);
        assertEquals(1000, SharedPreferencesUtil.loadLong(fakeContext, Constants.GOAL));
    }

    @Test
    public void testInvalidInput() {
        boolean result = setGoal.set(0);
        assertFalse(result);
    }


}*/
