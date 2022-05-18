package github.com.arnaumolins.quokkafe.Repository;
/*
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;

public class TableRepository {
    private static final String TAG = "EventRepository";
    private static TableRepository instance;
    private static MutableLiveData<ArrayList<Table>> tableLiveData;


    public static TableRepository getInstance() {
        if (instance == null) {
            instance = new TableRepository();
            tableLiveData = new MutableLiveData<>(null);
        }
        return instance;
    }


    public MutableLiveData<Table> getTableById(String tableId){
        MutableLiveData<Table> tableMutableLiveData = new MutableLiveData<>();
        Log.d("EventRepository", "getting event with id " + tableId);
        FirebaseDatabase.getInstance().getReference().child("Tables").child(tableId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Table table = task.getResult().getValue(Table.class);
                    if (table != null) {
                        tableMutableLiveData.setValue(table);
                    } else {
                        Log.d("EventRepository", "event with id " + tableId + " appears to be null!");
                        tableMutableLiveData.setValue(null);
                    }
                } else {
                    Log.d("EventRepository", "task was not successful when getting event with id " + tableId);
                    tableMutableLiveData.setValue(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tableMutableLiveData.setValue(null);
            }
        });
        return tableMutableLiveData;
    }


    public MutableLiveData<Boolean> modifyTable(MutableLiveData<Table> table) {
        MutableLiveData<Boolean> modifyEventState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Tables");
        Log.d("EventRepository", "Before: " + table.getValue().getTableId());
        dbRef.child(table.getValue().getTableId()).setValue(table.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("EventRepository", "Modified event " + table.getValue().getTableId());
                    modifyEventState.setValue(true);
                    //event.setValue(event.getValue());//notify UI completed
                } else {
                    Log.d("EventRepository", "Can't modify event!!!");
                    //event.setValue(null);//notify UI not completed
                    modifyEventState.setValue(false);
                }
            }
        });
        return modifyEventState;
    }


    public MutableLiveData<Boolean> setTable(MutableLiveData<Table> table, MutableLiveData<User> user) {
        MutableLiveData<Boolean> setTablesState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Tables");
        String tableId = dbRef.push().getKey();
        table.getValue().setTableId(tableId);
        Log.d(TAG, "Set event with id " + tableId);
        dbRef.child(tableId).setValue(table.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Crated event with id " + tableId);
                    FirebaseDatabase.
                            getInstance()
                            .getReference("Users")
                            .child(user.getValue().userId)
                            .child(tableId)
                            .setValue(tableId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "User " + user.getValue().userId + " is owner of " + tableId);
                                        setTablesState.setValue(true);
                                    }   else{
                                        Log.e(TAG, "User "+ user.getValue().userId +" is not owner of " + tableId);
                                        setTablesState.setValue(false);
                                    }
                                }
                            });
                } else {
                    Log.e(TAG, "Can't create event!!!");
                    setTablesState.setValue(false);
                }
            }
        });
        return setTablesState;
    }


    public MutableLiveData<ArrayList<Table>> getAllEvents() {
        Log.i(TAG, "getting all tables");
        if(tableLiveData.getValue() == null) {
            Log.i(TAG, "set value event listener to firebase-Table");
            FirebaseDatabase.getInstance().getReference("Table").addValueTableListener(new ValueTableListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Table> tables = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Table table = snap.getValue(Table.class);
                        if (table != null) {
                            table.setTableId(snap.getKey());
                            Log.d(TAG, "child with id " + snap.getKey());
                            Log.d(TAG, table.toString());
                            tables.add(table);
                        } else {
                            Log.e(TAG, "Table with id " + snap.getKey() + " is not valid");
                        }
                    }
                    tableLiveData.setValue(tables);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "getting all events is cancelled");
                    Log.e(TAG, error.getMessage());
                    Log.e(TAG, error.getDetails());
                }
            });
        }
        return tableLiveData;
    }

}
*/