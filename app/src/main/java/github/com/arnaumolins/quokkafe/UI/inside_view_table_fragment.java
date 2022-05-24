package github.com.arnaumolins.quokkafe.UI;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.Repository.ImageRepository;
import github.com.arnaumolins.quokkafe.ViewModel.ViewTableViewModel;

public class inside_view_table_fragment extends Fragment {
    MutableLiveData<Table> tableLiveData;
    LiveData<String> tableIdLiveData;
    private TextView tableNumber, tableAvailability, tableCostumers;
    private DatePicker startingHour, endingHour;
    private Button bookingButton;

    ViewTableViewModel viewTableViewModel;
    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> usersBooksTable;

    public inside_view_table_fragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inside_view_table_fragmentArgs args = inside_view_table_fragmentArgs.fromBundle(getArguments());
        tableIdLiveData = new MutableLiveData<>(args.getItemId());
        View view = inflater.inflate(R.layout.fragment_inside_view_table_fragment, container, false);

        viewTableViewModel = new ViewModelProvider(this).get(ViewTableViewModel.class);

        userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();

        usersBooksTable = new MutableLiveData<>();
        tableLiveData = new MutableLiveData<>();

        usersBooksTable.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean books) {
                if (books) {

                } else {

                }
            }
        });

        tableNumber = (TextView) view.findViewById(R.id.tableNumberPlaceholder);
        tableAvailability = (TextView) view.findViewById(R.id.tableAvailabilityPlaceholder);
        tableCostumers = (TextView) view.findViewById(R.id.tableCostumersPlaceholder);
        startingHour = (DatePicker) view.findViewById(R.id.startingHour);
        endingHour = (DatePicker) view.findViewById(R.id.endingHour);
        bookingButton = (Button) view.findViewById(R.id.tableBookingButton);
        bookingButton.setOnClickListener(l -> {

        });

        Query query = FirebaseDatabase.getInstance().getReference().child("Tables").child(tableIdLiveData.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Table table = snapshot.getValue(Table.class);

                tableNumber.setText("Table number: " + table.getTableNumber());
                if (table.isAvailable()) {
                    tableAvailability.setText("Available");
                } else {
                    tableAvailability.setText("Not available");
                }
                tableCostumers.setText(table.getNumberOfCustomers());
                ImageRepository.getInstance().getImageUri(table.getImagePath()).observe(getViewLifecycleOwner(), new Observer<Uri>() {
                    @Override
                    public void onChanged(Uri uri) {
                        if (uri != null) {
                            Glide.with(getContext())
                                    .load(uri)
                                    .centerCrop()
                                    .into((ImageView) view.findViewById(R.id.tableImagePlaceholder));
                        }
                    }
                });

                tableLiveData.setValue(table);

                MutableLiveData<List<String>> tableIds = AuthRepository.getAuthRepository().getCurrentUserAttendingEvents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}