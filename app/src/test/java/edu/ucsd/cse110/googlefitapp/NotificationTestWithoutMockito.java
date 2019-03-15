/*package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import edu.ucsd.cse110.googlefitapp.chatmessage.ChatActivity;

import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class NotificationTestWithoutMockito {

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
    }

    @Test
    public void subscribeToCorrectTopic() {
        Intent intent = TestUtils.getChatActivityIntent(TestUtils.getChatMessageService(new ArrayList<>()), TestUtils.getNotificationService("chat1"));
        ChatActivity activity = Robolectric.buildActivity(ChatActivity.class, intent).create().get();
    }
}*/