package edu.ucsd.cse110.googlefitapp.chatmessage;

import com.google.firebase.firestore.CollectionReference;

interface CollectionCallback {
    void onCallback(CollectionReference collection);
}
