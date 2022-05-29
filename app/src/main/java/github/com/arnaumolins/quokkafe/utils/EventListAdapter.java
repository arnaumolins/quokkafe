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

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.R;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    public static final String TAG = "EventListAdapter";
    private ArrayList<Event> events;
    private final Context context;
    private EventItemAction action;

    public EventListAdapter(Context context, EventItemAction action) {
        this.context = context;
        this.action = action;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_info, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Event event = events.get(position);
        viewHolder.itemView.setTag(event);
        viewHolder.name.setText(event.getEventName());

        String[] dateSArr = event.getDate().split("-");
        StringBuilder dateS = new StringBuilder(dateSArr[2]).append("/").append(dateSArr[1]).append("/").append(dateSArr[0]);
        viewHolder.date.setText(dateS);

        viewHolder.interest.setText(event.getInterest());

        StringBuilder descriptionS = new StringBuilder("Event description: ").append(event.getEventDescription());
        viewHolder.description.setText(descriptionS);

        FirebaseStorage.getInstance().getReference("images/" + event.getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
            Navigation.findNavController(view).navigate(this.action.navigate(event.getEventId()));
        });
    }

    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, interest, description, date;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.event_img);
            name = itemView.findViewById(R.id.event_name);
            date = itemView.findViewById(R.id.event_date);
            interest = itemView.findViewById(R.id.event_interest);
            description = itemView.findViewById(R.id.event_description);
        }
    }
}
