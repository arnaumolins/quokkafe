package github.com.arnaumolins.quokkafe.Utils;
/*
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.R;

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.ViewHolder>{

    private static final String TAG = "TableListAdapter";
    private ArrayList<Table> tables;
    private final Context context;
    private TableItemAction action;

    public TableListAdapter(Context context, TableItemAction action) {
//        this.events = events;
        this.context = context;
        this.action = action;
    }

    public void setEvents(ArrayList<Table> tables) {
        this.tables = tables;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Table table = tables.get(position);
        holder.itemView.setTag(table);
        holder.number.setText(table.getTableNumber());
        holder.availability.setText(table.isAvailable().toString());
        holder.customers.setText(table.getNumberOfCustomers());

        Log.d(TAG, "images/" + table.getImagePath());
        //Problemes
        FirebaseStorage.getInstance().getReference("images/" + table.getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null) {
                    Glide.with(context)
                            .load(uri)
                            .centerCrop()
                            .into(holder.imageView);
                }else{
                    Glide.with(context)
                            .load(R.drawable.speech_stage)
                            .centerCrop()
                            .into(holder.imageView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(context)
                        .load(R.drawable.speech_stage)
                        .centerCrop()
                        .into(holder.imageView);

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Glide.with(context)
                        .load(R.drawable.speech_stage)
                        .centerCrop()
                        .into(holder.imageView);

            }
        });


        holder.itemView.setOnClickListener(view -> {

//            FeedFragmentDirections.ActionFeedFragmentToViewEventFragment action = FeedFragmentDirections.actionFeedFragmentToViewEventFragment();
//            action.setEventId(event.getId());
            Navigation.findNavController(view).navigate(this.action.navigate(table.getTableId()));

        });
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, availability, customers;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.tvTableNumber);
            imageView = itemView.findViewById(R.id.ivTableImage);
            availability = itemView.findViewById(R.id.tvTableAvailable);
            customers = itemView.findViewById(R.id.tvTableCustomers);
        }
    }
}
*/