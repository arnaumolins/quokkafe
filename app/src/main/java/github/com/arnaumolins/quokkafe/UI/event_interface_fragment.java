package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.EventViewModel;
import github.com.arnaumolins.quokkafe.utils.EventItemAction;
import github.com.arnaumolins.quokkafe.utils.EventListAdapter;

public class event_interface_fragment extends Fragment {

    public static final String TAG = "Event interface";

    private RecyclerView eventRV;
    private Spinner interestSpinner;
    private EventListAdapter eventListAdapter;

    AuthViewModel authViewModel;
    EventViewModel eventViewModel;

    public event_interface_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
        //TODO Buttons which are visible
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_interface_fragment, container, false);

        Log.d(TAG, "1");

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        User user = authViewModel.getCurrentUser().getValue();

        Log.d(TAG, "2");

        interestSpinner = (Spinner) view.findViewById(R.id.interestSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.InterestsList, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.fragment_event_interface_fragment);
        interestSpinner.setAdapter(adapter);

        interestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                MutableLiveData<ArrayList<Event>> eventsToShow = eventViewModel.getEventWithInterest((String) item);
                eventsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
                    @Override
                    public void onChanged(ArrayList<Event> events) {
                        ArrayList<Event> eventsWithInterest = new ArrayList<>();
                        if (events != null) {
                            for (Event event : events) {
                                for (String interest : user.interestedIn){
                                    if (event.getInterest().equals(interest)) {
                                        eventsWithInterest.add(event);
                                    }
                                }
                            }
                        }

                        eventListAdapter.setEvents(eventsWithInterest);
                        eventRV.setAdapter(eventListAdapter);
                        eventListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        eventRV = view.findViewById(R.id.eventRV);
        eventRV.hasFixedSize();
        eventRV.setLayoutManager(new LinearLayoutManager(getContext()));

        eventRV.addItemDecoration(new DividerItemDecoration(eventRV.getContext(), DividerItemDecoration.VERTICAL));

        eventListAdapter = new EventListAdapter(getContext(), new EventItemAction() {
            @Override
            public NavDirections navigate(String id) {
                Navigation.findNavController(getView()).navigate(R.id.action_event_interface_fragment_to_inside_view_event_fragment);
                return null;
            }
        });

        Log.d(TAG, "3");

        MutableLiveData<ArrayList<Event>> eventsToShow = eventViewModel.getEventMutableLiveData();
        eventsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                ArrayList<Event> eventsWithInterest = new ArrayList<>();
                if (events != null) {
                    for (Event event : events) {
                        for (String interest : user.interestedIn){
                            if (event.getInterest().equals(interest)) {
                                eventsWithInterest.add(event);
                            }
                        }
                    }
                }

                eventListAdapter.setEvents(eventsWithInterest);
                eventRV.setAdapter(eventListAdapter);
                eventListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}