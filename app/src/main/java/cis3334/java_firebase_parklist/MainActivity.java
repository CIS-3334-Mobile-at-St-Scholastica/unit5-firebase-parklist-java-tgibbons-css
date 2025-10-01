package cis3334.java_firebase_parklist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import cis3334.java_firebase_parklist.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // The set of top-level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.parkListFragment, R.id.loginFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                // User is signed in, navigate to ParkListFragment if we are on the login screen
                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.loginFragment) {
                    navController.navigate(R.id.action_loginFragment_to_parkListFragment);
                }
            } else {
                // User is signed out, navigate to LoginFragment
                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != R.id.loginFragment) {
                    navController.navigate(R.id.action_parkListFragment_to_loginFragment);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            authViewModel.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
