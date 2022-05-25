package github.com.arnaumolins.quokkafe.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Booking;
import github.com.arnaumolins.quokkafe.Model.User;

public class BookingRepository {

    private static final String TAG = "BookingRepository";
    private static BookingRepository instance;
    private static MutableLiveData<ArrayList<Booking>> bookingsLiveData;

    public static BookingRepository getInstance() {
        if (instance == null) {
            instance = new BookingRepository();
            bookingsLiveData = new MutableLiveData<>(null);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Booking>> getAllBookings() {
        Log.i(TAG, "Getting all the bookings");
        if (bookingsLiveData.getValue() == null) {
            FirebaseDatabase.getInstance().getReference("Bookings").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Booking> bookings = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Booking booking = snap.getValue(Booking.class);
                        if (booking != null) {
                            booking.setBookingId(snap.getKey());
                            Log.d(TAG, "Child with id " + snap.getKey());
                            Log.d(TAG, booking.toString());
                            bookings.add(booking);
                        } else {
                            Log.e(TAG, "Booking with id " + snap.getKey() + " is not valid");
                        }
                    }
                    bookingsLiveData.setValue(bookings);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Getting all bookings is cancelled");
                    Log.e(TAG, error.getMessage());
                    Log.e(TAG, error.getDetails());
                }
            });
        }
        return bookingsLiveData;
    }

    public MutableLiveData<Boolean> createBooking(MutableLiveData<Booking> booking, MutableLiveData<User> user) {
        MutableLiveData<Boolean> setBookingState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Bookings");
        String bookingId = dbRef.push().getKey();
        booking.getValue().setBookingId(bookingId);
        dbRef.child(bookingId).setValue(booking.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Booking created with id " + bookingId);
                    FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(user.getValue().userId)
                            .child("ownedBookingIds")
                            .child(bookingId)
                            .setValue(bookingId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User " + user.getValue().userId + " is owner of " + bookingId);
                                        setBookingState.setValue(true);
                                    } else {
                                        Log.d("TAG", "User " + user.getValue().userId + " is not owner of " + bookingId);
                                        setBookingState.setValue(false);
                                    }
                                }
                            });
                } else {
                    Log.e(TAG, "Can not create the booking!");
                    setBookingState.setValue(false);
                }
            }
        });
        return setBookingState;
    }

    public MutableLiveData<Boolean> deleteBooking(String bookingId) {
        MutableLiveData<Boolean> delEvent = new MutableLiveData<>();
        FirebaseDatabase.getInstance().getReference("Bookings").child(bookingId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Log.d(TAG, error.getMessage());
                    Log.d(TAG, error.getDetails());
                }
                Log.d(TAG, "Booking deleted");
                delEvent.setValue(true);
            }
        });
        return delEvent;
    }
}
