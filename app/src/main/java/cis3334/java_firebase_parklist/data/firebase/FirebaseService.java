package cis3334.java_firebase_parklist.data.firebase;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer; // Import the Consumer interface
import javax.annotation.Nullable;
import cis3334.java_firebase_parklist.data.model.Park;

public class FirebaseService {

    private static final String TAG = "FirebaseService";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    // --- Firestore Park Methods ---

    public void fetchParks(Consumer<List<Park>> callback) {
        db.collection("parks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Park> parksList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Park park = document.toObject(Park.class);
                            park.setId(document.getId()); // Set the document ID on the park object
                            parksList.add(park);
                        }
                        callback.accept(parksList); // Pass the completed list to the callback
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        // On failure, you could pass back an empty list
                        callback.accept(new ArrayList<>());
                    }
                });
    }

    public void addPark(Park park) {
        db.collection("parks")
                .add(park);
                // optionally add listeners for success and failure
                //.addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                //.addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    // --- Firebase Auth Methods ---

    public Task<AuthResult> signUpWithEmail(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signInWithEmail(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public void signOut() {
        auth.signOut();
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public Task<Void> sendPasswordReset(String email) {
        return auth.sendPasswordResetEmail(email);
    }
}
