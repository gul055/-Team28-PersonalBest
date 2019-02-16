package edu.ucsd.cse110.googlefitapp;

import android.util.Log;

import java.util.Calendar;

public class FitCalendar implements CalendarInterface {

    Calendar calendar;

    // Construct calendar and set its date to the first date of the week
    public FitCalendar() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Log.d("FIRST DAY OF THE WEEK", getDate(0));
    }

    @Override
    public String getYear() {
        return calendar.get(Calendar.YEAR) + "";
    }

    @Override
    public String getMonth() {
        return calendar.get(Calendar.MONTH) + "";
    }

    @Override
    public String getDayOfMonth(int increment) {
        return (calendar.get(Calendar.DAY_OF_MONTH) + increment) + "";
    }

    @Override
    public String getDate(int increment) {
        return getYear() + "-" + getMonth() + "-" + getDayOfMonth(increment);
    }

    public String getDateWithoutYear(int increment) {
        return getMonth() + "-" + getDayOfMonth(increment);
    }

    // Returns a list of strings for this whole week
    @Override
    public String[] getWeek() {
        String[] week = new String[Constants.WEEK_SIZE];
        for (int day = 0; day < Constants.WEEK_SIZE; day++) {
            week[day] = getDate(day);
        }
        return week;
    }

    public String[] getWeekWithoutYear() {
        String[] week = new String[Constants.WEEK_SIZE];
        for (int day = 0; day < Constants.WEEK_SIZE; day++) {
            week[day] = getDateWithoutYear(day);
        }
        return week;
    }

    public void setDate(int year, int month, int day) {
        calendar.set(year, month, day);
    }
}
