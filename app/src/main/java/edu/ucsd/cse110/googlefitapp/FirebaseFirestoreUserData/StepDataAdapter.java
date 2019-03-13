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

    public StepDataAdapter(DocumentReference ref) {
        this.ref = ref;
        if (snapshot == null) {
            getDocumentSnapshot();
        }
    }

    public static DataService getInstance(String email) {
        if (singleton != null) {
            DocumentReference collection = FirebaseFirestore.getInstance()
                    .collection(Constants.STEPDATA)
                    .document(email);
            singleton = new StepDataAdapter(collection);
        }
        return singleton;
    }

    @Override
    public Task<?> addData(Map<String, Object> data) {
        return ref.update(data);
    }

    @Override
    public Long getLong(String tag) {
        Long result = snapshot.getLong(tag);
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
