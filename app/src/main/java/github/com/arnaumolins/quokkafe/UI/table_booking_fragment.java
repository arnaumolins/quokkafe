package github.com.arnaumolins.quokkafe.UI;
/*
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Utils.TableItemAction;
import github.com.arnaumolins.quokkafe.Utils.TableListAdapter;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.TableViewModel;

public class table_booking_fragment extends Fragment {

    public table_booking_fragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private TableListAdapter adapter;
    AuthViewModel authViewModel;
    TableViewModel tableViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_booking_fragment, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        tableViewModel = new ViewModelProvider(this).get(TableViewModel.class);

        User user = authViewModel.getCurrentUser().getValue();

        //showedEvents.setValue(feedViewModel.getEventsWithGenre(user.interestedIn));
        recyclerView = view.findViewById(R.id.recyclerViewTable);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new TableListAdapter(getContext(), new TableItemAction() {
            @Override
            public NavDirections navigate(String id) {
                FeedFragmentDirections.ActionFeedFragmentToViewEventFragment action = FeedFragmentDirections.actionFeedFragmentToViewEventFragment();
                action.setEventId(id);
                return action;
            }
        });

        MutableLiveData<ArrayList<Table>> tablesToShow = TableViewModel.getTableMutableLiveData();
        tablesToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Table>>() {
            @Override
            public void onChanged(ArrayList<Table> tables) {
                Log.d("ASK!", user.interestedIn.toString());
                ArrayList<Table> tablesAvailable = new ArrayList<>();
                if(tables != null){
                    for (Table table : tables) {
                        Log.d("ASK", table.getTableNumber());
                        Log.d("ASK", table.getTableNumber());
                        if (table.isAvailable().equals(true)) {
                            tablesAvailable.add(table);
                        }
                    }
                }

                adapter.setEvents(tablesAvailable);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        Log.d("feedFragment", "4");
        return view;
    }
}*/