package edu.ucsd.cse110.googlefitapp;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import edu.ucsd.cse110.googlefitapp.fitness.FitnessService;
import edu.ucsd.cse110.googlefitapp.fitness.FitnessServiceFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SetGoalUnitTest {

    private static final String TEST_SERVICE = "TEST_SERVICE";

    private StepCountActivity activity;
    private Button setGoalButton;

    @Before
    public void setUp() throws Exception {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(StepCountActivity stepCountActivity) {
                return new SetGoalUnitTest.TestFitnessService(stepCountActivity);
            }
        });

        Intent intent = new Intent(RuntimeEnvironment.application, StepCountActivity.class);
        intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(StepCountActivity.class, intent).create().get();

        setGoalButton = activity.findViewById(R.id.setGoal);
    }

    @Test
    public void testUpdateGoal(){
        assertEquals("Set Goal", setGoalButton.getText().toString());
        setGoalButton.performClick();

    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private StepCountActivity stepCountActivity;

        public TestFitnessService(StepCountActivity stepCountActivity) {
            this.stepCountActivity = stepCountActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            stepCountActivity.setStepCount(nextStepCount);
        }
    }
}
