package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Booking;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Utils.BookingListAdapter;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;
import github.com.arnaumolins.quokkafe.ViewModel.BookingViewModel;


public class booked_tables_fragment extends Fragment {

    private static final String TAG = "UserBookedTables";

    private RecyclerView bookedTablesRV;
    private BookingListAdapter bookingListAdapter;

    AuthViewModel authViewModel;
    BookingViewModel bookingViewModel;

    public booked_tables_fragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booked_tables_fragment, container, false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        User user = authViewModel.getCurrentUser().getValue();

        bookedTablesRV = view.findViewById(R.id.bookedTablesRV);
        bookedTablesRV.hasFixedSize();
        bookedTablesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        bookedTablesRV.addItemDecoration(new DividerItemDecoration(bookedTablesRV.getContext(), DividerItemDecoration.VERTICAL));

        bookingListAdapter = new BookingListAdapter(getContext());

        MutableLiveData<ArrayList<Booking>> bookingsToShow = bookingViewModel.getBookingMutableLiveData();
        bookingsToShow.observe(getViewLifecycleOwner(), new Observer<ArrayList<Booking>>() {
            @Override
            public void onChanged(ArrayList<Booking> bookings) {
                ArrayList<String> userBookingIds = new ArrayList<>();
                ArrayList<Booking> userBookings = new ArrayList<>();

                Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(user.userId).child("ownedBookingIds");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String userBookingId = snap.getValue(String.class);
                            if (userBookingId != null) {
                                userBookingId = snap.getKey();
                                userBookingIds.add(userBookingId);
                            }
                        }

                        if (bookings != null) {
                            for (Booking booking : bookings) {
                                if (userBookingIds.contains(booking.getBookingId())) {
                                    userBookings.add(booking);
                                }
                            }

                            bookingListAdapter.setBookings(userBookings);
                            bookedTablesRV.setAdapter(bookingListAdapter);
                            bookingListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;
    }
}