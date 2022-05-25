package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Drinks;
import github.com.arnaumolins.quokkafe.Model.Food;
import github.com.arnaumolins.quokkafe.Model.Order;
import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.Repository.TableRepository;

public class order_interface_fragment extends Fragment {

    private ArrayList<Drinks> drinksMenu = new ArrayList<Drinks>();
    private ArrayList<Food> foodMenu = new ArrayList<Food>();
    private Button createOrderButton;
    private ListView foodList, drinksList;
    private ProgressBar progressBar;
    private float totalPriceMenu;
    private Spinner tableBooked;


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
        ((MainActivity)getActivity()).unlockDrawerMenu();
        //TODO Buttons which are visible
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_interface_fragment, container, false);

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
        for (Food tmp : aFood){
            String tmpStr = tmp.getFoodName() + '\t' + '\t' + tmp.getFoodPrice() + " dkk";
            result.add(tmpStr);
        }
        return result;
    }

    private ArrayList<String> drinkToString(ArrayList<Drinks> aDrink) {
        ArrayList<String> result = new ArrayList<String>();
        for (Drinks tmp : aDrink){
            String tmpStr = tmp.getDrinkName() + '\t' + '\t' + tmp.getDrinkPrice() + " dkk";
            result.add(tmpStr);
        }
        return result;
    }

    private void generateOrder(ListView foodListView, ListView drinksListView) {
        ArrayList<Food> aFood = new ArrayList<>();
        ArrayList<Drinks> aDrink = new ArrayList<>();
        String tableBookedString = tableBooked.toString().trim();

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

        Boolean continueWithOrder = false;
        MutableLiveData<User> userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("User").child(userMutableLiveData.getValue().userId).child("ownedBookingIds").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

            }
        });

    }
}