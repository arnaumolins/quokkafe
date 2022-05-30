package github.com.arnaumolins.quokkafe.UI;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import github.com.arnaumolins.quokkafe.Model.Booking;
import github.com.arnaumolins.quokkafe.Model.Drinks;
import github.com.arnaumolins.quokkafe.Model.Food;
import github.com.arnaumolins.quokkafe.Model.Order;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.ViewModel.BookingViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.OrderViewModel;

public class order_interface_fragment extends Fragment {

    private ArrayList<Drinks> drinksMenu = new ArrayList<Drinks>();
    private ArrayList<Food> foodMenu = new ArrayList<Food>();
    private Button createOrderButton;
    private ListView foodList, drinksList;
    private ProgressBar progressBar;
    private float totalPriceMenu;
    private Spinner tableBooked;
    private OrderViewModel orderViewModel;
    BookingViewModel bookingViewModel;
    MutableLiveData<ArrayList<Booking>> BookingsLiveData;

    public order_interface_fragment() {
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
        ((MainActivity) getActivity()).unlockDrawerMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_interface_fragment, container, false);


        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        drinksMenu.add(new Drinks("Espresso", 0));
        drinksMenu.add(new Drinks("Latte", 0));
        drinksMenu.add(new Drinks("Cappuccino", 0));
        drinksMenu.add(new Drinks("Hot Chocolate", 0));
        drinksMenu.add(new Drinks("Tea", 0));
        drinksMenu.add(new Drinks("Soda", 0));
        drinksMenu.add(new Drinks("Americano", 0));
        drinksMenu.add(new Drinks("Mocha", 0));
        drinksMenu.add(new Drinks("Ice cafe", 0));
        drinksMenu.add(new Drinks("Juice", 0));

        foodMenu.add(new Food("Cookies", 25));
        foodMenu.add(new Food("Muffin", 30));
        foodMenu.add(new Food("Waffle", 30));
        foodMenu.add(new Food("Croissant", 25));
        foodMenu.add(new Food("Cinnamon Roll", 35));
        foodMenu.add(new Food("Pain au chocolat", 30));


        foodList = (ListView) view.findViewById(R.id.foodListView);
        drinksList = (ListView) view.findViewById(R.id.drinksListView);
        createOrderButton = (Button) view.findViewById(R.id.createOrderButton);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarOrder);
        tableBooked = (Spinner) view.findViewById(R.id.tableSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tables));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableBooked.setAdapter(adapter);

        ArrayAdapter<String> foodAdaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, foodToString(foodMenu));
        foodList.setAdapter(foodAdaptor);

        ArrayAdapter<String> drinksAdaptor = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, drinkToString(drinksMenu));
        drinksList.setAdapter(drinksAdaptor);

        createOrderButton.setOnClickListener(l -> {
            generateOrder(foodList, drinksList);
        });

        return view;
    }

    private ArrayList<String> foodToString(ArrayList<Food> aFood) {
        ArrayList<String> result = new ArrayList<String>();
        for (Food tmp : aFood) {
            String tmpStr = tmp.getFoodName() + '\t' + '\t' + tmp.getFoodPrice() + " dkk";
            result.add(tmpStr);
        }
        return result;
    }

    private ArrayList<String> drinkToString(ArrayList<Drinks> aDrink) {
        ArrayList<String> result = new ArrayList<String>();
        for (Drinks tmp : aDrink) {
            String tmpStr = tmp.getDrinkName() + '\t' + '\t' + tmp.getDrinkPrice() + " dkk";
            result.add(tmpStr);
        }
        return result;
    }

    private void generateOrder(ListView foodListView, ListView drinksListView) {
        ArrayList<Food> aFood = new ArrayList<>();
        ArrayList<Drinks> aDrink = new ArrayList<>();
        String tableBookedString = tableBooked.getSelectedItem().toString().trim();

        for (int i = 0; i < foodListView.getCount(); i++) {
            if (foodListView.isItemChecked(i)) {
                aFood.add(foodMenu.get(i));
            }
        }

        for (int i = 0; i < drinksListView.getCount(); i++) {
            if (drinksListView.isItemChecked(i)) {
                aDrink.add(drinksMenu.get(i));
            }
        }

        for (int i = 0; i < aFood.size(); i++) {
            totalPriceMenu += aFood.get(i).getFoodPrice();
        }

        Calendar calendar = Calendar.getInstance();
        int actualHour = calendar.get(Calendar.HOUR_OF_DAY);

        MutableLiveData<Order> orderMutableLiveData = new MutableLiveData<>();
        MutableLiveData<User> userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("Users").child(userMutableLiveData.getValue().userId).child("ownedBookingIds").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ArrayList<String> bookingsIds = new ArrayList<>();
                for (DataSnapshot sp : dataSnapshot.getChildren()) {
                    bookingsIds.add(sp.getValue(String.class));
                }

                if (!bookingsIds.isEmpty()) {
                    BookingsLiveData = bookingViewModel.getBookingMutableLiveData();
                    BookingsLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Booking>>() {
                        @Override
                        public void onChanged(ArrayList<Booking> bookings) {
                            ArrayList<Booking> bookingArrayList = new ArrayList<>();
                            if (bookings != null){
                                for (Booking book : bookings) {
                                    if (bookingsIds.contains(book.getBookingId())) {
                                        Log.d("allBookings", book.getTableName());
                                        bookingArrayList.add(book);
                                    }
                                }

                                Log.d("allBookings", bookingArrayList.toString());
                                for (Booking b : bookingArrayList) {
                                    Log.d("allBookings", b.getTableName());
                                    if (b.getTableName().equals(tableBookedString)) {
                                        Log.d("allBookings", String.valueOf(b.getEndingHour()));
                                        if (Integer.parseInt(b.getStartingHour()) <= actualHour && actualHour <= Integer.parseInt(b.getEndingHour())) {
                                            float priceBooking = 0;
                                            float differenceHours = Integer.parseInt(b.getEndingHour()) - Integer.parseInt(b.getStartingHour());
                                            if (differenceHours <= 4) {
                                                priceBooking = getPriceBooking(differenceHours);
                                            }
                                            String bName = b.getTableName();
                                            String bSHour = b.getStartingHour();
                                            String bEHour = b.getEndingHour();
                                            String bSMinute = b.getStartingMinute();
                                            String bEMinute = b.getEndingMinute();
                                            Order order = new Order(null, bName, bSHour, bEHour, bSMinute, bEMinute, aFood, aDrink, priceBooking + totalPriceMenu);
                                            orderMutableLiveData.setValue(order);

                                            orderViewModel.setOrder(orderMutableLiveData, userMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                                @Override
                                                public void onChanged(Boolean aBoolean) {
                                                    if (aBoolean != null && aBoolean) {
                                                        Toast.makeText(getActivity(), "Order has been registered successfully!", Toast.LENGTH_LONG).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        Navigation.findNavController(getView()).navigate(R.id.action_order_interface_fragment_to_event_interface_fragment);
                                                    } else {
                                                        Toast.makeText(getActivity(), "Failed to register order!", Toast.LENGTH_LONG).show();
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Failed to register order!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
            }
        });

    }

    private float getPriceBooking(float differenceHours) {
        if (0 < differenceHours && differenceHours <= 1) {
            return 59;
        } else if (1 < differenceHours && differenceHours <= 2) {
            return 87;
        } else if (2 < differenceHours && differenceHours <= 3) {
            return 115;
        } else if (3 < differenceHours && differenceHours <= 4) {
            return 143;
        }
        return 0;
    }
}