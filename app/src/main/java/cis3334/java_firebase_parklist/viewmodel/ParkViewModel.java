package cis3334.java_firebase_parklist.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import cis3334.java_firebase_parklist.data.model.Park;
import cis3334.java_firebase_parklist.data.firebase.FirebaseService;

public class ParkViewModel extends ViewModel {

    public final String instanceId = UUID.randomUUID().toString();
    private final FirebaseService firebaseService;
    private ListenerRegistration parksListenerRegistration; // For holding the listener

    private final MutableLiveData<List<Park>> _parks = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Park>> getParks() {
        return _parks;
    }

    private final MutableLiveData<Park> _selectedPark = new MutableLiveData<>(null);
    public LiveData<Park> getSelectedPark() {
        return _selectedPark;
    }

    public ParkViewModel() {
        Log.d("ParkViewModel", "Instance created: " + instanceId);
        firebaseService = new FirebaseService();
        listenForParkUpdates(); // Start listening for updates
    }

    private void listenForParkUpdates() {
        Log.d("ParkViewModel", "Starting to listen for park updates from Firestore...");
        parksListenerRegistration = firebaseService.listenForParks(new FirebaseService.ParkListCallback() {
            @Override
            public void onSuccess(List<Park> parks) {
                _parks.setValue(parks); // Update LiveData whenever the list changes
                Log.d("ParkViewModel", "Park list updated: " + parks.size() + " parks.");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ParkViewModel", "Error listening for park updates.", e);
            }
        });
    }

    public void selectPark(String parkId) {
        if (parkId == null) {
            _selectedPark.setValue(null);
            return;
        }
        Park foundPark = null;
        List<Park> currentParks = _parks.getValue();
        if (currentParks != null) {
            for (Park park : currentParks) {
                if (park.getId().equals(parkId)) {
                    foundPark = park;
                    break;
                }
            }
        }
        _selectedPark.setValue(foundPark);
    }

    public void addPark(Park park) {
        Log.d("ParkViewModel", "Adding park to Firestore: " + park.getName());
        // The listener will automatically update the UI. We no longer need to manually add to the local list.
        firebaseService.addPark(park);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("ParkViewModel", "Instance cleared, detaching Firestore listener: " + instanceId);
        // Important: Detach the listener when the ViewModel is destroyed to prevent memory leaks.
        if (parksListenerRegistration != null) {
            parksListenerRegistration.remove();
        }
    }
}
