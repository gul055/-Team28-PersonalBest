package edu.ucsd.cse110.googlefitapp.Friends;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.chatmessage.Callback;

public class EncouragementFriendList extends FirebaseFriendList {

    public EncouragementFriendList(Context context, String email) {
        super(context, email);
    }

    public void loadFriends(Callback callback){
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> currentUser = new HashMap<String, Object>();

                //find current user's document
                for(QueryDocumentSnapshot doc : task.getResult()) {
                    Map<String, Object> curr = doc.getData();
                    String currEmail = (String) curr.get("user");

                    if(currEmail.equals(myEmail)) {
                        currentUser = curr;
                    }
                }

                //get current user's friend list
                List<String> myFriends = (List<String>) currentUser.get("friends");

                //notify observers of each friend
                if(myFriends != null) {
                    for (String email : myFriends) {
                        Log.d(Constants.FRIEND_TAG, email);
                        friendList.add(email);
                        for (IFriendObserver observer : observers) {
                            observer.onStateChange(email);
                        }
                    }
                }

                callback.onCallback();
            }
        });
    }
}
