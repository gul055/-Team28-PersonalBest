package edu.ucsd.cse110.googlefitapp.Graph;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Constants;

public class DateAxisValueFormatter implements IAxisValueFormatter {

    AbstractCalendar calendar;
    String[] days;

    public DateAxisValueFormatter(AbstractCalendar calendar) {
        this.calendar = calendar;
        days = calendar.getLast7Days(Constants.WITHOUT_YEAR);
        Log.d("DAYS OF THE WEEK", days[0]);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            return days[(int) value];
        } catch (Exception e) {
            return null;
        }
    }
}
