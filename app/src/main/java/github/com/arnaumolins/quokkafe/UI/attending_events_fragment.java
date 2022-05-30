package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Utils.EventItemAction;
import github.com.arnaumolins.quokkafe.Utils.EventListAdapter;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.EventViewModel;

public class attending_events_fragment extends Fragment {

    private static final String TAG = "UserAttendingEvents";

    private RecyclerView eventRV;
    private EventListAdapter eventListAdapter;

    AuthViewModel authViewModel;
    EventViewModel eventViewModel;

    public attending_events_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attending_events_fragment, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        User user = authViewModel.getCurrentUser().getValue();

        eventRV = view.findViewById(R.id.eventAttendingRV);
        eventRV.hasFixedSize();
        eventRV.setLayoutManager(new LinearLayoutManager(getContext()));
        eventRV.addItemDecoration(new DividerItemDecoration(eventRV.getContext(), DividerItemDecoration.VERTICAL));

        eventListAdapter = new EventListAdapter(getContext(), new EventItemAction() {
            @Override
            public NavDirections navigate(String id) {
                attending_events_fragmentDirections.ActionAttendingEventsFragmentToInsideViewEventFragment action = attending_events_fragmentDirections.actionAttendingEventsFragmentToInsideViewEventFragment();
                action.setItemId(id);
                return action;
            }
        });

        MutableLiveData<ArrayList<Event>> eventsToShow = eventViewModel.getEventMutableLiveData();
        eventsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                ArrayList<String> userEventIds = new ArrayList<>();
                ArrayList<Event> userEvents = new ArrayList<>();

                Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(user.userId).child("attendingEventsIds");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String userEventId = snap.getValue(String.class);
                            if (userEventId != null) {
                                userEventId = snap.getKey();
                                userEventIds.add(userEventId);
                            }
                        }

                        if (events != null) {
                            for (Event event : events) {
                                if (userEventIds.contains(event.getEventId())) {
                                    userEvents.add(event);
                                }
                            }

                            eventListAdapter.setEvents(userEvents);
                            eventRV.setAdapter(eventListAdapter);
                            eventListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;
    }
}