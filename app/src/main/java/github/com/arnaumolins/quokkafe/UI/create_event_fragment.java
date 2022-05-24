package github.com.arnaumolins.quokkafe.UI;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.ViewModel.CreateEventViewModel;

public class create_event_fragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 234;

    public create_event_fragment() {
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

    private EditText eventName,eventDescription;
    private Spinner interestSpinner;
    private View createEvent, addPhoto;
    private Button startDate;
    private DatePickerDialog startDateEventDialog;
    private ProgressBar progressBar;
    private ImageView imagePlaceholder;
    private CreateEventViewModel createEventViewModel;
    private MutableLiveData<Uri> filePathLiveData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event_fragment, container, false);

        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);

        eventName = view.findViewById(R.id.eventName);
        eventDescription = view.findViewById(R.id.EventDescription);
        interestSpinner = view.findViewById(R.id.interestSelection);
        createEvent = view.findViewById(R.id.createEvent);
        addPhoto = view.findViewById(R.id.uploadPhotoIcon);
        startDate = view.findViewById(R.id.eventStartDate);
        progressBar = view.findViewById(R.id.progressBar3);
        imagePlaceholder = view.findViewById(R.id.uploadPhotoImage);

        initDatePicker();
        startDate.setOnClickListener(v -> startDateEventDialog.show());
        startDate.setText(getTodaysDate());

        ArrayAdapter<String> interestAdaptor = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.InterestsList));
        interestAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interestSpinner.setAdapter(interestAdaptor);

        //Images
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        createEvent.setOnClickListener(v -> {
            newEvent();
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
            filePathLiveData = new MutableLiveData<>(data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathLiveData.getValue());
                imagePlaceholder.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                filePathLiveData.setValue(null);
                Toast.makeText(getActivity(), "Can't load image! Try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }


    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String dateBuffer = "";
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Log.d("TAG", calendar.toString());
                Log.d("TAG", calendar.getTime().toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateBuffer = dateFormat.format(calendar.getTime());
                Log.d("TAG", dateBuffer);
                startDate.setText(dateBuffer);
            }
        };
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        startDateEventDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    private void newEvent() {

        String eventNameString = eventName.getText().toString().trim();
        String eventDescriptionString = eventDescription.getText().toString().trim();
        String startEventDateString = startDate.getText().toString().trim();
        String eventInterest;

        if (eventNameString.isEmpty()) {
            eventName.setError("Event name is required!");
            eventName.requestFocus();
            return;
        }

        if (eventDescriptionString.isEmpty()) {
            eventDescription.setError("Event description is required!");
            eventDescription.requestFocus();
            return;
        }

        if (filePathLiveData == null || filePathLiveData.getValue() == null) {
            Toast.makeText(getActivity(), "Can't load image! Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        eventInterest = interestSpinner.getSelectedItem().toString();
        Log.d("CreatingEventFragment", "1");
        progressBar.setVisibility(View.VISIBLE);
        MutableLiveData<User> userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();
        Event event = new Event("null",
                eventNameString,
                eventDescriptionString,
                startEventDateString,
                eventInterest);
        MutableLiveData<Event> eventMutableLiveData = new MutableLiveData<>();
        eventMutableLiveData.setValue(event);

        createEventViewModel.setEvent(eventMutableLiveData, userMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean setEventFinished) {
                if (setEventFinished != null && setEventFinished) {
                    Log.d("TAG2", event.getEventName());
                    String imagePath = event.getEventId() + "/" + event.getEventId() + ".jpg";
                    createEventViewModel.setEventImage(imagePath, filePathLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean setImageFinished) {
                            if (setImageFinished != null && setImageFinished){
                                Log.d("TAG", "Event with id " + event.getEventId() + " has been registered successfully.");
                                Toast.makeText(getActivity(), "Event has been registered successfully!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                //Navigation.findNavController(getView()).navigate(R.id.action_create_event_fragment_to_inside_view_event_fragment);
                            }else{
                                Log.d("TAG", "setting image has failed!");
                                Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Log.d("TAG", "setting event has returned null!");
                    Toast.makeText(getActivity(), "Failed to register! A Try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}