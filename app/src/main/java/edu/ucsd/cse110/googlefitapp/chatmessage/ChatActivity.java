package edu.ucsd.cse110.googlefitapp.chatmessage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.R;
import edu.ucsd.cse110.googlefitapp.notification.FirebaseCloudMessagingAdapter;
import edu.ucsd.cse110.googlefitapp.notification.NotificationService;
import edu.ucsd.cse110.googlefitapp.notification.NotificationServiceFactory;

import static edu.ucsd.cse110.googlefitapp.Constants.FROM_KEY;
import static edu.ucsd.cse110.googlefitapp.Constants.TEXT_KEY;
import static edu.ucsd.cse110.googlefitapp.Constants.TIMESTAMP_KEY;

public class ChatActivity extends AppCompatActivity {
    /*These are used for something else, don't put in Constants file!*/
    public static final String SHARED_PREFERENCES_NAME = "FirebaseLabApp";
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";
    public static final String NOTIFICATION_SERVICE_EXTRA = "NOTIFICATION_SERVICE";

    String TAG = ChatActivity.class.getSimpleName();

    String DOCUMENT_KEY = "chat1";

    ChatMessageService chat;
    String from;

    //TODO: Get given email to work with text
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sharedpreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        Intent intent = getIntent();

        from = intent.getExtras().getString("yourID");

        String yourID = from.replace(".", ",");
        String toID = intent.getExtras().getString("friendID").replace(".", ",");
        String stringExtra = getIntent().getStringExtra(CHAT_MESSAGE_SERVICE_EXTRA);

        Log.d("YOURID COMMA", yourID);
        Log.d("THEIRID COMMA", toID);
        //ALL FUNCTIONS THAT USE DATABASE MUST BE CALLED IN ONCALLBACK
        //This is because grabbing data is asynchronous!

        //Check if a chat was already put into the intent.
        chat = ChatMessageServiceFactory.getInstance().get(stringExtra);
        if (chat == null) {
            FirebaseFirestoreAdapter.checkInstance(new Callback() {
                                                       @Override
                                                       public void onCallback() {
                                                           grabData(yourID, toID, stringExtra);
                                                       }
                                                   },
                    yourID, toID);
        } else {
            firebaseFunctionsUpdater();
        }

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());

        TextView nameView = findViewById(R.id.user_name);
        nameView.setText(from);
        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                from = s.toString();
                sharedpreferences.edit().putString(FROM_KEY, from).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void firebaseFunctionsUpdater() {
        initMessageUpdateListener();
        subscribeToNotificationsTopic();
    }

    private void grabData(String yourID, String toID, String stringExtra) {
        FirebaseFirestoreAdapter
                .setInstance(new CollectionCallback() {
                                 @Override
                                 public void onCallback(CollectionReference collection) {
                                     if (collection != null) {
                                         FirebaseFirestoreAdapter.setSingeleton(new FirebaseFirestoreAdapter(collection));
                                         chat = ChatMessageServiceFactory.getInstance().getOrDefault(stringExtra, FirebaseFirestoreAdapter::getInstance);
                                     } else {
                                         chat = ChatMessageServiceFactory.getInstance().getOrDefault(stringExtra, FirebaseFirestoreAdapter::getInstance);
                                     }
                                     firebaseFunctionsUpdater();
                                 }
                             },
                        yourID,
                        toID);
    }

    private void sendMessage() {
        /*if (from == null || from.isEmpty()) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }*/

        EditText messageView = findViewById(R.id.text_message);

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, from);
        newMessage.put(TIMESTAMP_KEY, String.valueOf(new Date().getTime()));
        newMessage.put(TEXT_KEY, messageView.getText().toString());

        chat.addMessage(newMessage).addOnSuccessListener(result -> {
            messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    @Override
    protected void onDestroy() {
        //So singeleton may be reused.
        super.onDestroy();
        FirebaseFirestoreAdapter.setSingeleton(null);
    }

    private void initMessageUpdateListener() {
        TextView chatView = findViewById(R.id.chat);
        chat.addOrderedMessagesListener(
                chatMessagesList -> {
                    Log.d(TAG, "msg list size:" + chatMessagesList.size());
                    chatMessagesList.forEach(chatMessage -> {
                        chatView.append(chatMessage.toString());
                    });
                });
    }

    private void subscribeToNotificationsTopic() {
        NotificationServiceFactory notificationServiceFactory = NotificationServiceFactory.getInstance();
        String notificationServiceKey = getIntent().getStringExtra(NOTIFICATION_SERVICE_EXTRA);
        NotificationService notificationService = notificationServiceFactory.getOrDefault(notificationServiceKey, FirebaseCloudMessagingAdapter::getInstance);

        notificationService.subscribeToNotificationsTopic(DOCUMENT_KEY, task -> {
            String msg = "Subscribed to notifications";
            if (!task.isSuccessful()) {
                msg = "Subscribe to notifications failed";
            }
            Log.d(TAG, msg);
            Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
        });
    }

}
