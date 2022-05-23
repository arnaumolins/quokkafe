package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.ViewModel.ViewEventViewModel;

public class inside_view_event_fragment extends Fragment {

    private TextView eventName, eventInterest, eventDescription, eventUsers;
    private ImageView eventImage;
    private Button willAttend;

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

        View view = inflater.inflate(R.layout.fragment_inside_view_event_fragment, container, false);

        return view;
    }
}