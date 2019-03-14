package edu.ucsd.cse110.googlefitapp;

import org.junit.*;

import edu.ucsd.cse110.googlefitapp.stepupdaters.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StepUpdaterUnitTest {

    StepUpdater updater;
    StepUpdater stepProgress;

    @Before
    public void setup(){
        //updater = new StepUpdater();
    }

    /*Basic constructor test*/
    @Test
    public void testConstructor(){
        /*assertEquals(5000, updater.getDailyGoal());
        assertEquals(0, updater.getTotalSteps());
        assertEquals(0, updater.getDailySteps());*/
    }

    /*Test goal progress logic*/
    @Test
    public void testGoalProgress(){
        /*Basic use case, person walks 50 steps*/
        /*
        assertFalse(updater.updateDaily(50));
        assertEquals(4950, updater.getGoalProgress());
        assertEquals(50, updater.getDailySteps());

        /*Check for successful run*/
        /*assertFalse(updater.updateDaily( 4999));
        assertTrue(updater.updateDaily( 1));
        assertEquals(0, updater.getDailySteps());


        /*Average steps taken in human lifespan*/
        /*assertTrue(updater.updateDaily( 216262500));
        assertEquals(0, updater.getDailySteps());
        */
    }
}
