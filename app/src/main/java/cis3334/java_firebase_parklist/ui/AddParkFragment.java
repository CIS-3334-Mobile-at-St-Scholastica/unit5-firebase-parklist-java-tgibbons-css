package cis3334.java_firebase_parklist.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout; // Not strictly needed for logic, but good for completeness
import cis3334.java_firebase_parklist.R;
import cis3334.java_firebase_parklist.data.model.Park;
import cis3334.java_firebase_parklist.viewmodel.ParkViewModel;

public class AddParkFragment extends Fragment {

    private ParkViewModel parkViewModel;
    private NavController navController;

    private MaterialToolbar toolbar;
    private TextInputEditText editTextName;
    private TextInputEditText editTextAddress;
    private TextInputEditText editTextLatitude;
    private TextInputEditText editTextLongitude;
    private Button buttonSavePark;

    public AddParkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Scope ViewModel to the Activity, similar to the Kotlin version
        parkViewModel = new ViewModelProvider(requireActivity()).get(ParkViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_park, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        toolbar = view.findViewById(R.id.toolbar_add_park);
        editTextName = view.findViewById(R.id.editTextName);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextLatitude = view.findViewById(R.id.editTextLatitude);
        editTextLongitude = view.findViewById(R.id.editTextLongitude);
        buttonSavePark = view.findViewById(R.id.buttonSavePark);

        // Setup Toolbar
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());

        // Basic input validation to enable/disable save button
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInput();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        editTextName.addTextChangedListener(textWatcher);
        editTextAddress.addTextChangedListener(textWatcher);
        // Initially disable button until valid input
        validateInput();


        buttonSavePark.setOnClickListener(v -> savePark());
    }

    private void validateInput() {
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        buttonSavePark.setEnabled(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address));
    }

    private void savePark() {
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String latitudeStr = editTextLatitude.getText().toString().trim();
        String longitudeStr = editTextLongitude.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
            // This should ideally be handled by disabling the button,
            // but as a fallback, show a Toast or update TextInputLayout error.
            Log.w("AddParkFragment", "Name or address is empty.");
            if (TextUtils.isEmpty(name)) {
                ((TextInputLayout)editTextName.getParent().getParent()).setError("Name cannot be empty");
            }
            if (TextUtils.isEmpty(address)) {
                ((TextInputLayout)editTextAddress.getParent().getParent()).setError("Address cannot be empty");
            }
            return;
        }
         // Clear previous errors
        ((TextInputLayout)editTextName.getParent().getParent()).setError(null);
        ((TextInputLayout)editTextAddress.getParent().getParent()).setError(null);


        double latitude = 0.0;
        double longitude = 0.0;

        try {
            if (!TextUtils.isEmpty(latitudeStr)) {
                latitude = Double.parseDouble(latitudeStr);
            }
        } catch (NumberFormatException e) {
            Log.w("AddParkFragment", "Invalid latitude format: " + latitudeStr);
            ((TextInputLayout)editTextLatitude.getParent().getParent()).setError("Invalid latitude");
            return; // Or handle more gracefully
        }
        ((TextInputLayout)editTextLatitude.getParent().getParent()).setError(null);


        try {
            if (!TextUtils.isEmpty(longitudeStr)) {
                longitude = Double.parseDouble(longitudeStr);
            }
        } catch (NumberFormatException e) {
            Log.w("AddParkFragment", "Invalid longitude format: " + longitudeStr);
            ((TextInputLayout)editTextLongitude.getParent().getParent()).setError("Invalid longitude");
            return; // Or handle more gracefully
        }
         ((TextInputLayout)editTextLongitude.getParent().getParent()).setError(null);


        // ID will be generated by Firestore or a local mechanism in ViewModel
        Park newPark = new Park("", name, address, latitude, longitude);
        parkViewModel.addPark(newPark); // Assuming addPark handles ID generation or Firestore does

        Log.d("AddParkFragment", "Park saved: " + newPark.getName());
        navController.popBackStack(); // Navigate back
    }
}
