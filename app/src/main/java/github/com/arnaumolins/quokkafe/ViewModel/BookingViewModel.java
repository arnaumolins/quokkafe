package github.com.arnaumolins.quokkafe.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import github.com.arnaumolins.quokkafe.Model.Booking;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.Repository.BookingRepository;

public class BookingViewModel extends ViewModel {

    public MutableLiveData<Boolean> createBooking(MutableLiveData<Booking> booking, MutableLiveData<User> user) {
        return BookingRepository.getInstance().createBooking(booking, user);
    }

    //TODO use this
    public MutableLiveData<Boolean> deleteBooking(String bookingId) {
        return BookingRepository.getInstance().deleteBooking(bookingId);
    }
}
