package github.com.arnaumolins.quokkafe.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    public void firebaseSignOut() {
        firebaseAuth.signOut();
        currentUser = new MutableLiveData<>();
    }

    public MutableLiveData<List<String>> getCurrentUserAttendingEvents() {
        User user = currentUser.getValue();
        MutableLiveData<List<String>> attendingEventsLiveData = new MutableLiveData<>();
        if(currentUser != null) {
            FirebaseDatabase.getInstance().getReference("Users").child(user.userId).child("attendingEventsIds").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        ArrayList<String> eventIds = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                            eventIds.add(ds.getValue(String.class));
                        }
                        attendingEventsLiveData.setValue(eventIds);
                    } else {
                        attendingEventsLiveData.setValue(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    attendingEventsLiveData.setValue(null);
                }
            });
        }
        return attendingEventsLiveData;
    }

    public MutableLiveData<List<String>> getCurrentUserBookings() {
        User user = currentUser.getValue();
        MutableLiveData<List<String>> bookingsLiveData = new MutableLiveData<>();
        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference("Users").child(user.userId).child("bookingsIds").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        ArrayList<String> bookingsIds = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            bookingsIds.add(ds.getValue(String.class));
                        }
                        bookingsLiveData.setValue(bookingsIds);
                    } else {
                        bookingsLiveData.setValue(null);
                    }
                }
            }). addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bookingsLiveData.setValue(null);
                }
            });
        }
        return bookingsLiveData;
    }

    public MutableLiveData<Boolean> deleteOrderOwnership(String orderId, String ownerId) {
        MutableLiveData<Boolean> delOwnership = new MutableLiveData<>();
        FirebaseDatabase.getInstance().getReference("Users").child(ownerId).child("ownedOrdersIds").child(orderId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null){
                    Log.d(TAG, error.getMessage());
                    Log.d(TAG, error.getDetails());
                }
                Log.d(TAG, "delOwnership to true");
                delOwnership.setValue(true);
            }
        });
        return delOwnership;
    }
}