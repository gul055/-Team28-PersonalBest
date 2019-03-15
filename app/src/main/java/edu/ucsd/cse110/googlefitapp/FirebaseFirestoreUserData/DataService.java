package edu.ucsd.cse110.googlefitapp.FirebaseFirestoreUserData;

import com.google.android.gms.tasks.Task;

import java.util.Map;

public interface DataService {

    Task<?> addData(Map<String, Object> data);

    Long getLong(String tag);

    void getDocumentSnapshot();
}
