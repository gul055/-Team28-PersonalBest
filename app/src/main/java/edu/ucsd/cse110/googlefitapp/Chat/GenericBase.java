package edu.ucsd.cse110.googlefitapp.Chat;

public interface GenericBase {

    void sendMessage(String msg);
    String getName();
    String getTopic();
    void subscribeToNotificationsTopic();
}
