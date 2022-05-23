package github.com.arnaumolins.quokkafe.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import github.com.arnaumolins.quokkafe.Model.User;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    static private AuthRepository authRepository;

    public static AuthRepository getAuthRepository() {
        if (authRepository == null) {
            authRepository = new AuthRepository();
        }
        return authRepository;
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void firebaseSignOut() {
        firebaseAuth.signOut();
        currentUser = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> firebaseSignIn(String email, String password) {
        MutableLiveData<Boolean> signInEnd = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();

                    FirebaseDatabase.getInstance().getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            user.userId = uid;
                            currentUser.setValue(user);
                            signInEnd.setValue(true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            currentUser.setValue(null);
                            signInEnd.setValue(false);
                        }
                    });
                } else {
                    currentUser.setValue(null);
                    signInEnd.setValue(false);
                }
            } else {
                currentUser.setValue(null);
                signInEnd.setValue(false);
            }
        });
        return signInEnd;
    }

    public MutableLiveData<User> firebaseSignUp(String username, String email, String password, Integer age, List<String> interestedIn) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(username, email, age, interestedIn);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                userMutableLiveData.setValue(user);
                            }
                        }
                    });
                }
            }
        });
        return userMutableLiveData;
    }

}
