package edu.ucsd.cse110.googlefitapp;

import org.apache.tools.ant.taskdefs.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WalkRunUnitTest {
    private final static double strideMultiplier = .413;
    private final static int inchesInFeet = 12;
    private final static int feetInMile = 5280;
    private final static int secondsInHour = 3600;
    private final static int secondsInMinute = 60;

    @Test
    public void testGoodStartStop() throws Exception {
        TestWalkRun run;
        run = new TestWalkRun(63);
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
    }

    @Test
    public void testBadInit() {
        try {
            TestWalkRun run = new TestWalkRun(0);
            fail("Should not be able to input height <= 0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TestWalkRun run = new TestWalkRun(-1);
            fail("Should not be able to input height <= 0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStart2xNoEnd() {
        try {
            TestWalkRun run = new TestWalkRun(1);
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
            TestWalkRun run = new TestWalkRun(1);
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
            TestWalkRun run = new TestWalkRun(1);
            run.endWalkRun(1000, LocalDateTime.of(2019, Month.JANUARY,1,0,0,0));
            fail("Should not be able to end without starting");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStartEndEnd() {
        try {
            TestWalkRun run = new TestWalkRun(1);
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
        run = new TestWalkRun(63);
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
    }

    private class TestWalkRun extends WalkRun {
        public TestWalkRun(int userHeight) throws Exception {
            super(userHeight);
        }

        public void startWalkRun(int initSteps, LocalDateTime s) throws Exception {
            //every start must be met with an end
            if(!started) {
                if(initSteps >= 0) {
                    startSteps = initSteps;
                }
                else {
                    throw new Exception("Invalid: negative initial step count");
                }

                startTime = s;
                started = true;
                ok = false;
            }
            else {
                throw new Exception("Invalid: this WalkRun has already started");
            }
        }

        public void endWalkRun(int finalSteps, LocalDateTime e) throws Exception {
            //can only end WalkRun that has already started
            if(started) {
                if(finalSteps < 0) {
                    throw new Exception("Invalid: negative final step count");
                }

                //cannot decrease the amount of steps taken on a walk
                if(finalSteps >= startSteps) {
                    endTime = e;
                    endSteps = finalSteps;
                    started = false;
                    ok = true;
                }
                else {
                    throw new Exception("Invalid: Steps DECREASED on WalkRun.\n" +
                            "Start steps: " + startSteps + ". End steps: " + endSteps);
                }
            }
            else {
                throw new Exception("Invalid: attempt to end WalkRun before starting");
            }
        }

    }
}