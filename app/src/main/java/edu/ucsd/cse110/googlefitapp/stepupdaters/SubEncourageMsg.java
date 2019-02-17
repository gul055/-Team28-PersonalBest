package edu.ucsd.cse110.googlefitapp.stepupdaters;

import java.util.Date;

public class SubEncourageMsg implements EncourageMsg{
    private String message;
    private String message2;
    private long stepsImproved;
    private Date date;

    public SubEncourageMsg(Date date, String message, long stepsImproved, String message2) {
        this.message = message;
        this.message2 = message2;
        this.stepsImproved = stepsImproved;
        this.date = date;
    }

    public String getMessage() {
        return message + Long.toString(stepsImproved) + message2;
    }

    public Date getDate() {
        return date;
    }
}
