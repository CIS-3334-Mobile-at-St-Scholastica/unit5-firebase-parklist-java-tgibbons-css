package cis3334.java_firebase_parklist.data.firebase;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cis3334.java_firebase_parklist.data.model.Park; // Ensure this import is correct

public class FirebaseService {

    private static final String TAG = "FirebaseService";
    private FirebaseFirestore db;



    public FirebaseService() {
        db = FirebaseFirestore.getInstance();
    }

    public void addPark(Park park) {
         Map<String, Object> parkMap = new HashMap<>();
        // We don't put park.getId() here because Firestore generates the document ID.
        // If park.getId() is already set (e.g., from a previous fetch or client-side generation),
        // it will be part of the Park object, but the document ID in Firestore is separate.
        parkMap.put("name", park.getName());
        parkMap.put("address", park.getAddress());
        parkMap.put("latitude", park.getLatitude());
        parkMap.put("longitude", park.getLongitude());

        db.collection("parks")
                .add(parkMap) // '.add()' generates a new document ID
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    public void fetchParks(ParkListCallback callback) {
        db.collection("parks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Park> parksList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Park park = document.toObject(Park.class);
                            park.setId(document.getId()); // Set the Firestore document ID to the Park object
                            parksList.add(park);
                        }
                        callback.onSuccess(parksList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Callback interface for fetchParks
    public interface ParkListCallback {
        void onSuccess(List<Park> parks);
        void onFailure(Exception e);
    }
}
