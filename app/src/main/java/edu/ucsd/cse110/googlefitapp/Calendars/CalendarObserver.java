package edu.ucsd.cse110.googlefitapp.Calendars;

import java.util.Calendar;

/**
 *  Interface for classes that are used to observe a Calendar object
 */
public interface CalendarObserver {
    // Updates the observer of the calendar
    void update(Calendar calendar);
}
