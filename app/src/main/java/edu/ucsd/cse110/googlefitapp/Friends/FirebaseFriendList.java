package edu.ucsd.cse110.googlefitapp.Friends;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.observer.ISubject;

public class FirebaseFriendList implements IFriendList, ISubject<IFriendObserver> {
    CollectionReference db;
    String myEmail;
    private Collection<IFriendObserver> observers;
    Context context;

    public FirebaseFriendList(Context context, String email) {
        db = FirebaseFirestore.getInstance()
                .collection(Constants.FRIEND_COLLECTION_KEY);

        myEmail = email;

        observers = new ArrayList<>();

        this.context = context;
    }

    @Override
    public void addFriend(String email) {
        //Let A be the current user
        //Let B be the user A is adding
        //find B's document
        //find A's document
        //Check if B is in A's pending list
        //if so, move A into B's friend list
        //put B in A's friend list
        //if not, check if B is already in A's friend list -> do nothing
        //otherwise, add A to B's pending list
        //return message
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //for resulting toast message
                String result = "";

                //user as hashmap object
                Map<String, Object> currentUser = new HashMap<>();
                Map<String, Object> otherUser = new HashMap<>();

                //find current user and other user's document
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Map<String, Object> curr = doc.getData();
                    String currEmail = (String) curr.get("user");

                    if (currEmail.equals(myEmail)) {
                        currentUser = curr;
                    } else if (currEmail.equals(email)) {
                        otherUser = curr;
                    }
                }

                //if users are not found, don't do anything
                if (otherUser.size() == 0) {
                    Log.d(Constants.FRIEND_TAG, "user does not exist");
                    result = "Could not find user " + email;
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    return;
                }

                //get users' friend and pending lists
                List<String> otherPending = (List<String>) otherUser.get("pending");
                List<String> otherFriends = (List<String>) otherUser.get("friends");
                List<String> myPending = (List<String>) currentUser.get("pending");
                List<String> myFriends = (List<String>) currentUser.get("friends");

                //users are already friends with each other
                if (otherFriends.contains(myEmail) && myFriends.contains(email)) {
                    Log.d(Constants.FRIEND_TAG, "already friends with this user");
                    result = "You are already friends with " + email;
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    return;
                }
                //if other user is in current user's pending list, they become friends
                else if (myPending.contains(email)) {
                    Log.d(Constants.FRIEND_TAG, "becoming friends");
                    myPending.remove(email);
                    myFriends.add(email);
                    otherFriends.add(myEmail);

                    result = "Added " + email;
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();

                    //notify listeners
                    for (IFriendObserver observer : observers) {
                        observer.onStateChange(email);
                    }
                }
                //put this user in other user's pending friends
                else {
                    Log.d(Constants.FRIEND_TAG, "putting user in pending");
                    if (!otherPending.contains(myEmail)) {
                        result = "You will be friends with " + email + " when they add you as a friend.";
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                        otherPending.add(myEmail);
                    } else {
                        result = "Already added! You will be friends with " + email + " when they add you as a friend.";
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show();

                    }
                }

                //make the changes in the database
                updateDatabase(myEmail, myFriends, myPending);
                updateDatabase(email, otherFriends, otherPending);
            }
        });

        Log.d(Constants.FRIEND_TAG, "end add friend process");
    }

    private void updateDatabase(String email, List<String> friends, List<String> pending) {
        db.whereEqualTo("user", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(Constants.FRIEND_TAG, "updating user " + email);
                        Map<String, Object> map = new HashMap<>();
                        map.put("user", email);
                        map.put("friends", friends);
                        map.put("pending", pending);
                        db.document(document.getId()).set(map, SetOptions.merge());
                    }
                }
            }
        });
    }

    @Override
    public void loadFriends() {
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> currentUser = new HashMap<String, Object>();

                //find current user's document
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Map<String, Object> curr = doc.getData();
                    String currEmail = (String) curr.get("user");

                    if (currEmail.equals(myEmail)) {
                        currentUser = curr;
                    }
                }

                //get current user's friend list
                List<String> myFriends = (List<String>) currentUser.get("friends");

                //notify observers of each friend
                for (String email : myFriends) {
                    for (IFriendObserver observer : observers) {
                        observer.onStateChange(email);
                    }
                }
            }
        });
    }

    @Override
    public void register(IFriendObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(IFriendObserver observer) {

    }
}
