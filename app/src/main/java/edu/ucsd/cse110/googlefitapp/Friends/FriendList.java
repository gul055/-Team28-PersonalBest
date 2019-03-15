package edu.ucsd.cse110.googlefitapp.Friends;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FriendList implements IFriendObserver {
    List<String> friends = new ArrayList<>();
    int numFriends = 0;
    @Override
    public void onStateChange(String email) {
        if(!friends.contains(email)) {
            Log.d("FRIENDLIST", email);
            friends.add(email);
            numFriends++;
        }
    }

    public List<String> getFriends() {
        return friends;
    }

    public int getSize() {
        Log.d("SUMFRIENDS", String.valueOf(numFriends));
        return numFriends;
    }
}
