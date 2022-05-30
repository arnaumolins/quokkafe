package github.com.arnaumolins.quokkafe.Utils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.R;

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.ViewHolder> {

    public static final String TAG = "TableListAdapter";
    private ArrayList<Table> tables;
    private final Context context;
    private TableItemAction action;

    public TableListAdapter(Context context, TableItemAction action) {
        this.context = context;
        this.action = action;
    }

    public void setTables(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.table_info, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Table table = tables.get(position);
        viewHolder.itemView.setTag(table);

        String tableNumberS = "Table number: " + table.getTableNumber();
        viewHolder.number.setText(tableNumberS);

        String tableCostumerS = "Number of costumers: " + table.getNumberOfCustomers();
        viewHolder.customers.setText(tableCostumerS);

        FirebaseStorage.getInstance().getReference("images/" + table.getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null) {
                    Glide.with(context)
                            .load(uri)
                            .centerCrop()
                            .into(viewHolder.image);
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        });

        viewHolder.itemView.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(this.action.navigate(table.getTableId()));
        });
    }

    public int getItemCount() {
        return tables.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, customers;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivTableImage);
            number = itemView.findViewById(R.id.tvTableNumber);
            customers = itemView.findViewById(R.id.tvTableCustomers);
        }
    }
}