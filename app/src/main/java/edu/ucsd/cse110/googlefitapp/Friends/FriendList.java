package edu.ucsd.cse110.googlefitapp.Friends;

import java.util.ArrayList;
import java.util.List;

public class FriendList implements IFriendObserver {
    List<String> friends = new ArrayList<>();
    @Override
    public void onStateChange(String email) {
        friends.add(email);
    }

    public List<String> getFriends() {
        return friends;
    }

    public int getSize() {
        return friends.size();
    }
}
