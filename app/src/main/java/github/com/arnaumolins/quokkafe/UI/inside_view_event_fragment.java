package github.com.arnaumolins.quokkafe.UI;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.Repository.ImageRepository;
import github.com.arnaumolins.quokkafe.ViewModel.ViewEventViewModel;

public class inside_view_event_fragment extends Fragment {

    MutableLiveData<Event> eventLiveData;
    LiveData<String> eventIdLiveData;

    private TextView eventName, eventInterest, eventDate, eventDescription, eventUsers;

    ViewEventViewModel viewEventViewModel;

    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> userAttendsEvent;

    public inside_view_event_fragment() {
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
        inside_view_event_fragmentArgs args = inside_view_event_fragmentArgs.fromBundle(getArguments());
        eventIdLiveData = new MutableLiveData<>(args.getItemId());
        View view = inflater.inflate(R.layout.fragment_inside_view_event_fragment, container, false);

        viewEventViewModel = new ViewModelProvider(this).get(ViewEventViewModel.class);

        userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();

        userAttendsEvent = new MutableLiveData<>();
        eventLiveData = new MutableLiveData<>();

        userAttendsEvent.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean attends) {
                if (attends) {
                    view.findViewById(R.id.IWillAttend).setBackgroundTintList(getContext().getResources().getColorStateList(R.color.light_brown));
                    ((Button) view.findViewById(R.id.IWillAttend)).setText("Leave event");

                    view.findViewById(R.id.IWillAttend).setOnClickListener(v -> {
                        view.findViewById(R.id.IWillAttend).setOnClickListener(null);
                        Event event = eventLiveData.getValue();
                        FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                Event e = currentData.getValue(Event.class);
                                if (e == null) {
                                    return Transaction.success(currentData);
                                }
                                e.getAssistants().remove(userMutableLiveData.getValue().userName);
                                currentData.setValue(e);
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                Event e = currentData.getValue(Event.class);
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("Users")
                                        .child(userMutableLiveData.getValue().userId)
                                        .child("attendingEventsIds")
                                        .child(event.getEventId()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                User currentUser = AuthRepository.getAuthRepository().getCurrentUser().getValue();
                                                if (currentUser != null) {
                                                    e.removeAssistant(currentUser.userName);
                                                }
                                                StringBuilder assistantsS = new StringBuilder("Assistants to the event\n_______________\n\n").append(event.getAssistantsString());
                                                ((TextView) view.findViewById(R.id.eventUsersPlaceholder)).setText(assistantsS);

                                                userAttendsEvent.setValue(false);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        });
                    });
                } else {
                    view.findViewById(R.id.IWillAttend).setBackgroundTintList(getContext().getResources().getColorStateList(R.color.dark_brown));
                    ((Button) view.findViewById(R.id.IWillAttend)).setText("Join event");
                    view.findViewById(R.id.IWillAttend).setOnClickListener(v -> {
                        view.findViewById(R.id.IWillAttend).setOnClickListener(null);
                        Event event = eventLiveData.getValue();
                        FirebaseDatabase.getInstance().getReference().child("Events").child(event.getEventId()).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                Event e = currentData.getValue(Event.class);
                                if (e == null) {
                                    return Transaction.success(currentData);
                                }
                                e.addAssistant(userMutableLiveData.getValue().userName);
                                currentData.setValue(e);
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                Event e = currentData.getValue(Event.class);
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("Users")
                                        .child(userMutableLiveData.getValue().userId)
                                        .child("attendingEventsIds")
                                        .child(event.getEventId())
                                        .setValue(event.getEventId())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                User currentUser = AuthRepository.getAuthRepository().getCurrentUser().getValue();
                                                if (currentUser != null) {
                                                    e.addAssistant(currentUser.userName);
                                                }

                                                StringBuilder assistantsS = new StringBuilder("Assistants to the event\n_______________\n\n").append(event.getAssistantsString());
                                                ((TextView) view.findViewById(R.id.eventUsersPlaceholder)).setText(assistantsS);

                                                userAttendsEvent.setValue(true);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        });
                    });
                }
            }
        });

        eventName = (TextView) view.findViewById(R.id.eventNamePlaceholder);
        eventDate = (TextView) view.findViewById(R.id.eventDatePlaceholder);
        eventDescription = (TextView) view.findViewById(R.id.eventDescriptionPlaceholder);
        eventDescription.setMovementMethod(new ScrollingMovementMethod());
        eventInterest = (TextView) view.findViewById(R.id.eventInterestPlaceholder);
        eventUsers = (TextView) view.findViewById(R.id.eventUsersPlaceholder);

        Query query = FirebaseDatabase.getInstance().getReference().child("Events").child(eventIdLiveData.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = snapshot.getValue(Event.class);
                eventName.setText(event.getEventName());

                String[] dateSArr = event.getDate().split("-");
                StringBuilder dateS = new StringBuilder(dateSArr[2]).append("/").append(dateSArr[1]).append("/").append(dateSArr[0]);
                eventDate.setText(dateS);

                eventInterest.setText(event.getInterest());

                StringBuilder descriptionS = new StringBuilder("Event description: ").append(event.getEventDescription());
                eventDescription.setText(descriptionS);

                StringBuilder assistantsS = new StringBuilder("Assistants to the event\n_______________\n\n").append(event.getAssistantsString());
                eventUsers.setText(assistantsS);

                ImageRepository.getInstance().getImageUri(event.getImagePath()).observe(getViewLifecycleOwner(), new Observer<Uri>() {
                    @Override
                    public void onChanged(Uri uri) {
                        if (uri != null) {
                            //     StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + eventIdLD.getValue() + "/" + eventIdLD.getValue() + ".jpg");
                            Glide.with(getContext())
                                    .load(uri)
                                    .centerCrop()
                                    .into((ImageView) view.findViewById(R.id.eventImagePlaceholder));
                        }
                    }
                });

                eventLiveData.setValue(event);

                MutableLiveData<List<String>> eventIds = AuthRepository.getAuthRepository().getCurrentUserAttendingEvents();
                eventIds.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> strings) {
                        boolean isAttending = false;
                        if (strings != null) {
                            for (String eventId : strings) {
                                if (event.getEventId().equals(eventId)) {
                                    isAttending = true;
                                    break;
                                }
                            }
                        }
                        userAttendsEvent.setValue(isAttending);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}