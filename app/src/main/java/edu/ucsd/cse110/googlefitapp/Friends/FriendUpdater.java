package edu.ucsd.cse110.googlefitapp.Friends;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import edu.ucsd.cse110.googlefitapp.Constants;

public class FriendUpdater implements IFriendObserver {
    LinearLayout friendContainer;
    Context context;
    View.OnClickListener clickOnFriend;

    public FriendUpdater(Context c, LinearLayout layout) {
        context = c;
        friendContainer = layout;

        clickOnFriend = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked on button: " + view.getTag(), Toast.LENGTH_SHORT).show();
                //ultimately we want to go to friend's monthly activity page
            }
        };
    }

    @Override
    public void onStateChange(String email) {
        Log.d(Constants.FRIEND_UPDATER_TAG, "Creating a button for " + email);

        // Create the Friend button element
        Button friend = new Button(context);
        friend.setTag(email);
        friend.setText(email);
        friend.setOnClickListener(clickOnFriend);

        Log.d(Constants.FRIEND_UPDATER_TAG, "Adding " + email + "'s button");

        // Put button in the LinearLayout
        friendContainer.addView(friend);
    }
}
