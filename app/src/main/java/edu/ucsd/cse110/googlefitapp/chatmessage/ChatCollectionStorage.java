package edu.ucsd.cse110.googlefitapp.chatmessage;

import com.google.firebase.firestore.CollectionReference;

public class ChatCollectionStorage {
    static CollectionReference setCollection;

    public static void setCollection(CollectionReference collection) {
        setCollection = collection;
    }

    public static CollectionReference getCollection() {
        return setCollection;
    }
}
