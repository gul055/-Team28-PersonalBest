package edu.ucsd.cse110.googlefitapp;

public interface Dialog {

    String title = null;
    String message = null;
    String hint = null;
    String positiveButton = "Yes";
    String negativeButton = "No";

    void build();
    void show();
    int getIntResult();
}
