package github.com.arnaumolins.quokkafe.utils;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Event;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    public static final String TAG = "EventListAdapter";
    private ArrayList<Event> events;
    private final Context context;
    private EventItemAction action;

    public EventListAdapter(Context context, EventItemAction action) {
        this.context = context;
        this.action = action;
    }

    class ViewHolder {

    }

}
