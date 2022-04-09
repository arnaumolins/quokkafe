package github.com.arnaumolins.quokkafe.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;


public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;

    public AuthViewModel() {
        authRepository = AuthRepository.getAuthRepository();
    }

    public MutableLiveData<User> getCurrentUser() {
        return authRepository.getCurrentUser();
    }

    public MutableLiveData<Boolean> signIn(String email, String password) {
        return authRepository.firebaseSignIn(email, password);
    }

    public MutableLiveData<User> signUp(String username, String email, String password, List<String> interestedIn) {
        return authRepository.firebaseSignUp(username, email, password, interestedIn);
    }

    public void signOut() {
        authRepository.firebaseSignOut();
    }


}
