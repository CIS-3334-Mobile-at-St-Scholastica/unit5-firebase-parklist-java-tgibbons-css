package cis3334.java_firebase_parklist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.appbar.MaterialToolbar;
import cis3334.java_firebase_parklist.R; // Ensure this is correctly imported
import cis3334.java_firebase_parklist.data.model.Park;
import cis3334.java_firebase_parklist.navigation.AppDestinations;
import cis3334.java_firebase_parklist.viewmodel.ParkViewModel; // We will create this Java ViewModel later
import java.util.Locale;

public class ParkDetailFragment extends Fragment {

    private ParkViewModel parkViewModel;
    private NavController navController;
    private MaterialToolbar toolbar;
    private TextView textViewDetailName;
    private TextView textViewDetailAddress;
    private TextView textViewDetailLatitude;
    private TextView textViewDetailLongitude;
    private TextView textViewParkNotFound;

    private String parkId;

    public ParkDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parkViewModel = new ViewModelProvider(requireActivity()).get(ParkViewModel.class); // Scoped to activity

        if (getArguments() != null) {
            parkId = getArguments().getString(AppDestinations.PARK_ID_ARG);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_park_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        toolbar = view.findViewById(R.id.toolbar_detail);
        textViewDetailName = view.findViewById(R.id.textViewDetailName);
        textViewDetailAddress = view.findViewById(R.id.textViewDetailAddress);
        textViewDetailLatitude = view.findViewById(R.id.textViewDetailLatitude);
        textViewDetailLongitude = view.findViewById(R.id.textViewDetailLongitude);
        textViewParkNotFound = view.findViewById(R.id.textViewParkNotFound);

        // Setup Toolbar
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());

        if (parkId != null) {
            parkViewModel.selectPark(parkId);
        } else {
            // Handle case where parkId is null from arguments
            updateUi(null);
        }

        parkViewModel.getSelectedPark().observe(getViewLifecycleOwner(), this::updateUi);
    }

    private void updateUi(Park park) {
        if (park != null) {
            toolbar.setTitle(park.getName());
            textViewDetailName.setText(park.getName());
            textViewDetailAddress.setText(park.getAddress());
            textViewDetailLatitude.setText(String.format(Locale.getDefault(), "Latitude: %.4f", park.getLatitude()));
            textViewDetailLongitude.setText(String.format(Locale.getDefault(), "Longitude: %.4f", park.getLongitude()));

            textViewDetailName.setVisibility(View.VISIBLE);
            textViewDetailAddress.setVisibility(View.VISIBLE);
            textViewDetailLatitude.setVisibility(View.VISIBLE);
            textViewDetailLongitude.setVisibility(View.VISIBLE);
            textViewParkNotFound.setVisibility(View.GONE);
        } else {
            toolbar.setTitle("Park Details");
            textViewDetailName.setVisibility(View.GONE);
            textViewDetailAddress.setVisibility(View.GONE);
            textViewDetailLatitude.setVisibility(View.GONE);
            textViewDetailLongitude.setVisibility(View.GONE);
            textViewParkNotFound.setVisibility(View.VISIBLE);
        }
    }
}
