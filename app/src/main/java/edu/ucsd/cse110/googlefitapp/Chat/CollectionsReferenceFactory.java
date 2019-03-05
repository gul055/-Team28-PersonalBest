package edu.ucsd.cse110.googlefitapp.Chat;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CollectionsReferenceFactory {

    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";

    public CollectionReference create(){
        try{
            return FirebaseFirestore.getInstance()
                    .collection(COLLECTION_KEY)
                    .document(DOCUMENT_KEY)
                    .collection(MESSAGES_KEY);
        }
        catch(Exception e){
            Log.i("Error", e.toString());
            return null;
        }
    }
}
