package edu.ucsd.cse110.googlefitapp.Calendars;

import android.util.Log;

import java.util.Calendar;

import edu.ucsd.cse110.googlefitapp.Constants;

public abstract class AbstractCalendar {

    protected Calendar calendar;

    public AbstractCalendar() {
        calendar = Calendar.getInstance();
    }

    public int get(int field) {
        if (field == Calendar.MONTH) {
            return calendar.get(Calendar.MONTH) + 1;
        }
        return calendar.get(field);
    }

    public String getMonthDay() {
        return get(Calendar.MONTH) + "-" + get(Calendar.DAY_OF_MONTH);
    }

    public String getYearMonthDay() {
        return get(Calendar.YEAR) + "-" + getMonthDay();
    }

    public void setTimeInMillis(int ms) {
        calendar.setTimeInMillis(ms);
    }

    // Gets the current day + next 6 days of the week in string
    public String[] getWeek(boolean withYear) {
        String[] week = new String[Constants.WEEK_SIZE];

        for (int day = 0; day < Constants.WEEK_SIZE; day++) {
            if (withYear) {
                week[day] = getYearMonthDay();
                Log.d("Generating date:", week[day]);
            } else {
                week[day] = getMonthDay();
                Log.d("Generating date:", week[day]);
            }

            // Increment date
            calendar.add(Calendar.DATE, 1);
        }

        // Revert date upon ending
        calendar.add(Calendar.DATE, -Constants.WEEK_SIZE);

        return week;
    }

    // Gets the current day and previous 6 days
    public String[] getLast7Days(boolean withYear) {

        calendar.add(Calendar.DATE, -Constants.WEEK_SIZE);

        String[] week = new String[Constants.WEEK_SIZE];

        for (int day = 0; day < Constants.WEEK_SIZE; day++) {
            if (withYear) {
                week[day] = getYearMonthDay();
                Log.d("Generating date:", week[day]);
            } else {
                week[day] = getMonthDay();
                Log.d("Generating date:", week[day]);
            }

            // Increment date
            calendar.add(Calendar.DATE, 1);
        }

        return week;
    }

    public String[] getLastXDays(boolean withYear, int x) {

        calendar.add(Calendar.DATE, -x);

        String[] days = new String[x];

        for (int day = 0; day < x; day++) {
            if (withYear) {
                days[day] = getYearMonthDay();
                Log.d("Generating date:", days[day]);
            } else {
                days[day] = getMonthDay();
                Log.d("Generating date:", days[day]);
            }

            // Increment date
            calendar.add(Calendar.DATE, 1);
        }

        return days;
    }
}
