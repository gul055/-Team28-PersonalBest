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
        //Check if A is in B's pending list
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
            Object currEmail = curr.get("user");

            if(currEmail == myEmail) {
                currentUser = curr;
            }
            else if(currEmail == email) {
                otherUser = curr;
            }
        }

        if(currentUser.size() == 0 || otherUser.size() == 0) {
            Log.d(Constants.FRIEND_TAG, "user does not exist");
        }
    }

    @Override
    public List<String> getFriendList() {
        return null;
    }
}
