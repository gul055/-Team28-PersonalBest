package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.Utils.GoogleUserUtil;

public class StepDataAdapter implements DataService {

    public static StepDataAdapter singleton;
    DocumentReference ref;
    Context context;

    public StepDataAdapter(DocumentReference ref, Context c){
        context = c;
        this.ref = ref;
    }

    public static DataService getInstance(Context c){
        if(singleton != null){
            DocumentReference collection = FirebaseFirestore.getInstance()
                    .collection(Constants.STEPDATA)
                    .document(GoogleUserUtil.getEmail(c));
            singleton = new StepDataAdapter(collection, c);
        }
        return singleton;
    }

    @Override
    public Task<?> addData(Map<String, Object> data) {
        return ref.update(data);
    }
}
