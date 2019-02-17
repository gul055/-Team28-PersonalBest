package edu.ucsd.cse110.googlefitapp;

public class MockCalendar extends AbstractCalendar {

    public MockCalendar(int setYear, int setMonth, int setDay) {
        super();
        super.calendar.set(setYear, setMonth - 1, setDay);
    }
}
