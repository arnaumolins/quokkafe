package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Utils.TableItemAction;
import github.com.arnaumolins.quokkafe.Utils.TableListAdapter;
import github.com.arnaumolins.quokkafe.ViewModel.TableViewModel;

public class table_booking_fragment extends Fragment {
    private RecyclerView tableRV;
    private TableListAdapter tableListAdapter;

    TableViewModel tableViewModel;

    public table_booking_fragment() {
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
        ((MainActivity)getActivity()).lockDrawerMenu();
        //TODO Buttons which are visible
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_booking_fragment, container, false);

        tableViewModel = new ViewModelProvider(this).get(TableViewModel.class);

        tableRV = view.findViewById(R.id.tableRV);
        tableRV.hasFixedSize();
        tableRV.setLayoutManager(new LinearLayoutManager(getContext()));
        tableRV.addItemDecoration(new DividerItemDecoration(tableRV.getContext(), DividerItemDecoration.VERTICAL));

        tableListAdapter = new TableListAdapter(getContext(), new TableItemAction() {
            @Override
            public NavDirections navigate(String id) {
                table_booking_fragmentDirections.ActionTableBookingFragmentToInsideViewTableFragment action = table_booking_fragmentDirections.actionTableBookingFragmentToInsideViewTableFragment();
                action.setItemId(id);
                return action;
            }
        });

        MutableLiveData<ArrayList<Table>> tablesToShow = tableViewModel.getTableMutableLiveData();
        tablesToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Table>>() {
            @Override
            public void onChanged(ArrayList<Table> tables) {
                tableListAdapter.setTables(tables);
                tableRV.setAdapter(tableListAdapter);
                tableListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}