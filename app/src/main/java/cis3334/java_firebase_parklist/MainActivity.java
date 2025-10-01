package cis3334.java_firebase_parklist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import cis3334.java_firebase_parklist.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getUser().observe(this, firebaseUser -> {
            int currentDestinationId = Objects.requireNonNull(navController.getCurrentDestination()).getId();
            if (firebaseUser != null) {
                // User is signed in, navigate to the main app screen
                if (currentDestinationId == R.id.loginFragment) {
                    navController.navigate(R.id.action_loginFragment_to_parkListFragment);
                } else if (currentDestinationId == R.id.signUpFragment) {
                    navController.navigate(R.id.action_signUpFragment_to_parkListFragment);
                }
            } else {
                // User is signed out, navigate to LoginFragment if we aren't already on an auth screen.
                if (currentDestinationId != R.id.loginFragment && currentDestinationId != R.id.signUpFragment) {
                    navController.navigate(R.id.action_parkListFragment_to_loginFragment);
                }
            }
        });
    }
}
