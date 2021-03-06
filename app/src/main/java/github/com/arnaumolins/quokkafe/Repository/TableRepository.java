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
            bookingsLiveData = new MutableLiveData<>(null);
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

    public MutableLiveData<Boolean> addTableBooking(MutableLiveData<Booking> booking, MutableLiveData<User> user) {
        MutableLiveData<Boolean> setTableState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Tables");
        dbRef.child(booking.getValue().getTableId())
                .child("TableBookings")
                .child(booking.getValue().getBookingId())
                .setValue(booking.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
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