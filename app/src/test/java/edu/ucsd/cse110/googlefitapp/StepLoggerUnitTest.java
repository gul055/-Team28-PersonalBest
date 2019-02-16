package edu.ucsd.cse110.googlefitapp;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import edu.ucsd.cse110.googlefitapp.stepupdaters.StepLogger;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class StepLoggerUnitTest {

    private StepLogger stepLogger;
    private MainActivity activity;

    @Before
    public void setup() {
        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();
        stepLogger = new StepLogger(activity.getApplicationContext());
    }

    @Test
    public void testReadWriteSteps() {
        stepLogger.writeSteps(50, 100, 20, 500);
        assertEquals(50, stepLogger.readDaily());
        assertEquals(100, stepLogger.readTotal());
        assertEquals(20, stepLogger.readLastStep());
        assertEquals(500, stepLogger.readGoal());
    }

    @Test
    public void testReadWriteDailyBool() {
        stepLogger.writeOnDaily(true);
        assertTrue(stepLogger.readOnDaily());
        stepLogger.writeOnDaily(false);
        assertFalse(stepLogger.readOnDaily());
    }
}
