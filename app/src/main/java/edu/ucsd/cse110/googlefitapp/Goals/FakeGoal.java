package edu.ucsd.cse110.googlefitapp.Goals;

import edu.ucsd.cse110.googlefitapp.Constants;

public class FakeGoal implements Goal {
    @Override
    public boolean set(long goalCandidate) {
        if (goalCandidate >= Constants.MINIMUM_VALID_GOAL) {
            return true;
        }
        return false;
    }
}
