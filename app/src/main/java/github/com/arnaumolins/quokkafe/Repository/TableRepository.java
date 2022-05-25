package github.com.arnaumolins.quokkafe.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
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
import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;

public class TableRepository {

    private static final String TAG = "TableRepository";
    private static TableRepository instance;
    private static MutableLiveData<ArrayList<Table>> tablesLiveData;
    private static MutableLiveData<ArrayList<Booking>> bookingsLiveData;

    public static TableRepository getInstance() {
        if (instance == null) {
            instance = new TableRepository();
            tablesLiveData = new MutableLiveData<>(null);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Table>> getAllTables() {
        Log.i(TAG, "Getting all the tables");
        if (tablesLiveData.getValue() == null) {
            FirebaseDatabase.getInstance().getReference("Tables").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Table> tables = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Table table = snap.getValue(Table.class);
                        if (table != null) {
                            table.setTableId(snap.getKey());
                            Log.d(TAG, "Child with id " + snap.getKey());
                            Log.d(TAG, table.toString());
                            tables.add(table);
                        } else {
                            Log.e(TAG, "Table with id " + snap.getKey() + " is not valid");
                        }
                    }
                    tablesLiveData.setValue(tables);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Getting all tables is cancelled");
                    Log.e(TAG, error.getMessage());
                    Log.e(TAG, error.getDetails());
                }
            });
        }
        return tablesLiveData;
    }

    public MutableLiveData<ArrayList<Booking>> getTableBookings(String tableId) {
        Log.i(TAG, "Getting all bookings for table with id " + tableId);
        if (bookingsLiveData.getValue() == null) {
            FirebaseDatabase.getInstance().getReference("Tables").child(tableId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Booking> tableBookings = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Booking booking = snap.getValue(Booking.class);
                        if (booking != null) {
                            booking.setBookingId(snap.getKey());
                            Log.d(TAG, "Child with id " + snap.getKey());
                            Log.d(TAG, tableBookings.toString());
                            tableBookings.add(booking);
                        } else {
                            Log.e(TAG, "Booking with id " + snap.getKey() + "from table with id " + tableId + " is not valid");
                        }
                    }
                    bookingsLiveData.setValue(tableBookings);
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

    public MutableLiveData<Boolean> addTableBooking(MutableLiveData<Booking> booking, MutableLiveData<User> user) {
        MutableLiveData<Boolean> setTableState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Tables");
        String tableBookingId = dbRef.push().getKey();
        dbRef.child("TableBookings").child(tableBookingId).setValue(booking.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                   setTableState.setValue(true);
                } else {
                    setTableState.setValue(false);
                }
            }
        });
        return setTableState;
    }
}