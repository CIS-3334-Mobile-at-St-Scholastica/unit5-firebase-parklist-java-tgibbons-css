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
import cis3334.java_firebase_parklist.R; // Ensure R is imported
import cis3334.java_firebase_parklist.viewmodel.AuthViewModel; // We will create this

public class SignUpFragment extends Fragment {

    private AuthViewModel authViewModel;
    private NavController navController;

    private MaterialToolbar toolbar;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextDisplayName;
    private TextInputEditText editTextFavParkType;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfirmPassword;
    private TextInputLayout textFieldLayoutPassword;
    private TextInputLayout textFieldLayoutConfirmPassword;
    private TextView textViewPasswordError;
    private Button buttonCreateAccount;

    public SignUpFragment() {
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        toolbar = view.findViewById(R.id.toolbar_sign_up);
        editTextEmail = view.findViewById(R.id.editTextEmailSignUp);
        editTextDisplayName = view.findViewById(R.id.editTextDisplayNameSignUp);
        editTextFavParkType = view.findViewById(R.id.editTextFavParkTypeSignUp);
        editTextPassword = view.findViewById(R.id.editTextPasswordSignUp);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPasswordSignUp);
        textFieldLayoutPassword = view.findViewById(R.id.textFieldLayoutPasswordSignUp);
        textFieldLayoutConfirmPassword = view.findViewById(R.id.textFieldLayoutConfirmPasswordSignUp);
        textViewPasswordError = view.findViewById(R.id.textViewPasswordError);
        buttonCreateAccount = view.findViewById(R.id.buttonCreateAccount);

        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());

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
        editTextDisplayName.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);
        editTextConfirmPassword.addTextChangedListener(textWatcher);
        validateInput(); // Initial check

        buttonCreateAccount.setOnClickListener(v -> createAccount());

        authViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Log.d("SignUpFragment", "User signed up and logged in: " + firebaseUser.getEmail());
                // Navigate to ParkListFragment, clearing the auth flow from backstack
                navController.navigate(R.id.action_signUpFragment_to_parkListFragment);
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
        String displayName = editTextDisplayName.getText() != null ? editTextDisplayName.getText().toString().trim() : "";
        String password = editTextPassword.getText() != null ? editTextPassword.getText().toString() : ""; // No trim for password
        String confirmPassword = editTextConfirmPassword.getText() != null ? editTextConfirmPassword.getText().toString() : "";

        boolean passwordsMatch = password.equals(confirmPassword);
        if (password.length() > 0 && confirmPassword.length() > 0) { // Only show error if both have text
            if (passwordsMatch) {
                textViewPasswordError.setVisibility(View.GONE);
                textFieldLayoutPassword.setError(null);
                textFieldLayoutConfirmPassword.setError(null);
            } else {
                textViewPasswordError.setVisibility(View.VISIBLE);
                textFieldLayoutPassword.setError("Passwords do not match");
                textFieldLayoutConfirmPassword.setError("Passwords do not match");
            }
        } else {
            textViewPasswordError.setVisibility(View.GONE);
            textFieldLayoutPassword.setError(null);
            textFieldLayoutConfirmPassword.setError(null);
        }

        buttonCreateAccount.setEnabled(
                !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(displayName) &&
                !TextUtils.isEmpty(password) &&
                passwordsMatch && password.length() >= 6 // Basic Firebase password length requirement
        );
    }

    private void createAccount() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString(); // No trim
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String displayName = editTextDisplayName.getText().toString().trim();
        String favParkType = editTextFavParkType.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            textViewPasswordError.setVisibility(View.VISIBLE);
            textFieldLayoutPassword.setError("Passwords do not match");
            textFieldLayoutConfirmPassword.setError("Passwords do not match");
            Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            textFieldLayoutPassword.setError("Password must be at least 6 characters");
             Toast.makeText(getContext(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Clear errors if checks pass
        textViewPasswordError.setVisibility(View.GONE);
        textFieldLayoutPassword.setError(null);
        textFieldLayoutConfirmPassword.setError(null);


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(displayName)) {
            Toast.makeText(getContext(), "Email and Display Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        authViewModel.signUp(email, password, displayName, favParkType);
    }
}
