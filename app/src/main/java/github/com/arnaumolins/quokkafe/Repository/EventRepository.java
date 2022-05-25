package github.com.arnaumolins.quokkafe.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.User;

public class EventRepository {

    private static final String TAG = "EventRepository";
    private static EventRepository instance;
    private static MutableLiveData<ArrayList<Event>> eventsLiveData;

    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
            eventsLiveData = new MutableLiveData<>(null);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Event>> getAllEvents() {
        Log.i(TAG, "Getting all the events");
        if (eventsLiveData.getValue() == null) {
            FirebaseDatabase.getInstance().getReference("Events").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Event> events = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Event event = snap.getValue(Event.class);
                        if (event != null) {
                            event.setEventId(snap.getKey());
                            Log.d(TAG, "Child with id " + snap.getKey());
                            Log.d(TAG, event.toString());
                            events.add(event);
                        } else {
                            Log.e(TAG, "Event with id " + snap.getKey() + " is not valid");
                        }
                    }
                    eventsLiveData.setValue(events);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Getting all events is cancelled");
                    Log.e(TAG, error.getMessage());
                    Log.e(TAG, error.getDetails());
                }
            });
        }
        return eventsLiveData;
    }

    // TODO Check this at the end
    public MutableLiveData<Boolean> setEvent(MutableLiveData<Event> event, MutableLiveData<User> user) {
        MutableLiveData<Boolean> setEventState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Events");
        String eventId = dbRef.push().getKey();
        event.getValue().setEventId(eventId);
        Log.d(TAG, "Set event with id " + eventId);
        dbRef.child(eventId).setValue(event.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Event created with id " + eventId);
                    FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(user.getValue().userId)
                            .child("ownedEventIds")
                            .child(eventId)
                            .setValue(eventId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User " + user.getValue().userId + " is owner of " + eventId);
                                        setEventState.setValue(true);
                                    } else {
                                        Log.d("TAG", "User " + user.getValue().userId + " is not owner of " + eventId);
                                        setEventState.setValue(false);
                                    }
                                }
                            });
                } else {
                    Log.e(TAG, "Can not create the event!");
                    setEventState.setValue(false);
                }
            }
        });
        return setEventState;
    }
}
