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

    public AuthViewModel() {
        // Check for current user when the ViewModel is created
        _user.setValue(firebaseService.getCurrentUser());
    }

    public void signIn(String email, String password) {
        firebaseService.signInWithEmail(email, password)
            .addOnSuccessListener(authResult -> {
                _user.postValue(authResult.getUser());
           })
            .addOnFailureListener(e -> {
            });
    }

    public void signUp(String email, String password) {
        firebaseService.signUpWithEmail(email, password)
            .addOnSuccessListener(authResult -> {
                _user.postValue(authResult.getUser());
            })
            .addOnFailureListener(e -> {
            });
    }

    public void signOut() {
        firebaseService.signOut();
        _user.setValue(null);
    }


}
