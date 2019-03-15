package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Friends.FirebaseFriendList;
import edu.ucsd.cse110.googlefitapp.Friends.FriendListContainer;
import edu.ucsd.cse110.googlefitapp.Friends.IFriendList;
import edu.ucsd.cse110.googlefitapp.Friends.IFriendObserver;
import edu.ucsd.cse110.googlefitapp.chatmessage.ChatActivity;
import edu.ucsd.cse110.googlefitapp.chatmessage.ChatMessage;

import static org.junit.Assert.assertEquals;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class FriendTest {
    CollectionReference db;
    FriendViewActivity activity;
    EditText emailInput;
    Button addButton;
    FirebaseFriendList mockFriendList;


    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        mockFriendList = Mockito.mock(FirebaseFriendList.class);
        String testKey = "friend test key";
        FriendListContainer.getInstance().put(testKey, () -> mockFriendList);
        intent.putExtra(FriendViewActivity.FRIENDLIST_EXTRA, testKey);

        activity = Robolectric.buildActivity(FriendViewActivity.class, intent).create().get();
        Context context = getInstrumentation().getTargetContext();

        db = FirebaseFirestore.getInstance()
                .collection(Constants.FRIEND_COLLECTION_KEY);

        emailInput = activity.findViewById(R.id.emailEditText);
        addButton = activity.findViewById(R.id.addFriendButton);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAdd() {

        String email = "user@example.com";
        emailInput.setText(email);
        addButton.performClick();

        Mockito.verify(mockFriendList, Mockito.atLeastOnce()).addFriend(email);
    }
}