/*package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.apache.tools.ant.taskdefs.Local;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import androidx.test.InstrumentationRegistry;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WalkRunUnitTest {
    private Context ctx;

    @Before
    public void setup() {
        ctx = InstrumentationRegistry.getContext();
    }

    @Test
    public void testGoodStartStop() throws Exception {
        TestWalkRun run;
        run = new TestWalkRun(ctx,63);
        LocalDateTime start = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0);
        run.startWalkRun(0, start);

        LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
        run.endWalkRun(7305, end);

        //check duration
        assertEquals(3600, run.secondsWalked());

        //check number of steps
        assertEquals(7305, run.getNumSteps());

        //check distance
        assertEquals(3.0, run.getDistance(), .001);

        //check speed
        assertEquals(3.0, run.getSpeed(), .001);

        //check stats statement
        assertEquals("Duration: 1 hour(s), 0 minute(s), 0 second(s)\n"
                + "Number of steps: 7305\n"
                + "Speed: 3.0 mph\n"
                + "Distance: 3.0 miles", run.getStats());
    }

    @Test
    public void testBadInit() {
        try {
            TestWalkRun run = new TestWalkRun(ctx,0);
            fail("Should not be able to input height <= 0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TestWalkRun run = new TestWalkRun(ctx,-1);
            fail("Should not be able to input height <= 0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStart2xNoEnd() {
        try {
            TestWalkRun run = new TestWalkRun(ctx,1);
            run.startWalkRun(0,LocalDateTime.of(2019, Month.JANUARY,1,0,0, 0));
            run.startWalkRun(0,LocalDateTime.of(2019, Month.JANUARY,1,0,0, 3));
            fail("Should not be able to start twice without ending");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStart3xNoEnd() {
        try {
            TestWalkRun run = new TestWalkRun(ctx,1);
            run.startWalkRun(0,LocalDateTime.of(2019, Month.JANUARY,1,0,0, 0));
            run.startWalkRun(0,LocalDateTime.of(2019, Month.JANUARY,1,0,0, 3));
            run.startWalkRun(0,LocalDateTime.of(2019, Month.JANUARY,1,0,0, 10));
            fail("Should not be able to start twice without ending");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEndNoStart() {
        try {
            TestWalkRun run = new TestWalkRun(ctx,1);
            run.endWalkRun(1000, LocalDateTime.of(2019, Month.JANUARY,1,0,0,0));
            fail("Should not be able to end without starting");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStartEndEnd() {
        try {
            TestWalkRun run = new TestWalkRun(ctx,1);
            LocalDateTime start = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0);
            run.startWalkRun(0, start);

            LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
            run.endWalkRun(7305, end);
            run.endWalkRun(8000, LocalDateTime.of(2020,Month.JANUARY,1,0,0,0));
            fail("Should not be able to start end end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void useSameInstanceTwice() throws Exception{
        TestWalkRun run;
        run = new TestWalkRun(ctx,63);
        LocalDateTime start = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0);
        run.startWalkRun(0, start);

        LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
        run.endWalkRun(7305, end);

        //check duration
        assertEquals(3600, run.secondsWalked());

        //check number of steps
        assertEquals(7305, run.getNumSteps());

        //check distance
        assertEquals(3.0, run.getDistance(), .001);

        //check speed
        assertEquals(3.0, run.getSpeed(), .001);

        //check stats statement
        assertEquals("Duration: 1 hour(s), 0 minute(s), 0 second(s)\n"
                + "Number of steps: 7305\n"
                + "Speed: 3.0 mph\n"
                + "Distance: 3.0 miles", run.getStats());


        LocalDateTime start2 = LocalDateTime.of(2019, Month.JANUARY,2,0,0,0);
        LocalDateTime end2 = LocalDateTime.of(2019, Month.JANUARY,2,2,0,0);
        run.startWalkRun(7305, start2);
        run.endWalkRun(9740, end2);

        //check duration
        assertEquals(7200, run.secondsWalked());

        //check number of steps
        assertEquals(2435, run.getNumSteps());

        //check distance
        assertEquals(1.0, run.getDistance(), .001);

        //check speed
        assertEquals(0.5, run.getSpeed(), .001);

        //check stats statement
        assertEquals("Duration: 2 hour(s), 0 minute(s), 0 second(s)\n"
                + "Number of steps: 2435\n"
                + "Speed: 0.5 mph\n"
                + "Distance: 1.0 miles", run.getStats());
    }

    @Test
    public void timeTravel() {
        try {
            TestWalkRun run;
            run = new TestWalkRun(ctx,63);
            LocalDateTime start = LocalDateTime.of(2019, Month.JANUARY, 2, 0, 0, 0);
            run.startWalkRun(0, start);

            LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
            run.endWalkRun(7305, end);
            fail("End time should not be before start time");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void decreaseSteps() {
        try {
            TestWalkRun run;
            run = new TestWalkRun(ctx,63);
            LocalDateTime start = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0);
            run.startWalkRun(7305, start);

            LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
            run.endWalkRun(0, end);
            fail("End steps should not be less than start steps");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void startProgressEnd() throws Exception {
        TestWalkRun run;
        run = new TestWalkRun(ctx,63);
        LocalDateTime start = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0);
        run.startWalkRun(0, start);

        LocalDateTime mid = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 30, 0);

        //check progress statement
        assertEquals("Duration: 0 hour(s), 30 minute(s), 0 second(s)\n"
                + "Number of steps: 3653\n"
                + "Speed: 3.0 mph\n"
                + "Distance: 1.5 miles", run.checkProgress(3653, mid));

        LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
        run.endWalkRun(7305, end);

        //check stats statement
        assertEquals("Duration: 1 hour(s), 0 minute(s), 0 second(s)\n"
                + "Number of steps: 7305\n"
                + "Speed: 3.0 mph\n"
                + "Distance: 3.0 miles", run.getStats());

    }

    @Test
    public void startProgressx2End() throws Exception {
        TestWalkRun run;
        run = new TestWalkRun(ctx,63);
        LocalDateTime start = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0);
        run.startWalkRun(0, start);

        LocalDateTime mid = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 30, 0);

        //check progress statement
        assertEquals("Duration: 0 hour(s), 30 minute(s), 0 second(s)\n"
                + "Number of steps: 3653\n"
                + "Speed: 3.0 mph\n"
                + "Distance: 1.5 miles", run.checkProgress(3653, mid));

        LocalDateTime mid2 = LocalDateTime.of(2019, Month.JANUARY, 1, 2, 0, 0);

        //check stats statement
        assertEquals("Duration: 2 hour(s), 0 minute(s), 0 second(s)\n"
                + "Number of steps: 7305\n"
                + "Speed: 1.5 mph\n"
                + "Distance: 3.0 miles", run.checkProgress(7305, mid2));

        LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 3, 0, 0);
        run.endWalkRun(24350, end);

        //check stats statement
        assertEquals("Duration: 3 hour(s), 0 minute(s), 0 second(s)\n"
                + "Number of steps: 24350\n"
                + "Speed: 3.3 mph\n"
                + "Distance: 10.0 miles", run.getStats());
    }

    @Test
    public void startEndProgress() throws Exception{
        TestWalkRun run;
        run = new TestWalkRun(ctx,63);
        LocalDateTime start = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 0, 0);
        run.startWalkRun(0, start);

        LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
        run.endWalkRun(7305, end);

        //check stats statement
        assertEquals("Duration: 1 hour(s), 0 minute(s), 0 second(s)\n"
                + "Number of steps: 7305\n"
                + "Speed: 3.0 mph\n"
                + "Distance: 3.0 miles", run.getStats());

        try {
            LocalDateTime mid = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 30, 0);

            //check progress statement
            assertEquals("Duration: 0 hour(s), 30 minute(s), 0 second(s)\n"
                    + "Number of steps: 3653\n"
                    + "Speed: 3.0 mph\n"
                    + "Distance: 1.5 miles", run.checkProgress(3653, mid));

            fail("Should not be able to check progress on a WalkRun that has ended");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void progressEndNoStart() throws Exception{
        TestWalkRun run;
        run = new TestWalkRun(ctx,63);

        try {
            LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
            run.endWalkRun(7305, end);
            //check stats statement
            assertEquals("Duration: 1 hours, 0 minutes, 0 seconds\n"
                    + "Number of steps: 7305\n"
                    + "Speed: 3.0 mph\n"
                    + "Distance: 3.0 miles", run.getStats());
            fail("Can't end a WalkRun that hasn't started");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            LocalDateTime mid = LocalDateTime.of(2019, Month.JANUARY, 1, 0, 30, 0);

            //check progress statement
            assertEquals("Duration: 0 hours, 30 minutes, 0 seconds\n"
                    + "Number of steps: 3653\n"
                    + "Speed: 3.0 mph\n"
                    + "Distance: 1.5 miles", run.checkProgress(3653, mid));

            fail("Should not be able to check progress on a WalkRun that has not started");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            LocalDateTime end = LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0, 0);
            run.endWalkRun(7305, end);
            //check stats statement
            assertEquals("Duration: 1 hours, 0 minutes, 0 seconds\n"
                    + "Number of steps: 7305\n"
                    + "Speed: 3.0 mph\n"
                    + "Distance: 3.0 miles", run.getStats());
            fail("Can't end a WalkRun that hasn't started");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TestWalkRun extends WalkRun {

        public TestWalkRun(Context ctx, int userHeight) throws Exception {
            super(ctx, userHeight);
        }

        public void startWalkRun(int initSteps, LocalDateTime s) throws Exception {
            //every start must be met with an end
            boolean isStarted = SharedPreferencesUtil.loadBoolean(context, Constants.STARTED_TAG);
            if (!isStarted) {
                //haredPreferences.Editor editor = sharedPref.edit();

                //initial step count must be valid
                if (initSteps >= 0) {
                    SharedPreferencesUtil.saveInt(context, Constants.STARTSTEPS_TAG, initSteps);
                    SharedPreferencesUtil.saveLong(context, Constants.STARTTIME_TAG, Duration.between(refTime, s).getSeconds());
                    SharedPreferencesUtil.saveBoolean(context, Constants.STARTED_TAG, true);
                    SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, false);
                } else {
                    throw new Exception("Invalid: negative initial step count");
                }
            } else {
                throw new Exception("Invalid: this WalkRun has already started");
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void endWalkRun(int finalSteps, LocalDateTime e) throws Exception {
            //can only end WalkRun that has already started
            boolean isStarted = SharedPreferencesUtil.loadBoolean(context, Constants.STARTED_TAG);
            if (isStarted) {
                if (finalSteps < 0) {
                    throw new Exception("Invalid: negative final step count");
                }
                //cannot decrease the amount of steps taken on a walk
                int startSteps = SharedPreferencesUtil.loadInt(context, Constants.STARTSTEPS_TAG);
                if(startSteps == -1)
                    startSteps = Integer.MAX_VALUE;
                if (finalSteps >= startSteps) {

                    long start = SharedPreferencesUtil.loadLong(context, Constants.STARTTIME_TAG);
                    long end = Duration.between(refTime, e).getSeconds();

                    //cannot end walk at a time before it is started
                    if (end - start < 0) {
                        throw new Exception("Invalid: End time < start time");
                    }
                    else {
                        //SharedPreferences.Editor editor = sharedPref.edit();

                        //update the WalkRun
                        SharedPreferencesUtil.saveInt(context, Constants.ENDSTEPS_TAG, finalSteps);
                        SharedPreferencesUtil.saveLong(context, Constants.ENDTIME_TAG, end);
                        SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, true);
                        //editor.apply();

                        reset();
                    }
                } else {
                    throw new Exception("Invalid: Steps DECREASED on WalkRun.\n");
                }
            } else {
                throw new Exception("Invalid: attempt to end WalkRun before starting: sharedPref boolean is " + sharedPref.getBoolean("started", false));
            }
        }

        public String checkProgress(int pSteps, LocalDateTime ptime) throws Exception {
            boolean isStarted = SharedPreferencesUtil.loadBoolean(context, Constants.STARTED_TAG);
            boolean isOk = SharedPreferencesUtil.loadBoolean(context, Constants.OK_TAG);
            if (isStarted) {
                if (!isOk) {
                    //SharedPreferences.Editor editor = sharedPref.edit();

                    SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, true);
                    long end = Duration.between(refTime, ptime).getSeconds();

                    //update WalkRun prefs with the progress time and steps
                    SharedPreferencesUtil.saveLong(context, Constants.ENDTIME_TAG, end);
                    SharedPreferencesUtil.saveInt(context, Constants.ENDSTEPS_TAG, pSteps);

                    //int endSteps = SharedPreferencesUtil.loadInt(context, Constants.ENDSTEPS_TAG);
                    String progress = getStats();

                    //WalkRun is still incomplete

                    SharedPreferencesUtil.saveBoolean(context, Constants.OK_TAG, false);
                    SharedPreferencesUtil.saveBoolean(context, Constants.STARTED_TAG, true);

                    //Progress return string with stats
                    return progress;
                } else {
                    throw new Exception("No WalkRun in progress");
                }
            } else {
                throw new Exception("No WalkRun in progress");
            }
        }

    }
}*/