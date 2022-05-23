package github.com.arnaumolins.quokkafe.ViewModel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.Repository.EventRepository;
import github.com.arnaumolins.quokkafe.Repository.ImageRepository;


public class CreateEventViewModel extends ViewModel {

    public MutableLiveData<Boolean> setEvent(MutableLiveData<Event> event, MutableLiveData<User> user) {
        return EventRepository.getInstance().setEvent(event, user);
    }

    public MutableLiveData<Boolean> setEventImage(String imagePath, MutableLiveData<Uri> uriMutableLiveData) {
        return ImageRepository.getInstance().uploadImage(imagePath, uriMutableLiveData);

    }
}