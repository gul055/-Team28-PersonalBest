package edu.ucsd.cse110.googlefitapp.Friends;

import java.util.List;

public interface IFriendList {
    void addFriend(String email);
    List<String> getFriendList();
}
