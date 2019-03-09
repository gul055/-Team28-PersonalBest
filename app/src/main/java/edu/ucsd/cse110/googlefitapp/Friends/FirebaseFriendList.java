package edu.ucsd.cse110.googlefitapp.Friends;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Constants;

public class FirebaseFriendList implements IFriendList {
    final String FRIEND_COLLECTION_KEY = "friends";
    CollectionReference db;
    String myEmail;

    public FirebaseFriendList(String email) {
        db = FirebaseFirestore.getInstance()
                .collection(FRIEND_COLLECTION_KEY);
        myEmail = email;
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
        Task<QuerySnapshot> userData = db.get();

        Map<String, Object> currentUser = new HashMap<String, Object>();
        Map<String, Object> otherUser = new HashMap<String, Object>();;

        //find current user's document and other user's document
        for(QueryDocumentSnapshot doc : userData.getResult()) {
            Map<String, Object> curr = doc.getData();
            String currEmail = (String) curr.get("user");

            if(currEmail == myEmail) {
                currentUser = curr;
            }
            else if(currEmail == email) {
                otherUser = curr;
            }
        }

        //if users are not found, don't do anything
        if(currentUser.size() == 0 || otherUser.size() == 0) {
            Log.d(Constants.FRIEND_TAG, "user does not exist");
            return;
        }

        //get users' lists
        List<String> otherPending = (List<String>) otherUser.get("pending");
        List<String> otherFriends = (List<String>) otherUser.get("friends");
        List<String> myPending = (List<String>) currentUser.get("pending");
        List<String> myFriends = (List<String>) currentUser.get("friends");

        //users are already friends with each other
        if(otherFriends.contains(myEmail) && myFriends.contains(email)) {
            Log.d(Constants.FRIEND_TAG, "already friends with this user")
            return;
        }
        //if other user is in current user's pending list, they become friends
        else if(myPending.contains(email)) {
            Log.d(Constants.FRIEND_TAG, "becoming friends");
            myPending.remove(email);
            myFriends.add(email);
            otherFriends.add(myEmail);
        }
        //put this user in other user's pending friends
        else {
            Log.d(Constants.FRIEND_TAG, "putting user in pending");
            otherPending.add(myEmail);
        }
    }

    @Override
    public List<String> getFriendList() {
        return null;
    }
}
