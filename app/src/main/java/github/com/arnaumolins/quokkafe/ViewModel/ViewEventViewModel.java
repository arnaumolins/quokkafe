package github.com.arnaumolins.quokkafe.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.Repository.EventRepository;
import github.com.arnaumolins.quokkafe.Repository.ImageRepository;

public class ViewEventViewModel extends ViewModel {

    public MutableLiveData<Boolean> deleteEvent(String eventId) {
        return EventRepository.getInstance().deleteEvent(eventId);
    }

    public MutableLiveData<Boolean> deleteEventImage(String imgPath) {
        return ImageRepository.getInstance().deleteImage(imgPath);
    }

    public MutableLiveData<Boolean> deleteEventAttendees(String eventId) {
        return AuthRepository.getAuthRepository().deleteEventAttendees(eventId);
    }
}
