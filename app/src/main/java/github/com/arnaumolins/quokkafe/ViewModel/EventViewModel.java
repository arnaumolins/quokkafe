package github.com.arnaumolins.quokkafe.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Repository.EventRepository;

public class EventViewModel extends ViewModel {

    EventRepository eventRepository = EventRepository.getInstance();

    public MutableLiveData<ArrayList<Event>> getEventMutableLiveData() {
        return EventRepository.getInstance().getAllEvents();
    }

    public MutableLiveData<ArrayList<Event>> getEventWithTheme(String theme) {
        MutableLiveData<ArrayList<Event>> eventThemeLiveData = new MutableLiveData<>();
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : EventRepository.getInstance().getAllEvents().getValue()) {
            if (event.getTheme().equals(theme)) {
                events.add(event);
            }
        }
        eventThemeLiveData.setValue(events);
        return eventThemeLiveData;
    }
}
