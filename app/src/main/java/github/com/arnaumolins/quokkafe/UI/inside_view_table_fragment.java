package github.com.arnaumolins.quokkafe.UI;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
    private DatePicker bookingDate;
    private TimePicker startingHour, endingHour;

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
        tableBookingsLiveData = bookingViewModel.getBookingMutableLiveData();
        ArrayList<Booking> bookings = tableBookingsLiveData.getValue();
        ArrayList<Booking> tableBookingsOnly = new ArrayList<>();
        if (bookings != null) {
            for (Booking booking : bookings) {
                if (booking.getTableId().equals(tableIdLiveData.getValue())) {
                    tableBookingsOnly.add(booking);
                }
            }
        }

        tableLiveData = new MutableLiveData<>();

        tableNumber = (TextView) view.findViewById(R.id.tableNumberPlaceholder);
        tableAvailability = (TextView) view.findViewById(R.id.tableAvailabilityPlaceholder);
        tableCostumers = (TextView) view.findViewById(R.id.tableCostumersPlaceholder);

        bookingDate = (DatePicker) view.findViewById(R.id.bookingDate);
        startingHour = (TimePicker) view.findViewById(R.id.startingHour);
        endingHour = (TimePicker) view.findViewById(R.id.endingHour);
        Button bookingButton = (Button) view.findViewById(R.id.tableBookingButton);

        Query query = FirebaseDatabase.getInstance().getReference().child("Tables").child(tableIdLiveData.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Table table = snapshot.getValue(Table.class);
                String tableNumberS = "Table number: " + table.getTableNumber();
                tableNumber.setText(tableNumberS);

                StringBuilder tableBookingsS = new StringBuilder("Booked hours \n_______________\n\n");
                if (!tableBookingsOnly.isEmpty()) {
                    for (Booking booking : tableBookingsOnly) {
                        tableBookingsS.append(booking.getBookingDay()).append("/").append(booking.getBookingMonth()).append("/").append(booking.getBookingYear()).append("\n");
                        tableBookingsS.append("From: ").append(booking.getStartingHour()).append(":").append(booking.getStartingMinute()).append(" to ").append(booking.getEndingHour()).append(":").append(booking.getEndingMinute()).append("\n\n");
                    }
                } else {
                    tableBookingsS.append("There aren't bookings for this table");
                }
                tableAvailability.setText(tableBookingsS.toString());

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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Log.d("Book table", "Going to book");
                Booking newBooking = new Booking("null", tableLiveData.getValue().getTableNumber(), tableIdLiveData.getValue(), userMutableLiveData.getValue().userId,
                        String.valueOf(bookingDate.getYear()),
                        String.valueOf(bookingDate.getMonth()),
                        String.valueOf(bookingDate.getDayOfMonth()),
                        String.valueOf(startingHour.getHour()),
                        String.valueOf(startingHour.getMinute()),
                        String.valueOf(endingHour.getHour()),
                        String.valueOf(endingHour.getMinute()));

                MutableLiveData<Booking> bookingMutableLiveData = new MutableLiveData<>();
                bookingMutableLiveData.setValue(newBooking);
                ArrayList<Booking> bookings = tableBookingsLiveData.getValue();

                if (!bookings.isEmpty()) {
                    for (Booking booking : bookings) {
                        if (bookingDate.getYear() == Integer.parseInt(booking.getBookingYear())) {
                            if (bookingDate.getMonth() == Integer.parseInt(booking.getBookingMonth())) {
                                if (bookingDate.getDayOfMonth() == Integer.parseInt(booking.getBookingDay())) {
                                    if (startingHour.getHour() >= Integer.parseInt(booking.getStartingHour()) && startingHour.getHour() < Integer.parseInt(booking.getEndingHour())
                                    || endingHour.getHour() <= Integer.parseInt(booking.getEndingHour()) && endingHour.getHour() > Integer.parseInt(booking.getStartingHour())) {
                                        Toast.makeText(getActivity(), "There already exists a booking during this period", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (checkBooking()) {
                                            bookTable(bookingMutableLiveData);
                                        }
                                    }
                                } else {
                                    if (checkBooking()) {
                                        bookTable(bookingMutableLiveData);
                                    }
                                }
                            } else {
                                if (checkBooking()) {
                                    bookTable(bookingMutableLiveData);
                                }
                            }
                        } else {
                            if (checkBooking()) {
                                bookTable(bookingMutableLiveData);
                            }
                        }
                    }

                } else {
                    if (checkBooking()) {
                        bookTable(bookingMutableLiveData);
                    }
                }
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkBooking() {
        if ((endingHour.getHour() - startingHour.getHour()) > 4) {
            Toast.makeText(getActivity(), "The maximum time is 4 hours", Toast.LENGTH_LONG).show();
            return false;
        } else if (endingHour.getHour() == startingHour.getHour()) {
            if ((endingHour.getMinute() - startingHour.getMinute()) < 30) {
                Toast.makeText(getActivity(), "The minimum time has to be 30 minutes", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void bookTable(MutableLiveData<Booking> booking) {
        bookingViewModel.createBooking(booking, userMutableLiveData);
        tableViewModel.addBookingMutableLiveData(booking, userMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bookingFinished) {
                Toast.makeText(getActivity(), "Booking has been registered", Toast.LENGTH_LONG).show();
            }
        });
    }
}