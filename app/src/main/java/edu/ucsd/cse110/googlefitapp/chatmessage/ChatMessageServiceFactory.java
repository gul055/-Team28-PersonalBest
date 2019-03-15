package edu.ucsd.cse110.googlefitapp.chatmessage;

import edu.ucsd.cse110.googlefitapp.Factory;

public class ChatMessageServiceFactory extends Factory<ChatMessageService> {
    private static ChatMessageServiceFactory instance;

    public static ChatMessageServiceFactory getInstance() {
        if (instance == null) {
            instance = new ChatMessageServiceFactory();
        }
        return instance;
    }

}
