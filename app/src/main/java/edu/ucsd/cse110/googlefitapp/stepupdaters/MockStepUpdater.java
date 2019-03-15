package edu.ucsd.cse110.googlefitapp.stepupdaters;

import android.content.Context;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;

public class MockStepUpdater extends StepUpdater {

    long additionalSteps;

    public MockStepUpdater(Context c) {
        super(c);
        long prevAdditional = SharedPreferencesUtil.loadLong(c, Constants.ADDITIONAL_STEPS);
        if (prevAdditional == -1) {
            additionalSteps = 0;
        } else {
            additionalSteps = prevAdditional;
        }
    }

    @Override
    public long getTotalSteps() {
        long steps = super.getTotalSteps();
        return additionalSteps + steps;
    }

    @Override
    public void addTotalSteps(long additionalStep) {
        this.additionalSteps += additionalStep;
        SharedPreferencesUtil.saveLong(c, Constants.ADDITIONAL_STEPS, additionalSteps);
    }
}
