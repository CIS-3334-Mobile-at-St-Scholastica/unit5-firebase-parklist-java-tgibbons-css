package cis3334.java_firebase_parklist.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import cis3334.java_firebase_parklist.data.model.Park; // Ensure this import is correct

public class ParkViewModel extends ViewModel {

    public final String instanceId = UUID.randomUUID().toString();

    // Internal mutable list for sample data
    private final ArrayList<Park> _sampleParksList = new ArrayList<>();

    private final MutableLiveData<List<Park>> _parks = new MutableLiveData<>();
    public LiveData<List<Park>> getParks() {
        return _parks;
    }

    private final MutableLiveData<Park> _selectedPark = new MutableLiveData<>(null);
    public LiveData<Park> getSelectedPark() {
        return _selectedPark;
    }

    public ParkViewModel() { // Constructor instead of init block
        Log.d("ParkViewModel", "Instance created: " + instanceId);
        // Initialize sample data
        _sampleParksList.add(new Park("park1", "Jay Cooke State Park", "780 MN-210, Carlton, MN 55718", 46.6577, -92.2175));
        _sampleParksList.add(new Park("park2", "Gooseberry Falls State Park", "3206 MN-61, Two Harbors, MN 55616", 47.1436, -91.4647));
        _sampleParksList.add(new Park("park3", "Tettegouche State Park", "5702 MN-61, Silver Bay, MN 55614", 47.3090, -91.2720));
        loadParks();
    }

    public void loadParks() {
        // Simulate loading parks (in future, this would be from a FirebaseService equivalent)
        // Post a new ArrayList to LiveData to ensure observers see it as a new list
        _parks.setValue(new ArrayList<>(_sampleParksList));
    }

    public void selectPark(String parkId) {
        if (parkId == null) {
            _selectedPark.setValue(null);
            return;
        }
        Park foundPark = null;
        for (Park park : _sampleParksList) {
            if (park.getId().equals(parkId)) {
                foundPark = park;
                break;
            }
        }
        _selectedPark.setValue(foundPark);
    }

    public void addPark(Park park) {
        Park newParkWithId;
        if (park.getId() == null || park.getId().isEmpty()) {
            // Generate a simple unique ID for sample data if not provided
            String generatedId = "park" + (_sampleParksList.size() + 1) + (int)(Math.random() * 1000);
            newParkWithId = new Park(generatedId, park.getName(), park.getAddress(), park.getLatitude(), park.getLongitude());
        } else {
            newParkWithId = park;
        }
        Log.d("ParkViewModel", "Adding park: " + newParkWithId.getName() + ", Instance: " + instanceId);
        _sampleParksList.add(newParkWithId);
        _parks.setValue(new ArrayList<>(_sampleParksList)); // Emit the updated list
        Log.d("ParkViewModel", "Parks list size after add: " + (_parks.getValue() != null ? _parks.getValue().size() : 0));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("ParkViewModel", "Instance cleared: " + instanceId);
    }
}
