package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;

public class user_interface_fragment extends Fragment {

    private TextView userNamePlaceholder, agePlaceholder, emailPlaceholder;
    ListView userInterests;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_interface_fragment, container, false);

        AuthRepository authRepository = AuthRepository.getAuthRepository();
        User user = AuthRepository.getAuthRepository().getCurrentUser().getValue();

        userNamePlaceholder = (TextView) view.findViewById(R.id.userNamePlaceHolder);
        agePlaceholder = (TextView) view.findViewById(R.id.agePlaceholder);
        emailPlaceholder = (TextView) view.findViewById(R.id.emailPlaceholder);
        userInterests = (ListView) view.findViewById(R.id.interestListPlaceholder);
        signOutButton = (Button) view.findViewById(R.id.sign_out_action);

        userNamePlaceholder.setText(user.userName);

        StringBuilder emailS = new StringBuilder("Email: ").append(user.userEmail);
        emailPlaceholder.setText(emailS);

        StringBuilder ageS = new StringBuilder("User age: ").append(user.userAge);
        agePlaceholder.setText(ageS);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, user.getInterestedIn());
        userInterests.setAdapter(arrayAdapter);

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