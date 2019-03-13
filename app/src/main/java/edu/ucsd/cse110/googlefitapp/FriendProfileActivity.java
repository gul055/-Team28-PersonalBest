package edu.ucsd.cse110.googlefitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import edu.ucsd.cse110.googlefitapp.chatmessage.ChatActivity;

public class FriendProfileActivity extends AppCompatActivity {

    TextView friendProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        friendProfile = findViewById(R.id.profileText);
        String friend = getIntent().getStringExtra("friend");
        friendProfile.setText(friend + "'s profile");

        Button b = findViewById(R.id.friendButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchChatActivity(friend);
            }
        });
    }

    public void launchChatActivity(String friendID){
        Intent intent = new Intent(this, ChatActivity.class);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        intent.putExtra("yourID", acct.getEmail());
        intent.putExtra("friendID", friendID);
        startActivity(intent);
    }
}
