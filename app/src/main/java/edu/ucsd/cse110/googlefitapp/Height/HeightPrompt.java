package edu.ucsd.cse110.googlefitapp.Height;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.googlefitapp.Constants;
import edu.ucsd.cse110.googlefitapp.R;
import edu.ucsd.cse110.googlefitapp.StepCountActivity;


public class HeightPrompt extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";
    public HeightLogger heightLog;
    private static final String TAG = "HeightpromptActivityFirebaseInitialization";

    FirebaseFirestore db;

    String STEPDATA_COLLECTION_KEY = "stepdata";
    String personEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_prompt);


        FirebaseApp.initializeApp(this);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personEmail = acct.getEmail();
            //Testing
            Toast.makeText(this, personEmail, Toast.LENGTH_LONG).show();
        }

        heightLog = new HeightLogger(this);
        Button okButton = findViewById(R.id.button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText EditFeet = findViewById(R.id.userFeet);
                EditText EditInches = findViewById(R.id.userInches);
                try {
                    Long feet = Long.parseLong(EditFeet.getText().toString());
                    Long inches = Long.parseLong(EditInches.getText().toString());
                    if (heightLog.writeHeight(feet, inches)) {
                        launchStepCountActivity();
                        Toast.makeText(HeightPrompt.this, "Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HeightPrompt.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ex) {
                    Toast.makeText(HeightPrompt.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //initialize user's data
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection(Constants.FRIEND_COLLECTION_KEY).document(personEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(HeightPrompt.this, "welcome back", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> users = new HashMap<>();

                        List<String> friends = new ArrayList<>();
                        List<String> pending = new ArrayList<>();
                        String user = personEmail;

                        users.put("friends", friends);
                        users.put("pending", pending);
                        users.put("user", user);

                        db.document(personEmail).set(users)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HeightPrompt.this, "initialize completed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "getting doc error ", task.getException());
                }
            }
        });
    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, StepCountActivity.class);
        intent.putExtra(StepCountActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        startActivity(intent);
    }

}
