package edu.ucsd.cse110.googlefitapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class CalendarTest {

    FitCalendar calendar;

    @Before
    public void setup(){
        calendar = new FitCalendar();
        calendar.setDate(2019, 2, 14);
    }

    @Test
    public void testGetCurrentDate(){
        assertEquals("2019-2-14", calendar.getDate(0));
    }

    @Test
    public void testGet7DaysFromNow(){
        assertEquals("2019-2-21", calendar.getDate(7));
    }

    @Test
    public void testGetWeek(){
        String[] weekString = calendar.getWeek();
        String[] expected = {"2019-2-14","2019-2-15","2019-2-16","2019-2-17","2019-2-18","2019-2-19","2019-2-20"};
        for(int i = 0; i < 7; i++){
            assertEquals(expected[i], weekString[i]);
        }
    }
}
