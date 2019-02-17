package edu.ucsd.cse110.googlefitapp;

public interface CalendarInterface {

    String getYear();

    String getMonth();

    String getDayOfMonth(int inc);

    String getDate(int inc);

    String[] getWeek();
}
