package edu.ucsd.cse110.googlefitapp;

public class FakeCalendar implements CalendarInterface {

    public FakeCalendar(){}

    @Override
    public String getYear() {
        return "1912";
    }

    @Override
    public String getMonth() {
        return "6";
    }

    @Override
    public String getDayOfMonth(int inc) {
        return "23";
    }

    // Alan Turing's birthday
    @Override
    public String getDate(int inc) {
        return "1912-6-23";
    }

    @Override
    public String[] getWeek() {
        return new String[0];
    }
}
