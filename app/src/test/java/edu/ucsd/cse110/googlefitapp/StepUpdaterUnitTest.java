package edu.ucsd.cse110.googlefitapp;

import org.junit.*;

import edu.ucsd.cse110.googlefitapp.stepupdaters.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StepUpdaterUnitTest {
    StepUpdater updater;
    @Before
    public void setup(){
        updater = new StepUpdater();
    }

    /*Basic constructor test*/
    @Test
    public void testConstructor(){
        assertEquals(5000, updater.getDailyGoal());
        assertEquals(0, updater.getTotalSteps());
        assertFalse(updater.getOnDaily());
        assertEquals(0, updater.getDailySteps());
    }

    /*Test goal progress logic*/
    @Test
    public void testGoalProgress(){
        /*Basic use case, person walks 50 steps*/
        assertFalse(updater.updateDaily(false, 50));
        assertEquals(4950, updater.getGoalProgress());
        assertEquals(50, updater.getDailySteps());

        /*Check if reset works correctly.*/
        assertFalse(updater.updateDaily(true, 500));
        assertEquals(0, updater.getDailySteps());

        /*Check for successful run*/
        assertFalse(updater.updateDaily(false, 4999));
        assertTrue(updater.updateDaily(false, 1));
        assertEquals(0, updater.getDailySteps());

        /*Average steps taken in human lifespan*/
        assertTrue(updater.updateDaily(false, 216262500));
        assertEquals(0, updater.getDailySteps());
    }

    /*Test total steps update*/
    @Test
    public void testTotalSteps(){
        /*Normal small case*/
        updater.updateProgress(50);
        assertEquals(50, updater.getTotalSteps());
        /*Normal medium case*/
        updater.updateProgress(1500);
        /*Normal large case*/
        assertEquals(1550, updater.getTotalSteps());
        updater.updateProgress(15961);
        assertEquals(17511, updater.getTotalSteps());
        /*No steps walked*/
        updater.updateProgress(0);
        assertEquals(17511, updater.getTotalSteps());
    }
}
