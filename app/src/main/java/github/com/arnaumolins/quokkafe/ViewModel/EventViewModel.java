package github.com.arnaumolins.quokkafe.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Repository.EventRepository;

public class EventViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Event>> getEventMutableLiveData() {
        return EventRepository.getInstance().getAllEvents();
    }

    public MutableLiveData<ArrayList<Event>> getEventWithInterest(String interest) {
        MutableLiveData<ArrayList<Event>> eventThemeLiveData = new MutableLiveData<>();
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : Objects.requireNonNull(EventRepository.getInstance().getAllEvents().getValue())) {
            if (event.getInterest().equals(interest)) {
                events.add(event);
            }
        }
        eventThemeLiveData.setValue(events);
        return eventThemeLiveData;
    }
}
