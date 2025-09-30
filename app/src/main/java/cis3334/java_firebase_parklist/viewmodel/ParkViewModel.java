package cis3334.java_firebase_parklist.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cis3334.java_firebase_parklist.data.model.Park;
import cis3334.java_firebase_parklist.data.firebase.FirebaseService;

public class ParkViewModel extends ViewModel {

    public final String instanceId = UUID.randomUUID().toString();

    private final FirebaseService firebaseService = new FirebaseService();
    // Initialize with an empty list to prevent null issues.
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
        loadParks();
    }

    public void loadParks() {
        firebaseService.fetchParks(parksList -> {
            _parks.setValue(parksList); // Update LiveData with the result from Firebase
            // If the Firebase list is empty, add three sample parks to seed the database.
            if (parksList.isEmpty()) {
                addPark(new Park(null, "Jay Cooke State Park", "780 MN-210, Carlton, MN 55718", 46.6577, -92.2175));
                addPark(new Park(null, "Gooseberry Falls State Park", "3206 MN-61, Two Harbors, MN 55616", 47.1436, -91.4647));
                addPark(new Park(null, "Tettegouche State Park", "5702 MN-61, Silver Bay, MN 55614", 47.3090, -91.2720));
            }
        });
    }

    public void selectPark(String parkId) {
        if (parkId == null) {
            _selectedPark.setValue(null);
            return;
        }
        Park foundPark = null;
        List<Park> currentParks = _parks.getValue(); // Get the list from LiveData
        for (Park park : currentParks) { // Loop over the list
            if (park.getId() != null && park.getId().equals(parkId)) {
                foundPark = park;
                break;
            }
        }
        _selectedPark.setValue(foundPark);
    }

    public void addPark(Park park) {
        Log.d("ParkViewModel", "Adding park: " + park.getName());

        // 1. Add park to Firebase.
        firebaseService.addPark(park);

        // 2. Update local LiveData for an immediate UI refresh.
        List<Park> currentParks = _parks.getValue();
        currentParks.add(park);
        _parks.setValue(currentParks); // Emit the updated list.
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("ParkViewModel", "Instance cleared: " + instanceId);
    }
}
