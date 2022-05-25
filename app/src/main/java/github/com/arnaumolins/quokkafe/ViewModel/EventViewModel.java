package github.com.arnaumolins.quokkafe.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Repository.EventRepository;

public class EventViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Event>> getEventMutableLiveData() {
        return EventRepository.getInstance().getAllEvents();
    }
}
