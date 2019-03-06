package edu.ucsd.cse110.googlefitapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FriendViewActivity extends AppCompatActivity {
    View friendView;
    Button friend;
    Button addFriendBtn;
    EditText emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);

        friendView = this.findViewById(R.id.friendView);
        addFriendBtn = this.findViewById(R.id.addFriendButton);
        emailText = this.findViewById(R.id.emailEditText);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                Log.d(Constants.FRIEND, "add friend email: " + email);
                //do stuff with email address
            }
        });
    }
}
