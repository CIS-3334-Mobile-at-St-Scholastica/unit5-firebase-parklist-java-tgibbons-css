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
    private final FirebaseService firebaseService;

    // This LiveData list will be populated from Firestore.
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
        // Remove hardcoded sample data initialization and load from Firestore.
        loadParks();
    }

    public void loadParks() {
        Log.d("ParkViewModel", "Loading parks from Firestore...");
        firebaseService.fetchParks(new FirebaseService.ParkListCallback() {
            @Override
            public void onSuccess(List<Park> parks) {
                _parks.setValue(parks); // Update LiveData with the fetched list.
                Log.d("ParkViewModel", "Parks loaded successfully: " + parks.size() + " parks.");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ParkViewModel", "Error loading parks from Firestore.", e);
                // Optionally, you could post an error state to the UI here.
            }
        });
    }

    public void selectPark(String parkId) {
        if (parkId == null) {
            _selectedPark.setValue(null);
            return;
        }
        Park foundPark = null;
        List<Park> currentParks = _parks.getValue(); // Use the LiveData's current value.
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
        Log.d("ParkViewModel", "Adding park: " + park.getName());

        // 1. Call FirebaseService to save the park to the database.
        firebaseService.addPark(park);

        // 2. For an immediate UI update, add the park to the current local list.
        //    This is a simple pattern. The new park won't have its official Firestore ID yet,
        //    but it will be corrected on the next full refresh (`loadParks`).
        List<Park> currentParks = _parks.getValue();
        if (currentParks != null) {
            ArrayList<Park> updatedParks = new ArrayList<>(currentParks);
            updatedParks.add(park);
            _parks.setValue(updatedParks);
        }

        // Optional: Instead of updating the local list, you could just reload all parks from Firestore.
        // This is simpler but can be less efficient.
        // loadParks();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("ParkViewModel", "Instance cleared: " + instanceId);
    }
}
