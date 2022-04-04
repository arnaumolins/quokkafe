package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import github.com.arnaumolins.quokkafe.R;

public class starting_page_fragment extends Fragment {

    private static final String TAG = "StartPageFragment";
    public starting_page_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button signinButton;
    Button loginButton;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_starting_page_fragment, container, false);
        signinButton = (Button) view.findViewById(R.id.sign_in_user_button);
        signinButton.setOnClickListener( l -> {
            Navigation.findNavController(getView()).navigate(R.id.action_starting_page_fragment_to_sign_in_fragment);
        });
        loginButton = (Button) view.findViewById(R.id.log_in_user_button);
        loginButton.setOnClickListener( l -> {
            Navigation.findNavController(getView()).navigate(R.id.action_starting_page_fragment_to_log_in_fragment);
        });
        return view;
    }
}