package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Utils.EventItemAction;
import github.com.arnaumolins.quokkafe.Utils.EventListAdapter;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.EventViewModel;

public class event_interface_fragment extends Fragment {

    private RecyclerView eventRV;
    private EventListAdapter eventListAdapter, eventListInterestAdapter;
    private Spinner interestSpinner;
    private Button searchButton;

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
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_interface_fragment, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        User user = authViewModel.getCurrentUser().getValue();

        interestSpinner = (Spinner) view.findViewById(R.id.interestSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.InterestsList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestSpinner.setAdapter(adapter);

        searchButton = (Button) view.findViewById(R.id.eventSearchButton);
        searchButton.setOnClickListener(l -> {
            searchButton.setEnabled(false);
            showEventWithInterest();
        });

        eventRV = view.findViewById(R.id.eventRV);
        eventRV.hasFixedSize();
        eventRV.setLayoutManager(new LinearLayoutManager(getContext()));
        eventRV.addItemDecoration(new DividerItemDecoration(eventRV.getContext(), DividerItemDecoration.VERTICAL));

        eventListAdapter = new EventListAdapter(getContext(), new EventItemAction() {
            @Override
            public NavDirections navigate(String id) {
                event_interface_fragmentDirections.ActionEventInterfaceFragmentToInsideViewEventFragment action = event_interface_fragmentDirections.actionEventInterfaceFragmentToInsideViewEventFragment();
                action.setItemId(id);
                return action;
            }
        });

        MutableLiveData<ArrayList<Event>> eventsToShow = eventViewModel.getEventMutableLiveData();
        eventsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                ArrayList<Event> eventsWithInterest = new ArrayList<>();
                if (events != null) {
                    for (Event event : events) {
                        ArrayList<String> userInterests = user.getInterestedIn();
                        for (String interested : userInterests){
                            if (event.getInterest().equals(interested)) {
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

    public void showEventWithInterest(){
        eventListInterestAdapter = new EventListAdapter(getContext(), new EventItemAction() {
            @Override
            public NavDirections navigate(String id) {
                event_interface_fragmentDirections.ActionEventInterfaceFragmentToInsideViewEventFragment action = event_interface_fragmentDirections.actionEventInterfaceFragmentToInsideViewEventFragment();
                action.setItemId(id);
                return action;
            }
        });

        String userSearch = interestSpinner.getSelectedItem().toString().trim();
        if (!userSearch.equals("Choose and interest???")) {
            MutableLiveData<ArrayList<Event>> eventsToShow = eventViewModel.getEventMutableLiveData();
            eventsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Event>>() {
                @Override
                public void onChanged(ArrayList<Event> events) {
                    ArrayList<Event> eventsWithInterest = new ArrayList<>();
                    if (events != null) {
                        for (Event event : events) {
                            if (event.getInterest().equals(userSearch)) {
                                eventsWithInterest.add(event);
                            }
                        }
                    }

                    eventListInterestAdapter.setEvents(eventsWithInterest);
                    eventRV.setAdapter(eventListInterestAdapter);
                    eventListInterestAdapter.notifyDataSetChanged();
                    searchButton.setEnabled(true);
                }
            });
        }
    }
}