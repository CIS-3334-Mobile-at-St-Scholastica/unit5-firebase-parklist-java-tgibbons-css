package cis3334.java_firebase_parklist.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cis3334.java_firebase_parklist.data.model.User; // Ensure this import is correct

public class AuthViewModel extends ViewModel {

    private final MutableLiveData<User> _user = new MutableLiveData<>(null);
    public LiveData<User> getUser() {
        return _user;
    }

    // For error messages, as used in LoginFragment and SignUpFragment
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() {
        return _errorMessage;
    }

    public void clearErrorMessage() {
        _errorMessage.setValue(null);
    }

    // Potential future additions:
    // private final MutableLiveData<Boolean> _authInProgress = new MutableLiveData<>(false);
    // public LiveData<Boolean> getAuthInProgress() { return _authInProgress; }

    /**
     * Simulates signing in a user.
     * Later, this will call Firebase Authentication.
     */
    public void signIn(String email, String password) {
        // TODO: Implement actual Firebase sign-in
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            _user.setValue(new User(
                "dummyUid-" + email.hashCode(),
                email,
                "Dummy " + (email.contains("@") ? email.substring(0, email.indexOf('@')) : email),
                "Any"
            ));
            _errorMessage.setValue(null); // Clear any previous error
        } else {
            // _user.setValue(null); // Ensure user is null if sign-in fails
            _errorMessage.setValue("Email and password cannot be blank.");
        }
    }

    /**
     * Simulates signing up a new user.
     * Later, this will call Firebase Authentication.
     */
    public void signUp(String email, String password, String displayName, String favoriteParkType) {
        // TODO: Implement actual Firebase sign-up
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            _user.setValue(new User(
                "dummyUid-" + email.hashCode() + "-new",
                email,
                displayName,
                favoriteParkType
            ));
            _errorMessage.setValue(null);
        } else {
            // _user.setValue(null);
            _errorMessage.setValue("Email and password cannot be blank for sign up.");
        }
    }

    /**
     * Signs out the current user.
     */
    public void signOut() {
        // TODO: Implement actual Firebase sign-out
        _user.setValue(null);
        // _errorMessage.setValue(null); // Optionally clear errors on sign out
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Clean up resources if any
    }
}
