package edu.ucsd.cse110.googlefitapp.Friends;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

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

        Map<String, Object> currentUser;
        Map<String, Object> otherUser;

        for(QueryDocumentSnapshot doc : userData.getResult()) {
            Map<String, Object> curr = doc.getData();
        }
    }

    @Override
    public List<String> getFriendList() {
        return null;
    }
}
