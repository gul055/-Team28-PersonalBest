package edu.ucsd.cse110.googlefitapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FriendProfileActivity extends AppCompatActivity {

    TextView friendProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        friendProfile = findViewById(R.id.profileText);
        String friend = getIntent().getStringExtra("friend");
        friendProfile.setText(friend + "'s profile");
    }
}
