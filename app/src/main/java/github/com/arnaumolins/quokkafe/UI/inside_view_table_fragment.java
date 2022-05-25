package github.com.arnaumolins.quokkafe.UI;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Booking;
import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.Repository.ImageRepository;
import github.com.arnaumolins.quokkafe.ViewModel.BookingViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.TableViewModel;

public class inside_view_table_fragment extends Fragment {

    MutableLiveData<Table> tableLiveData;
    LiveData<String> tableIdLiveData;

    private TextView tableNumber, tableAvailability, tableCostumers;
    private ListView bookingAvailability;
    private DatePicker startingDate, endingDate;
    private TimePicker startingHour, endingHour;
    private Button bookingButton;

    TableViewModel tableViewModel;
    BookingViewModel bookingViewModel;

    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<ArrayList<Booking>> tableBookingsLiveData;

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

        tableViewModel = new ViewModelProvider(this).get(TableViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();

        tableBookingsLiveData = tableViewModel.getBookingsMutableLiveData(tableIdLiveData.getValue());

        tableNumber = (TextView) view.findViewById(R.id.tableNumberPlaceholder);
        tableAvailability = (TextView) view.findViewById(R.id.tableAvailabilityPlaceholder);
        tableCostumers = (TextView) view.findViewById(R.id.tableCostumersPlaceholder);
        bookingAvailability = (ListView) view.findViewById(R.id.bookingAvailability);
        // TODO add adapter
        startingDate = (DatePicker) view.findViewById(R.id.startingDate);
        endingDate = (DatePicker) view.findViewById(R.id.endingDate);
        startingHour = (TimePicker) view.findViewById(R.id.startingHour);
        endingHour = (TimePicker) view.findViewById(R.id.endingHour);
        bookingButton = (Button) view.findViewById(R.id.tableBookingButton);

        // TODO Display data
        Query query = FirebaseDatabase.getInstance().getReference().child("Tables").child(tableIdLiveData.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Table table = snapshot.getValue(Table.class);
                String tableNumberS = "Table number: " + table.getTableNumber();
                tableNumber.setText(tableNumberS);


                tableAvailability.setText(TextUtils.join("\n ", tableBookingsLiveData.getValue()));

                String tableCostumerS = "Number of costumers: " + table.getNumberOfCustomers();
                tableCostumers.setText(tableCostumerS);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookingButton.setOnClickListener(new View.OnClickListener() {
            Booking newBooking = new Booking("null", tableIdLiveData.getValue(), userMutableLiveData.getValue().userId, tableLiveData.getValue().getTableNumber(), startingDate, endingDate, startingHour, endingHour);

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ArrayList<Booking> bookings = tableBookingsLiveData.getValue();
                for (Booking booking : bookings) {
                    if (startingDate.equals(booking.getStartingDate())) {
                        if (startingHour.equals(booking.getStartingHour()) || (startingHour.getHour() > booking.getStartingHour().getHour() && startingHour.getHour() < booking.getEndingHour().getHour())) {
                            Toast.makeText(getActivity(), "There already exists a booking during this period", Toast.LENGTH_LONG).show();
                        } else {
                            if ((endingHour.getMinute() - startingHour.getMinute()) < 30) {
                                Toast.makeText(getActivity(), "The booking time has to be at least 30  minutes", Toast.LENGTH_LONG).show();
                            } else {
                                bookingViewModel.createBooking(new MutableLiveData<>(newBooking), userMutableLiveData);
                                tableViewModel.addBookingMutableLiveData(new MutableLiveData<>(newBooking), userMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(Boolean bookingFinished) {
                                        Toast.makeText(getActivity(), "Booking has been registered", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    } else {
                        if ((endingHour.getMinute() - startingHour.getMinute()) < 30) {
                            Toast.makeText(getActivity(), "The booking time has to be at least 30  minutes", Toast.LENGTH_LONG).show();
                        } else {
                            bookingViewModel.createBooking(new MutableLiveData<>(newBooking), userMutableLiveData);
                            tableViewModel.addBookingMutableLiveData(new MutableLiveData<>(newBooking), userMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean bookingFinished) {
                                    Toast.makeText(getActivity(), "Booking has been registered", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }

            }
        });
        return view;
    }
}