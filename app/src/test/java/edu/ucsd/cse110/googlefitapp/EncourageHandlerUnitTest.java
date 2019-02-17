package edu.ucsd.cse110.googlefitapp;

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

import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.googlefitapp.stepupdaters.EncourageFactory;
import edu.ucsd.cse110.googlefitapp.stepupdaters.EncourageHandler;
import edu.ucsd.cse110.googlefitapp.stepupdaters.EncourageMsg;
import edu.ucsd.cse110.googlefitapp.stepupdaters.MainEncourageMsg;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepUpdater;
import edu.ucsd.cse110.googlefitapp.stepupdaters.SubEncourageMsg;

import static edu.ucsd.cse110.googlefitapp.Constants.MAIN;
import static edu.ucsd.cse110.googlefitapp.Constants.MAIN_ENCOURAGEMENT;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB_ENCOURAGEMENT1;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB_ENCOURAGEMENT2;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class EncourageHandlerUnitTest {
    private StepCountActivity activity;
    private EncourageHandler handler;
    private StepUpdater stepUpdater;
    private Date date;

    @Before
    public void setUp() {
        Intent intent = new Intent(RuntimeEnvironment.application, StepCountActivity.class);
        activity = Robolectric.buildActivity(StepCountActivity.class, intent).create().get();
        stepUpdater = new StepUpdater(activity.getApplicationContext());
        handler = new EncourageHandler(activity.getApplicationContext(), stepUpdater);
        stepUpdater.setDailyGoal(5000);
        stepUpdater.updateDaily(3000);
        date = Calendar.getInstance().getTime();

    }

    @Test
    public void testUpdateGoalMet() {
        stepUpdater.updateDaily(2000);
        handler.update();
        assertEquals(0, handler.getCurrSteps()); //goal met
        assertTrue(handler.isMainEncourageGiven());
    }

    @Test
    public void testSubMet() {
        stepUpdater.updateDaily(1999);
        handler.update();
        handler.setHour(21); //9pm to trigger subgoal
        assertEquals(4999, handler.getCurrSteps()); // today's steps at least 500 > yest
        assertTrue(!handler.isMainEncourageGiven());
        assertTrue(handler.isSubEncourageGiven());
    }

    @Test
    public void testSubEncourageBuild() {

    }

    @Test
    public void testNullBuild() {

    }
}
