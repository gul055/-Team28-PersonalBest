package edu.ucsd.cse110.googlefitapp.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class FirebaseAdapter implements GenericBase {

    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "chat2";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    // mocked collectionreference
    CollectionReference chat;
    SharedPreferences pref;
    Context context;

    String subscribedTopic = null;

    public FirebaseAdapter(Context c, CollectionReference ref) {
        chat = ref;
        context = c;
        pref = c.getSharedPreferences("FirebaseLabApp", Context.MODE_PRIVATE);
    }

    @Override
    public void sendMessage(String msg) {
        String from = getName();
        if (from == null || from.isEmpty()) {
            Log.e("SendError", "No message sent");
            return;
        }

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, from);
        newMessage.put(TEXT_KEY, msg);

        if (chat != null) {
            chat.add(newMessage).addOnSuccessListener(result -> {
                Log.e("Send", "Message sent");
            }).addOnFailureListener(error -> {
                Log.e("NoSend", error.getLocalizedMessage());
            });
        }
    }

    @Override
    public void subscribeToNotificationsTopic() {

        subscribedTopic = DOCUMENT_KEY;

        if (chat != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(DOCUMENT_KEY)
                    .addOnCompleteListener(task -> {
                                String msg = "Subscribed to notifications";
                                if (!task.isSuccessful()) {
                                    msg = "Subscribe to notifications failed";
                                }
                                Log.d("tag", msg);
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }
                    );
        } else {
        }
    }

    @Override
    public String getName() {
        return pref.getString(FROM_KEY, null);
    }

    @Override
    public String getTopic() {
        return subscribedTopic;
    }

    ;
}
