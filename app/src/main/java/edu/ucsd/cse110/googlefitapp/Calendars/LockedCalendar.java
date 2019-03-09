package edu.ucsd.cse110.googlefitapp.Calendars;

import android.util.Log;

import java.util.Calendar;

public class LockedCalendar extends AbstractCalendar {

    // This is a special calendar that will always set the current day to Sunday of current week
    public LockedCalendar() {
        super();
        super.calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Log.d("FIRST DAY OF THE WEEK", super.get(Calendar.DAY_OF_MONTH) + "");
    }
}
