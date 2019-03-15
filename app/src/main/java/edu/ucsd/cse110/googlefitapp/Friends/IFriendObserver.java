package edu.ucsd.cse110.googlefitapp.Friends;

import java.util.List;

public interface IFriendObserver {
    void onStateChange(String email);
    public List<String> getFriends();
}
