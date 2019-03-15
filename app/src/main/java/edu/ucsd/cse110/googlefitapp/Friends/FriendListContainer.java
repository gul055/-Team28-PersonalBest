package edu.ucsd.cse110.googlefitapp.Friends;

import edu.ucsd.cse110.googlefitapp.Factory;

public class FriendListContainer extends Factory<FirebaseFriendList> {
    private static FriendListContainer instance;

    public static FriendListContainer getInstance() {
        if (instance == null) {
            instance = new FriendListContainer();
        }
        return instance;
    }
}
