package cis3334.java_firebase_parklist.data.firebase;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cis3334.java_firebase_parklist.data.model.Park;

public class FirebaseService {

    private static final String TAG = "FirebaseService";
    private FirebaseFirestore db;

    // Callback interface remains the same.
    public interface ParkListCallback {
        void onSuccess(List<Park> parks);
        void onFailure(Exception e);
    }

    public FirebaseService() {
        db = FirebaseFirestore.getInstance();
    }

    public void addPark(Park park) {
        if (park == null) {
            Log.w(TAG, "Park object is null, cannot add to Firestore.");
            return;
        }

        Map<String, Object> parkMap = new HashMap<>();
        parkMap.put("name", park.getName());
        parkMap.put("address", park.getAddress());
        parkMap.put("latitude", park.getLatitude());
        parkMap.put("longitude", park.getLongitude());

        db.collection("parks")
                .add(parkMap)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    // This method now sets up a real-time listener and returns the registration.
    public ListenerRegistration listenForParks(ParkListCallback callback) {
        return db.collection("parks")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        callback.onFailure(e);
                        return;
                    }

                    List<Park> parksList = new ArrayList<>();
                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Park park = doc.toObject(Park.class);
                            park.setId(doc.getId());
                            parksList.add(park);
                        }
                    }
                    callback.onSuccess(parksList); // The callback is triggered every time data changes.
                });
    }
}
