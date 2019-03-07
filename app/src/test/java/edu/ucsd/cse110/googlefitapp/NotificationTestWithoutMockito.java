package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

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
        Intent intent = TestUtils.getMainActivityIntent(TestUtils.getChatMessageService(new ArrayList<>()), TestUtils.getNotificationService("chat1"));
        MainActivity activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();
    }
}