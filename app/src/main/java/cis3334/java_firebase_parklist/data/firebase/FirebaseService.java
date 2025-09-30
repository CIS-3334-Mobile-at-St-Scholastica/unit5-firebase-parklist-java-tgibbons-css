package cis3334.java_firebase_parklist.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cis3334.java_firebase_parklist.data.model.Park;

public class FirebaseService {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void fetchParks(ParkListCallback callback) {
        db.collection("parks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("CIS3334", document.getId() + " => " + document.getData());
                                Park park = document.toObject(Park.class);
                                itemViewModel.addItem(park);
                                Log.d("CIS3334", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("CIS3334", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    // Callback interface for fetchParks
    public interface ParkListCallback {
        void onSuccess(List<Park> parks);
        void onFailure(Exception e);
    }

}