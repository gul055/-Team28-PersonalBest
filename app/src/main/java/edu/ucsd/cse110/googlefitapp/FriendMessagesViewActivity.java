package edu.ucsd.cse110.googlefitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import edu.ucsd.cse110.googlefitapp.Friends.FirebaseFriendList;
import edu.ucsd.cse110.googlefitapp.Friends.FriendMessageUpdater;
import edu.ucsd.cse110.googlefitapp.Friends.IFriendObserver;
import edu.ucsd.cse110.googlefitapp.chatmessage.ChatActivity;

public class FriendMessagesViewActivity extends AppCompatActivity {
    ScrollView friendView;
    LinearLayout friendContainer;
    String myEmail;

    FirebaseFriendList myFriends;
    IFriendObserver friendUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_message_view);

        // set up elements
        friendView = this.findViewById(R.id.friendView2);
        friendContainer = friendView.findViewById(R.id.friendContainer2);

        //get user email
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            myEmail = acct.getEmail();
        }

        // friend list subject and listener
        myFriends = new FirebaseFriendList(this.getApplicationContext(), myEmail);
        friendUpdater = new FriendMessageUpdater(this.getApplication(), FriendMessagesViewActivity.this, friendContainer);
        myFriends.register(friendUpdater);

        // Load friends
        myFriends.loadFriends();

        // add friend's email to friend list
    }

    public void launchChatActivity(String friendID) {
        Intent intent = new Intent(this, ChatActivity.class);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        intent.putExtra("yourID", acct.getEmail());
        intent.putExtra("friendID", friendID);
        startActivity(intent);
    }

}
