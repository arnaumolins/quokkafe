package github.com.arnaumolins.quokkafe.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Table;

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
}