package edu.ucsd.cse110.googlefitapp.Friends;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.FriendMessagesViewActivity;
import edu.ucsd.cse110.googlefitapp.chatmessage.ChatActivity;

public class FriendMessageUpdater implements IFriendObserver {
    LinearLayout friendContainer;
    FriendMessagesViewActivity activity;
    View.OnClickListener clickOnFriend;

    public FriendMessageUpdater(FriendMessagesViewActivity activity, LinearLayout layout){
        friendContainer = layout;
        clickOnFriend = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                activity.launchChatActivity(b.getText(), );
            }
        };
    }
    @Override
    public void onStateChange(String email) {
        Log.d(Constants.FRIEND_TAG, "Creating a button for " + email);

        // Create the Friend button element
        Button friend = new Button(activity);
        friend.setTag(email);
        friend.setText(email);
        friend.setOnClickListener(clickOnFriend);

        Log.d(Constants.FRIEND_TAG, "Adding " + email + "'s button");

        // Put button in the LinearLayout
        friendContainer.addView(friend);
    }
}
