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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

import cis3334.java_firebase_parklist.R;
import cis3334.java_firebase_parklist.viewmodel.AuthViewModel;

public class LoginFragment extends Fragment {

    private AuthViewModel authViewModel;
    private NavController navController;

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoToSignUp;
    private ProgressBar progressBar;

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
        progressBar = view.findViewById(R.id.progressBarLogin);

        setupInputValidation();
        setupClickListeners();
        observeViewModel();
    }

    private void setupInputValidation() {
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
    }

    private void setupClickListeners() {
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            authViewModel.signIn(email, password);
        });

        buttonGoToSignUp.setOnClickListener(v -> {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment);
        });
    }

    private void observeViewModel() {
        authViewModel.getUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                // This navigation is now handled by MainActivity's observer
                 Log.d("LoginFragment", "User is logged in. Navigation will be handled by MainActivity.");
            }
        });

        authViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                authViewModel.clearErrorMessage();
            }
        });

        authViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                buttonLogin.setEnabled(false);
                buttonGoToSignUp.setEnabled(false);
                editTextEmail.setEnabled(false);
                editTextPassword.setEnabled(false);
            } else {
                progressBar.setVisibility(View.GONE);
                validateInput(); // Re-validate to set button state correctly
                buttonGoToSignUp.setEnabled(true);
                editTextEmail.setEnabled(true);
                editTextPassword.setEnabled(true);
            }
        });
    }

    private void validateInput() {
        String email = editTextEmail.getText() != null ? editTextEmail.getText().toString().trim() : "";
        String password = editTextPassword.getText() != null ? editTextPassword.getText().toString().trim() : "";
        buttonLogin.setEnabled(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password));
    }
}
