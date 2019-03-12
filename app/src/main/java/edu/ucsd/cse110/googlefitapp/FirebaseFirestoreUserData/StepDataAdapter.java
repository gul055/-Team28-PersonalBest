package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Constants;

public class StepDataAdapter implements DataService {

    public static StepDataAdapter singleton;
    DocumentReference ref;

    public StepDataAdapter(DocumentReference ref) {
        this.ref = ref;
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
}
