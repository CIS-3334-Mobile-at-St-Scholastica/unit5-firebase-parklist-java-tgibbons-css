package cis3334.java_firebase_parklist.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import cis3334.java_firebase_parklist.data.firebase.FirebaseService;

public class AuthViewModel extends ViewModel {

    private final FirebaseService firebaseService = new FirebaseService();

    private final MutableLiveData<FirebaseUser> _user = new MutableLiveData<>();
    public LiveData<FirebaseUser> getUser() {
        return _user;
    }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    public LiveData<String> getErrorMessage() {
        return _errorMessage;
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsLoading() {
        return _isLoading;
    }

    public AuthViewModel() {
        // Check for current user when the ViewModel is created
        _user.setValue(firebaseService.getCurrentUser());
    }

    public void signIn(String email, String password) {
        _isLoading.setValue(true);
        firebaseService.signInWithEmail(email, password)
            .addOnSuccessListener(authResult -> {
                _user.postValue(authResult.getUser());
                _errorMessage.postValue(null);
                _isLoading.postValue(false);
            })
            .addOnFailureListener(e -> {
                _errorMessage.postValue(e.getMessage());
                _isLoading.postValue(false);
            });
    }

    public void signUp(String email, String password) {
        _isLoading.setValue(true);
        firebaseService.signUpWithEmail(email, password)
            .addOnSuccessListener(authResult -> {
                _user.postValue(authResult.getUser());
                _errorMessage.postValue(null);
                _isLoading.postValue(false);
            })
            .addOnFailureListener(e -> {
                _errorMessage.postValue(e.getMessage());
                _isLoading.postValue(false);
            });
    }

    public void signOut() {
        firebaseService.signOut();
        _user.setValue(null);
    }

    public void clearErrorMessage() {
        _errorMessage.setValue(null);
    }
}
