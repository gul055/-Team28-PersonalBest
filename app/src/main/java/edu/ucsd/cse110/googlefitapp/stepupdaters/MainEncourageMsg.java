package edu.ucsd.cse110.googlefitapp.stepupdaters;

import java.util.Date;

public class MainEncourageMsg implements EncourageMsg {
    private String message;
    private Date date;

    public MainEncourageMsg(Date date, String message) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}