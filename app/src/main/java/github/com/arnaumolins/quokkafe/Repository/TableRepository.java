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

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;

public class TableRepository {
    private static final String TAG = "TableRepository";
    private static TableRepository instance;
    private static MutableLiveData<ArrayList<Table>> tablesLiveData;

    public static TableRepository getInstance() {
        if (instance == null) {
            instance = new TableRepository();
            tablesLiveData = new MutableLiveData<>(null);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Table>> getAllTables() {
        if (tablesLiveData.getValue() == null) {
            FirebaseDatabase.getInstance().getReference("Tables").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Table> tables = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Table table = snap.getValue(Table.class);
                        if (table != null) {
                            table.setTableId(snap.getKey());
                            tables.add(table);
                        } else {

                        }
                    }
                    tablesLiveData.setValue(tables);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return tablesLiveData;
    }

    public MutableLiveData<Boolean> setTable(MutableLiveData<Table> table, MutableLiveData<User> user) {
        MutableLiveData<Boolean> setEventState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Tables");
        String tableId = dbRef.push().getKey();
        table.getValue().setTableId(tableId);
        Log.d(TAG, "Set event with id " + tableId);
        dbRef.child(tableId).setValue(table.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Event created with id " + tableId);
                    FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(user.getValue().userId)
                            .child("ownedEventIds")
                            .child(tableId)
                            .setValue(tableId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User " + user.getValue().userId + " is owner of " + tableId);
                                        setEventState.setValue(true);
                                    } else {
                                        Log.d("TAG", "User " + user.getValue().userId + " is not owner of " + tableId);
                                        setEventState.setValue(false);
                                    }
                                }
                            });
                } else {
                    Log.e(TAG, "Can not create the event!");
                    setEventState.setValue(false);
                }
            }
        });
        return setEventState;
    }
}