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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import cis3334.java_firebase_parklist.R;
import cis3334.java_firebase_parklist.viewmodel.AuthViewModel;

public class SignUpFragment extends Fragment {

    private AuthViewModel authViewModel;
    private NavController navController;

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfirmPassword;
    private Button buttonCreateAccount;

    // Other UI elements that will be disabled during sign-up
    private TextInputEditText editTextDisplayName;
    private TextInputEditText editTextFavParkType;
    private TextView textViewPasswordError;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        initializeViews(view);
        setupToolbar();
        setupInputValidation();
        setupClickListeners();
        observeViewModel();
    }

    private void initializeViews(@NonNull View view) {
        editTextEmail = view.findViewById(R.id.editTextEmailSignUp);
        editTextPassword = view.findViewById(R.id.editTextPasswordSignUp);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPasswordSignUp);
        buttonCreateAccount = view.findViewById(R.id.buttonCreateAccount);
        // Other views
        editTextDisplayName = view.findViewById(R.id.editTextDisplayNameSignUp);
        editTextFavParkType = view.findViewById(R.id.editTextFavParkTypeSignUp);
        textViewPasswordError = view.findViewById(R.id.textViewPasswordError);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = requireView().findViewById(R.id.toolbar_sign_up);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());
    }

    private void setupInputValidation() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { validateInput(); }
            @Override public void afterTextChanged(Editable s) { }
        };
        editTextEmail.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);
        editTextConfirmPassword.addTextChangedListener(textWatcher);
        editTextDisplayName.addTextChangedListener(textWatcher);
        validateInput(); // Initial check
    }

    private void setupClickListeners() {
        buttonCreateAccount.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString();
            // Call the ViewModel to sign up. The logic for saving user profile data will be in the ViewModel.
            authViewModel.signUp(email, password);
        });
    }

    private void observeViewModel() {
        authViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                // Navigation is handled by MainActivity, so we just log it here.
                Log.d("SignUpFragment", "Sign-up successful. Navigation will be handled by MainActivity.");
            }
        });

    }

    private void validateInput() {
        String email = editTextEmail.getText() != null ? editTextEmail.getText().toString().trim() : "";
        String displayName = editTextDisplayName.getText() != null ? editTextDisplayName.getText().toString().trim() : "";
        String password = editTextPassword.getText() != null ? editTextPassword.getText().toString() : "";
        String confirmPassword = editTextConfirmPassword.getText() != null ? editTextConfirmPassword.getText().toString() : "";

        boolean passwordsMatch = password.equals(confirmPassword);
        if (password.length() > 0 && confirmPassword.length() > 0) {
            if (passwordsMatch) {
                textViewPasswordError.setVisibility(View.GONE);
            } else {
                textViewPasswordError.setVisibility(View.VISIBLE);
            }
        } else {
            textViewPasswordError.setVisibility(View.GONE);
        }

        boolean isPasswordValid = password.length() >= 6;

        buttonCreateAccount.setEnabled(
                !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(displayName) &&
                passwordsMatch &&
                isPasswordValid
        );
    }

    private void setUiEnabled(boolean enabled) {
        editTextEmail.setEnabled(enabled);
        editTextPassword.setEnabled(enabled);
        editTextConfirmPassword.setEnabled(enabled);
        editTextDisplayName.setEnabled(enabled);
        editTextFavParkType.setEnabled(enabled);
        buttonCreateAccount.setEnabled(enabled);
        // After disabling, re-run validation if re-enabling to set button state correctly
        if (enabled) {
            validateInput();
        }
    }
}
