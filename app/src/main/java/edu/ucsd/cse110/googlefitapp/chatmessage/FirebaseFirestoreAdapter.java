package edu.ucsd.cse110.googlefitapp.chatmessage;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static edu.ucsd.cse110.googlefitapp.chatmessage.ChatCollectionStorage.getCollection;
import static edu.ucsd.cse110.googlefitapp.chatmessage.ChatCollectionStorage.setCollection;

public class FirebaseFirestoreAdapter implements ChatMessageService {
    private static FirebaseFirestoreAdapter singeleton;

    private static final String TAG = FirebaseFirestoreAdapter.class.getSimpleName();

    private static final String COLLECTION_KEY = "chats";
    private static final String DOCUMENT_KEY = "chat4";
    private static final String MESSAGES_KEY = "messages";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String FROM_KEY = "from";
    private static final String TEXT_KEY = "text";

    private CollectionReference chat;

    public FirebaseFirestoreAdapter(CollectionReference chat) {
        this.chat = chat;
    }

    public static void setSingeleton(FirebaseFirestoreAdapter fb){
        if(fb == null)
            Log.d("SINGELETON NULL", "SET TO NULL");
        singeleton = fb;
    }

    public static ChatMessageService getInstance(){
        return singeleton;
    }

    //TODO: Change so it uses the correct keys :)
    public static void setInstance(CollectionCallback callback, String yourID, String friendID) {
        if (singeleton == null) {
            //TODO: Grab user ID and friendID on button click.
            String friendPair = "friendPair";
            //String yourID = "goodbye";
            //String friendID = "hello";
            Log.d("BEFOREQUER", "query");
            FirebaseFirestore fb = FirebaseFirestore.getInstance();

            Task<QuerySnapshot> task = fb.collection(COLLECTION_KEY)
                    .whereEqualTo(friendPair + "." + yourID, true)
                    .whereEqualTo(friendPair + "." + friendID, true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot snapshot = task.getResult();
                            List<DocumentSnapshot> list = snapshot.getDocuments();
                            DocumentSnapshot docSnap = list.get(0);
                            DocumentReference docRef = docSnap.getReference();
                            CollectionReference collRef = docRef.collection(MESSAGES_KEY);
                            callback.onCallback(collRef);
                        }
                    });
        }
        else {
            Log.d("NOTNULL", "Singeleton was not null!");
            callback.onCallback(null);
        }
        /*return singeleton;*/
    }

    @Override
    public Task<?> addMessage(Map<String, String> message) {
        return chat.add(message);
    }

    @Override
    public void addOrderedMessagesListener(Consumer<List<ChatMessage>> listener) {
        chat.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        if (!newChatSnapShot.getMetadata().hasPendingWrites()) {
                            List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();

                            List<ChatMessage> newMessages = documentChanges.stream()
                                    .map(DocumentChange::getDocument)
                                    .map(doc -> new ChatMessage(doc.getString(FROM_KEY), doc.getString(TEXT_KEY)))
                                    .collect(Collectors.toList());

                            listener.accept(newMessages);
                        }
                    }
                });
    }
}

