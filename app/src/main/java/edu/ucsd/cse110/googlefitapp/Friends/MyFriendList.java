package edu.ucsd.cse110.googlefitapp.Friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.observer.ISubject;

public class MyFriendList implements IFriendList, ISubject<IFriendObserver> {
    SharedPreferences friendPref;

    private Collection<IFriendObserver> observers;

    public MyFriendList(Context c) {
        observers = new ArrayList<>();
        friendPref = c.getSharedPreferences(Constants.FRIEND_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public void addFriend(String email) {
        Set<String> friendSet = friendPref.getStringSet("friends", new HashSet<String>());

        Log.d(Constants.FRIEND_TAG, "add friend to sharedPref: " + email);

        friendSet.add(email);
        friendPref.edit().putStringSet("friends", friendSet).apply();

        Log.d(Constants.FRIEND_TAG, "notifying observers of change");
        for (IFriendObserver observer : observers) {
            observer.onStateChange(email);
        }
    }

    @Override
    public void loadFriends() {
        Set<String> friendSet = friendPref.getStringSet("friends", new HashSet<String>());

        for (String email : friendSet) {
            for (IFriendObserver observer : observers) {
                observer.onStateChange(email);
            }
        }
    }

    public List<String> getFriendList() {
        Set<String> set = friendPref.getStringSet("friends", null);
        return (List<String>) set;
    }

    @Override
    public void register(IFriendObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(IFriendObserver observer) {
        observers.remove(observer);
    }
}
