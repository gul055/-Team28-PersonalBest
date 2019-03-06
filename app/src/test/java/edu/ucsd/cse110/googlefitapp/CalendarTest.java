package edu.ucsd.cse110.googlefitapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Calendars.MockCalendar;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class CalendarTest {

    AbstractCalendar calendar;

    @Before
    public void setup(){
        calendar = new MockCalendar(2019,2,14);
    }

    @Test
    public void testGetCurrentDate(){
        assertEquals("2019-2-19", calendar.getYearMonthDay());
    }

    @Test
    public void testGetWeek(){
        String[] weekString = calendar.getWeek(true);
        String[] expected = {"2019-2-14","2019-2-15","2019-2-16","2019-2-17","2019-2-18","2019-2-19","2019-2-20"};
        for(int i = 0; i < 7; i++){
            assertEquals(expected[i], weekString[i]);
        }
    }
}
