package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Calendars.MockCalendar;
import edu.ucsd.cse110.googlefitapp.Constants;

// A class used to solely inject fake step data into Firebase
public class FakeUserData {

    String[] emails = {"arrestrose@gmail.com", "deagretal@gmail.com", "gul055@ucsd.edu", "kdolay@ucsd.edu", "rjyoung@ucsd.edu"};
    String[] tags = {Constants.TOTAL_STEPS_TAG, Constants.INTENTIONAL, Constants.GOAL};

    public void sendData() {
        for (String email: emails) {

            int goal = 5000;

            DataService service = new StepDataAdapter(email);
            AbstractCalendar calendar = new MockCalendar(2019, 3, 18);
            Map<String, Object> table = new HashMap<>();

            String[] days = calendar.getLastXDays(Constants.WITH_YEAR, 28);


            // Generate keys, and generate numbers
            for (String day : days) {

                int totalSteps = new Random().nextInt(10001) + 3000;
                int recordedSteps = new Random().nextInt(totalSteps - 1000);

                double increaseChance = Math.random();

                // 10% chance of increasing goal by 500
                if (increaseChance < .1) {
                    goal += 500;
                }

                table.put(day + Constants.TOTAL_STEPS_TAG, totalSteps);
                table.put(day + Constants.INTENTIONAL, recordedSteps);
                table.put(day + Constants.GOAL, goal);
            }

            service.addData(table);
        }
    }
}
