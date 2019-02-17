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

import edu.ucsd.cse110.googlefitapp.stepupdaters.EncourageFactory;
import edu.ucsd.cse110.googlefitapp.stepupdaters.EncourageMsg;
import edu.ucsd.cse110.googlefitapp.stepupdaters.MainEncourageMsg;
import edu.ucsd.cse110.googlefitapp.stepupdaters.StepUpdater;

import static edu.ucsd.cse110.googlefitapp.Constants.MAIN;
import static edu.ucsd.cse110.googlefitapp.Constants.MAIN_ENCOURAGEMENT;
import static org.junit.Assert.assertEquals;


/**
 * Tests the functionality of the EncourageFactory class
 */
@RunWith(RobolectricTestRunner.class)
public class EncourageFactoryUnitTest {

        private StepCountActivity activity;
        private EncourageFactory factory;
        private StepUpdater stepUpdater;

        @Before
        public void setUp() {
           factory = new EncourageFactory();
           stepUpdater = new StepUpdater(activity.getApplicationContext());
        }

        @Test
        public void testMainEncourageBuild() {
            EncourageMsg msg = factory.buildMsg(MAIN, stepUpdater, 1000);
            assertEquals(MainEncourageMsg.class, msg.getClass());
            assertEquals(MAIN_ENCOURAGEMENT, msg.getMessage());
            assertEquals();
        }

        @Test
        public void testInvalidInputUI() {
            goalField.setText("0");
            confirm.performClick();
            ShadowToast.getTextOfLatestToast().equals("Invalid goal. Please try again.");
        }
    }

}
