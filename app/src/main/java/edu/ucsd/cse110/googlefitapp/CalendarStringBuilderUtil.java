package edu.ucsd.cse110.googlefitapp;

import java.util.Calendar;

public class CalendarStringBuilderUtil {
    public static String stringBuilderMillis(long milliseconds, String endTag) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String date = year + "-" + month + "-" + day;
        String key = date + endTag;
        return key;
    }

    public static String stringBuilderCalendar(Calendar calendar, String endTag) {
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String date = year + "-" + month + "-" + day;
        String key = date + endTag;
        return key;
    }
}
