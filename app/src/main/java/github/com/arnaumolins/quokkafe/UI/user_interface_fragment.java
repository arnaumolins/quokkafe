package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;

public class user_interface_fragment extends Fragment {

    private TextView userNamePlaceholder, agePlaceholder, emailPlaceholder;
    private Button signOutButton;

    MutableLiveData<User> userMutableLiveData;

    public user_interface_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).unlockDrawerMenu();
        //TODO Buttons which are visible
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_interface_fragment, container, false);

        AuthRepository authRepository = AuthRepository.getAuthRepository();
        userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();

        userNamePlaceholder = (TextView) view.findViewById(R.id.userNamePlaceHolder);
        agePlaceholder = (TextView) view.findViewById(R.id.agePlaceholder);
        emailPlaceholder = (TextView) view.findViewById(R.id.emailPlaceholder);
        signOutButton = (Button) view.findViewById(R.id.sign_out_action);

        userNamePlaceholder.setText(userMutableLiveData.getValue().userName);
        agePlaceholder.setText(String.valueOf(userMutableLiveData.getValue().userAge));
        emailPlaceholder.setText(userMutableLiveData.getValue().userEmail);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthRepository.getAuthRepository().firebaseSignOut();
                Navigation.findNavController(view).navigate(R.id.action_user_interface_fragment_to_starting_page_fragment);
            }
        });
        return view;
    }
}