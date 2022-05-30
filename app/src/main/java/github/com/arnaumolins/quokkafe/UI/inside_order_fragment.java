package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Drinks;
import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.Food;
import github.com.arnaumolins.quokkafe.Model.Order;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.ViewModel.OrderViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.ViewEventViewModel;

public class inside_order_fragment extends Fragment {

    private TextView namePlaceHolder, pricePlaceHolder, datePlaceholder;
    private ListView itemListPlaceHolder;
    private Button deleteOrder;

    MutableLiveData<Order> orderLiveData;
    LiveData<String> orderIdLiveData;

    OrderViewModel viewOrderViewModel;

    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> userOwnsOrder;

    public inside_order_fragment() {
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
        inside_order_fragmentArgs args = inside_order_fragmentArgs.fromBundle(getArguments());
        orderIdLiveData = new MutableLiveData<>(args.getItemId());
        View view = inflater.inflate(R.layout.fragment_inside_order_fragment, container, false);


        viewOrderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();

        userOwnsOrder = new MutableLiveData<>();
        orderLiveData = new MutableLiveData<>();

        namePlaceHolder = (TextView) view.findViewById(R.id.orderNamePlaceHolder);
        pricePlaceHolder = (TextView) view.findViewById(R.id.pricePlaceholder);
        datePlaceholder = (TextView) view.findViewById(R.id.datePlaceholder);
        itemListPlaceHolder = (ListView) view.findViewById(R.id.itemListPlaceholder);
        deleteOrder = (Button) view.findViewById(R.id.deleteOrderButton);

        FirebaseDatabase.getInstance().getReference().child("Orders").child(orderIdLiveData.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Order order = snapshot.getValue(Order.class);
                namePlaceHolder.setText(order.getTableName());

                StringBuilder periodS = new StringBuilder(order.getStartingHour()).append(":").append(order.getStartingMinute()).append(" - ").append(order.getEndingHour()).append(":").append(order.getEndingMinute());
                datePlaceholder.setText(periodS);

                StringBuilder priceString = new StringBuilder(String.valueOf(order.getTotalPrice())).append(" .dkk");
                pricePlaceHolder.setText(priceString);

                ArrayList<String> itemList = new ArrayList<>();
                for(Drinks itemDrink : order.getDrinksArrayList()){
                    String drinkName = itemDrink.getDrinkName();
                    itemList.add(drinkName);
                }

                for (Food itemFood : order.getFoodArrayList()){
                    String foodName = itemFood.getFoodName();
                    itemList.add(foodName);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,itemList);
                itemListPlaceHolder.setAdapter(arrayAdapter);

                deleteOrder.setOnClickListener(l -> {
                    deleteOrderAction(order);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void deleteOrderAction(Order order){
        viewOrderViewModel.deleteOrder(order.getOrderId()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                viewOrderViewModel.deleteOrderOwnership(order.getOrderId(), userMutableLiveData.getValue().userId).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean bBoolean) {
                        Navigation.findNavController(getView()).navigate(R.id.action_inside_order_fragment_to_all_orders_fragment);
                    }
                });
            }
        });
    }
}