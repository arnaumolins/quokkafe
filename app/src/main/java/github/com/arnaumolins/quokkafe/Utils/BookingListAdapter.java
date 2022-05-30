package github.com.arnaumolins.quokkafe.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Booking;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.BookingRepository;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {

    public static final String TAG = "BookingListAdapter";
    private ArrayList<Booking> bookings;
    private final Context context;

    BookingRepository bookingRepository;

    public  BookingListAdapter(Context context) {
        this.context = context;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.booking_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Booking booking = bookings.get(position);
        viewHolder.itemView.setTag(booking);

        String tableNumberS = "Table number: " + booking.getTableName();
        viewHolder.number.setText(tableNumberS);

        String bookedTimeS = booking.getBookingDay() + "/" + booking.getBookingMonth() + "/" + booking.getBookingYear()
                + "\n" + booking.getStartingHour() + ":" + booking.getStartingMinute() + " - " + booking.getEndingHour() + ":" + booking.getEndingMinute();
        viewHolder.time.setText(bookedTimeS);

        bookingRepository = BookingRepository.getInstance();
        viewHolder.deleteBooking.setOnClickListener(l -> {
            bookingRepository.deleteBooking(booking.getBookingId());
        });

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, time;
        Button deleteBooking;

        public ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.tvBookedTableNumber);
            time = itemView.findViewById(R.id.tvBookedTableTime);
            deleteBooking = itemView.findViewById(R.id.deleteBookingBtn);
        }
    }
}
