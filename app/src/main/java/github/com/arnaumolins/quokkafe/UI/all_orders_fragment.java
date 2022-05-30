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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Order;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Utils.OrderItemAction;
import github.com.arnaumolins.quokkafe.Utils.OrderListAdapter;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.OrderViewModel;

public class all_orders_fragment extends Fragment {

    private RecyclerView orderRV;
    private OrderListAdapter orderAdapter;
    OrderViewModel ordersViewModel;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_orders_fragment, container, false);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        ordersViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        User user = authViewModel.getCurrentUser().getValue();

        orderRV = view.findViewById(R.id.orderRV);
        orderRV.hasFixedSize();
        orderRV.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRV.addItemDecoration(new DividerItemDecoration(orderRV.getContext(), DividerItemDecoration.VERTICAL));

        orderAdapter = new OrderListAdapter(getContext(), new OrderItemAction() {
            @Override
            public NavDirections navigate(String id) {
                all_orders_fragmentDirections.ActionAllOrdersFragmentToInsideOrderFragment action = all_orders_fragmentDirections.actionAllOrdersFragmentToInsideOrderFragment();
                action.setItemId(id);
                return action;
            }
        });

        FirebaseDatabase.getInstance().getReference("Users").child(user.userId).child("ownedOrdersIds").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<String> orderIds = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    orderIds.add(ds.getValue(String.class));
                }


                MutableLiveData<ArrayList<Order>> OrdersLiveData = ordersViewModel.getOrderMutableLiveData();
                OrdersLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Order>>() {
                    @Override
                    public void onChanged(ArrayList<Order> orders) {
                        ArrayList<Order> ownedOrders = new ArrayList<>();
                        if (orders != null) {
                            for (Order order : orders) {
                                if (orderIds.contains(order.getOrderId())) {
                                    ownedOrders.add(order);
                                }
                            }
                            orderAdapter.setOrders(ownedOrders);
                            orderRV.setAdapter(orderAdapter);
                            orderAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });

        return view;
    }
}