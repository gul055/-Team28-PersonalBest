package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Constants;

public class StepDataAdapter implements DataService {

    public static StepDataAdapter singleton;
    DocumentReference ref;
    DocumentSnapshot snapshot;
    String email;

    public StepDataAdapter(DocumentReference ref) {
        Log.d("StepDataAdapter", "Creating adapter");
        this.ref = ref;
        if (snapshot == null) {
            getDocumentSnapshot();
        }
    }

    public StepDataAdapter(String email) {
        Log.d("StepDataAdapter", "Creating adapter");
        DocumentReference collection = FirebaseFirestore.getInstance()
                .collection(Constants.STEPDATA)
                .document(email);
        if (collection == null) {
            FirebaseFirestore.getInstance().collection(Constants.STEPDATA).document(email).set(null);
            collection = FirebaseFirestore.getInstance().collection(Constants.STEPDATA).document(email);
        }
        this.ref = collection;
        if (snapshot == null) {
            getDocumentSnapshot();
        }
        this.email = email;
    }

    public static DataService getInstance(String email) {
        Log.d("StepDataAdapter", "Creating instance of adapter for email " + email);
        if (singleton == null) {
            DocumentReference collection = FirebaseFirestore.getInstance()
                    .collection(Constants.STEPDATA)
                    .document(email);
            if (collection == null) {
                FirebaseFirestore.getInstance().collection(Constants.STEPDATA).document(email).set(null);
                collection = FirebaseFirestore.getInstance().collection(Constants.STEPDATA).document(email);
            }
            singleton = new StepDataAdapter(collection);
            singleton.email = email;
        }
        return singleton;
    }

    @Override
    public Task<?> addData(Map<String, Object> data) {
        Log.d("addData", "Adding a table to email " + email);
        return ref.update(data);
    }

    @Override
    public Long getLong(String tag) {
        Long result = (Long) snapshot.get(tag);
        return result != null ? result : 0L;
    }

    @Override
    // Helper function that'll retrieve our document. Purely 3rd party functionality.
    public void getDocumentSnapshot() {
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                snapshot = task.getResult();
                Log.i("GetDocument", "Document found");
            } else {
                Log.i("GetDocument", "Document not found");
            }
        });
    }
}
