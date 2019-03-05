package edu.ucsd.cse110.googlefitapp.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import edu.ucsd.cse110.googlefitapp.R;

public class ChatActivity extends AppCompatActivity {
    String TAG = ChatActivity.class.getSimpleName();

    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    CollectionReference chat;
    String from;
    GenericBase base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SharedPreferences sharedpreferences = getSharedPreferences("FirebaseLabApp", Context.MODE_PRIVATE);

        FirebaseApp.initializeApp(getApplicationContext());

        from = sharedpreferences.getString(FROM_KEY, null);

        chat = new CollectionsReferenceFactory().create();

        base = new FirebaseAdapter(this, chat);

        base.subscribeToNotificationsTopic();

        initMessageUpdateListener();

        findViewById(R.id.btn_send).setOnClickListener(view -> {
            EditText messageView = findViewById(R.id.text_message);
            base.sendMessage(messageView.getText().toString());
            messageView.setText("");
        });

        EditText nameView = findViewById((R.id.user_name));
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

    private void initMessageUpdateListener() {
        if (chat != null) {
            chat.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING)
                    .addSnapshotListener((newChatSnapShot, error) -> {
                        if (error != null) {
                            Log.e("tag", error.getLocalizedMessage());
                            return;
                        }

                        if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                            documentChanges.forEach(change -> {
                                QueryDocumentSnapshot document = change.getDocument();
                                sb.append(document.get(FROM_KEY));
                                sb.append(":\n");
                                sb.append(document.get(TEXT_KEY));
                                sb.append("\n");
                                sb.append("---\n");
                            });


                            TextView chatView = findViewById(R.id.chat);
                            chatView.append(sb.toString());
                        }
                    });
        } else {
        }
    }

    public GenericBase getBase() {
        return base;
    }


}
