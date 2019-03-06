package edu.ucsd.cse110.googlefitapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FriendViewActivity extends AppCompatActivity {
    ScrollView friendView;
    Button friend;
    Button addFriendBtn;
    EditText emailText;
    View.OnClickListener clickOnFriend;
    LinearLayout friendContainer;
    FriendList myFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);

        friendView = this.findViewById(R.id.friendView);
        friendContainer = friendView.findViewById(R.id.friendContainer);
        emailText = this.findViewById(R.id.emailEditText);
        addFriendBtn = this.findViewById(R.id.addFriendButton);

        myFriends = new FriendList();

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                Log.d(Constants.FRIEND, "add friend email: " + email);
                myFriends.addFriend(email);

                emailText.setText("");
                emailText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                //do stuff with email address
                showFriends(myFriends.getFriendList());
            }
        });

        clickOnFriend = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked on button: " + view.getTag(), Toast.LENGTH_SHORT).show();
                //ultimately we want to go to friend's monthly activity page
            }
        };
    }

    public void showFriends(List<String> friendList) {
        Log.d(Constants.FRIEND, "Show friends");

        //iterate through my friend list
        for(String email:friendList) {

            Log.d(Constants.FRIEND, "Creating a button for " + email);

            // Create the Friend button element
            friend = new Button(this);
            friend.setTag(email);
            friend.setText(email);
            friend.setOnClickListener(clickOnFriend);

            Log.d(Constants.FRIEND, "Adding " + email + "'s button");

            // Put button in the LinearLayout
            friendContainer.addView(friend);
        }
    }

    private class FriendList {
        List<String> friendList;

        public FriendList() {
            friendList = new ArrayList<String>();
        }

        public void addFriend(String email) {
            Log.d(Constants.FRIEND, "add email to list: " + email);
            friendList.add(email);
        }

        public List<String> getFriendList() {
            Log.d(Constants.FRIEND, "returning friend list");
            return friendList;
        }
    }
}
