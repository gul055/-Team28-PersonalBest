package edu.ucsd.cse110.googlefitapp.stepupdaters;

import java.util.Calendar;

import static edu.ucsd.cse110.googlefitapp.Constants.MAIN;
import static edu.ucsd.cse110.googlefitapp.Constants.MAIN_ENCOURAGEMENT;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB_ENCOURAGEMENT1;
import static edu.ucsd.cse110.googlefitapp.Constants.SUB_ENCOURAGEMENT2;

public class EncourageFactory {


    public EncourageFactory() {}

    /**
     * buildMsg - determines the kind of encouragement message to be made and builds it
     *                    "MAIN" = MainEncouragement
     *                    "SUB" = SubEncouragement
     */
    public EncourageMsg buildMsg(int msgType, StepUpdater stepUpdater, long prevSteps) {
        switch(msgType) {
            case MAIN: {
                return new MainEncourageMsg(Calendar.getInstance().getTime(), MAIN_ENCOURAGEMENT);
            }
            case SUB: {
                return new SubEncourageMsg(Calendar.getInstance().getTime(),
                                SUB_ENCOURAGEMENT1,
                                Math.abs(stepUpdater.getGoalProgress()),
                                SUB_ENCOURAGEMENT2);
            }
            default:
                return null;
        }
    }
}
