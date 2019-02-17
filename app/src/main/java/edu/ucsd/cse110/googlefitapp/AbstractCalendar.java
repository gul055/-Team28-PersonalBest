package edu.ucsd.cse110.googlefitapp;

import java.util.Calendar;

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

    // Gets the current dat + next 6 days of the week in string
    public String[] getWeek(boolean withYear) {
        String[] week = new String[Constants.WEEK_SIZE];

        for (int day = 0; day < Constants.WEEK_SIZE; day++) {
            if (withYear) {
                week[day] = getYearMonthDay();
            } else {
                week[day] = getMonthDay();
            }

            // Increment date
            calendar.add(Calendar.DATE, 1);
        }

        // Revert date upon ending
        calendar.add(Calendar.DATE, -Constants.WEEK_SIZE + 1);

        return week;
    }
}
