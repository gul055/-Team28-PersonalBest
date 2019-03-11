package edu.ucsd.cse110.googlefitapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ucsd.cse110.googlefitapp.Friends.FirebaseFriendList;
import edu.ucsd.cse110.googlefitapp.Friends.FriendUpdater;

public class FriendViewActivity extends AppCompatActivity {
    ScrollView friendView;
    Button addFriendBtn;
    EditText emailText;
    LinearLayout friendContainer;
    String myEmail;

    FirebaseFriendList myFriends;
    FriendUpdater friendUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseFirestore.getInstance();

        // set up elements
        friendView = this.findViewById(R.id.friendView);
        friendContainer = friendView.findViewById(R.id.friendContainer);
        emailText = this.findViewById(R.id.emailEditText);
        addFriendBtn = this.findViewById(R.id.addFriendButton);

        //get user email
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            myEmail = acct.getEmail();
        }

        // friend list subject and listener
        myFriends = new FirebaseFriendList(this.getApplicationContext(), myEmail);
        friendUpdater = new FriendUpdater(this.getApplicationContext(), friendContainer);
        myFriends.register(friendUpdater);

        // Load friends
        myFriends.loadFriends();

        // add friend's email to friend list
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                Log.d(Constants.FRIEND_TAG, "add friend email: " + email);
                myFriends.addFriend(email);
                emailText.setText("");
                emailText.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
    }

}
