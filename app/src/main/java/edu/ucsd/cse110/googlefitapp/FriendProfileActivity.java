package edu.ucsd.cse110.googlefitapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import edu.ucsd.cse110.googlefitapp.Calendars.AbstractCalendar;
import edu.ucsd.cse110.googlefitapp.Calendars.CalendarAdapter;
import edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData.ReceiveData;
import edu.ucsd.cse110.googlefitapp.Graph.GraphActivity;
import edu.ucsd.cse110.googlefitapp.Utils.SharedPreferencesUtil;
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

        Button viewFriend = findViewById(R.id.viewfriendactivity);
        b.setOnClickListener(v -> {
            AsyncTaskRunner runner = new AsyncTaskRunner(friend);
            runner.execute();
        });
    }

    public void launchChatActivity(String friendID){
        Intent intent = new Intent(this, ChatActivity.class);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        intent.putExtra("yourID", acct.getEmail());
        intent.putExtra("friendID", friendID);
        startActivity(intent);
    }

    /*ALL CREDIT FOR THE FOLLOWING ASYNCTASK CODE GOES TO THE TUTSPLUS TUTORIAL ON GOOGLE FIT API
    Title: Google Fit for Android: History API
    https://code.tutsplus.com/tutorials/google-fit-for-android-history-api--cms-25856
    Captured: 2/15/2019
    How the source was used: Copied code
    K.D.
    */
    //TODO: CALL THIS FROM SOMEWHERE.
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        String friend;

        public AsyncTaskRunner(String friend) {
            this.friend = friend;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("INASYNC", "In task");
            AbstractCalendar calendar = new CalendarAdapter();
            ReceiveData receiver = new ReceiveData(getApplicationContext(), friend, new SharedPreferencesUtil());
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
            startActivity(intent);
        }
    }
}
