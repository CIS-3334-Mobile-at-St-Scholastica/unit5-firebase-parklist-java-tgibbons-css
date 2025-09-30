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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;
import cis3334.java_firebase_parklist.R; // Ensure R is imported
import cis3334.java_firebase_parklist.viewmodel.AuthViewModel; // We will create this

public class LoginFragment extends Fragment {

    private AuthViewModel authViewModel;
    private NavController navController;

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoToSignUp;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        editTextEmail = view.findViewById(R.id.editTextEmailLogin);
        editTextPassword = view.findViewById(R.id.editTextPasswordLogin);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonGoToSignUp = view.findViewById(R.id.buttonGoToSignUp);

        // TextWatcher for enabling/disabling login button
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
        editTextEmail.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);
        validateInput(); // Initial check

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                authViewModel.signIn(email, password);
            } else {
                Toast.makeText(getContext(), "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        buttonGoToSignUp.setOnClickListener(v -> {
            // Navigate to SignUpFragment using the action defined in nav_graph.xml
            navController.navigate(R.id.action_loginFragment_to_signUpFragment);
        });

        // Observe user authentication state
        authViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Log.d("LoginFragment", "User logged in: " + firebaseUser.getEmail());
                // Navigate to ParkListFragment (or main app screen)
                // Clear back stack up to loginFragment
                navController.navigate(R.id.action_loginFragment_to_parkListFragment);
            } else {
                Log.d("LoginFragment", "User is null, not navigating from login.");
                // User is null (logged out or login failed), stay on login or handle error
            }
        });

        authViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                authViewModel.clearErrorMessage(); // Clear message after showing
            }
        });
    }

    private void validateInput() {
        String email = editTextEmail.getText() != null ? editTextEmail.getText().toString().trim() : "";
        String password = editTextPassword.getText() != null ? editTextPassword.getText().toString().trim() : "";
        buttonLogin.setEnabled(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password));
    }
}
