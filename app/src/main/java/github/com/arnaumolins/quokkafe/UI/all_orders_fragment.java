package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Utils.OrderListAdapter;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;

public class all_orders_fragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderListAdapter adapter;
    private AuthViewModel authViewModel;

    public all_orders_fragment() {
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
        ((MainActivity)getActivity()).unlockDrawerMenu();
        //TODO Buttons which are visible
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_orders_fragment, container, false);
        return view;
    }
}