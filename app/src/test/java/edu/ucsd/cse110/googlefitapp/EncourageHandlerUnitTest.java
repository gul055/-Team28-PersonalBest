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
import edu.ucsd.cse110.googlefitapp.stepupdaters.EncourageMsg;
import edu.ucsd.cse110.googlefitapp.stepupdaters.MainEncourageMsg;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepUpdater;
import edu.ucsd.cse110.googlefitapp.stepupdaters.SubEncourageMsg;

import static edu.ucsd.cse110.googlefitapp.Constants.MAIN;
import static edu.ucsd.cse110.googlefitapp.Constants.MAIN_ENCOURAGEMENT;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB_ENCOURAGEMENT1;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB_ENCOURAGEMENT2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class EncourageHandlerUnitTest {
    private StepCountActivity activity;
    private EncourageFactory factory;
    private StepUpdater stepUpdater;
    private Date date;

    @Before
    public void setUp() {
        Intent intent = new Intent(RuntimeEnvironment.application, StepCountActivity.class);
        activity = Robolectric.buildActivity(StepCountActivity.class, intent).create().get();
        factory = new EncourageFactory();
        stepUpdater = new StepUpdater(activity.getApplicationContext());
        stepUpdater.setDailyGoal(5000);
        stepUpdater.setTotalSteps(3000);
        date = Calendar.getInstance().getTime();
    }

    @Test
    public void testMainEncourageBuild() {
        EncourageMsg msg = factory.buildMsg(MAIN, stepUpdater, 1000);
        assertEquals(MainEncourageMsg.class, msg.getClass());
        assertEquals(MAIN_ENCOURAGEMENT, msg.getMessage());
        assertTrue(date.compareTo(msg.getDate()) < 0);
    }

    @Test
    public void testSubEncourageBuild() {
        stepUpdater.updateDaily(3500);
        EncourageMsg msg = factory.buildMsg(SUB, stepUpdater, 3000);
        assertEquals(SubEncourageMsg.class, msg.getClass());
        assertEquals(SUB_ENCOURAGEMENT1 + "500" + SUB_ENCOURAGEMENT2, msg.getMessage());
        assertTrue(date.compareTo(msg.getDate()) < 0);
    }

    @Test
    public void testNullBuild() {
        stepUpdater.updateDaily(3500);
        EncourageMsg msg = factory.buildMsg(5, stepUpdater, 3000);
        assertEquals(null, msg);
    }
}
