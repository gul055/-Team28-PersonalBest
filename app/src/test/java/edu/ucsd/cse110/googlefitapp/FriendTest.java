package edu.ucsd.cse110.googlefitapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Friends.FirebaseFriendList;
import edu.ucsd.cse110.googlefitapp.Friends.IFriendList;
import edu.ucsd.cse110.googlefitapp.Friends.IFriendObserver;
import edu.ucsd.cse110.googlefitapp.chatmessage.ChatActivity;
import edu.ucsd.cse110.googlefitapp.chatmessage.ChatMessage;

import static org.junit.Assert.assertEquals;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class FriendTest {
    String user1 = "user1";
    String user2 = "user2";
    CollectionReference db;
    Context context;
    StepCountActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(StepCountActivity.class).create().get();
        Context context = getInstrumentation().getTargetContext();

        db = FirebaseFirestore.getInstance()
                .collection(Constants.FRIEND_COLLECTION_KEY);

        //create documents for users and put them in the database
        Map<String, Object> user1Info = new HashMap<>();
        user1Info.put("user", user1);
        user1Info.put("pending", new ArrayList<>());
        user1Info.put("friends", new ArrayList<>());

        addNewUserToDb(db, user1, user1Info);

        Map<String, Object> user2Info = new HashMap<>();
        user2Info.put("user", user2);
        user2Info.put("pending", new ArrayList<>());
        user2Info.put("friends", new ArrayList<>());

        addNewUserToDb(db, user2, user2Info);

        System.out.println("Is db null? " + db.getPath());
    }

    @Test
    public void testEmpty() {
        FirebaseFriendList u1 = new FirebaseFriendList(context, user1);
        FirebaseFriendList u2 = new FirebaseFriendList(context, user2);

        FriendTester f1 = new FriendTester();
        u1.register(f1);

        FriendTester f2 = new FriendTester();
        u2.register(f2);

        u1.loadFriends();
        assertEquals(0, f1.getFriends().size());

        u2.loadFriends();
        assertEquals(0, f2.getFriends().size());
    }

    @Test
    public void testAddMutually() {
        /*FirebaseFriendList u1 = new FirebaseFriendList(context, user1);
        FirebaseFriendList u2 = new FirebaseFriendList(context, user2);

        FriendTester f1 = new FriendTester();
        u1.register(f1);

        FriendTester f2 = new FriendTester();
        u2.register(f2);

        u1.addFriend(user2);

        assertEquals(0, f1.getFriends().size());

        u2.addFriend(user1);

        assertEquals(1, f1.getFriends().size());*/
    }

    @After
    public void end() {
        removeUserFromDb(db, user1);
        removeUserFromDb(db, user2);
    }

    private void addNewUserToDb(CollectionReference db, String docId, Map<String, Object> user) {
        //i dont think this is adding the user
        db.document(docId).set(user);
    }

    private void removeUserFromDb(CollectionReference db, String user) {
        db.document(user).delete();
    }

    private class FriendTester implements IFriendObserver {
        List<String> friendList = new ArrayList<>();

        @Override
        public void onStateChange(String email) {
            System.out.println("Adding email " + email);
            friendList.add(email);
        }

        public List<String> getFriends() {
            return friendList;
        }
    }
}