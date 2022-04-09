package github.com.arnaumolins.quokkafe.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;

public class sign_in_fragment extends Fragment {

    public sign_in_fragment() {
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

    private ListView interestsList;
    private EditText username, email, password;
    private Button signinButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar1);
        username = (EditText) view.findViewById(R.id.username);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        signinButton = (Button) view.findViewById(R.id.sign_in_action);
        interestsList = (ListView) view.findViewById(R.id.listView_interests);

        ArrayAdapter<String> interestsAdaptator = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, getResources().getStringArray(R.array.InterestsList));
        interestsList.setAdapter(interestsAdaptator);

        signinButton.setOnClickListener(l -> {
            signInFunction();
        });
        return view;
    }

    public void signInFunction(){
        String usernameString = username.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        String uniqueID = UUID.randomUUID().toString();
        List<String> interestsStrings = new ArrayList<String>();

        for (int i = 0; i < interestsList.getCount(); i++){
            if(interestsList.isItemChecked(i)){
                interestsStrings.add((String) interestsList.getItemAtPosition(i));
            }
        }

        if (usernameString.isEmpty()) {
            username.setError("Username is required!");
            username.requestFocus();
            return;
        }
        if (emailString.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Please provide a valid email!");
            email.requestFocus();
            return;
        }
        if (passwordString.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if (passwordString.length() < 6) {
            password.setError("Password must have at least 6 characters!");
            password.requestFocus();
            return;
        }
        if (interestsStrings.isEmpty()) {
            Toast.makeText(getContext(), "MARK ONE INTEREST MINIMUM", Toast.LENGTH_LONG);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.signUp(usernameString, emailString, passwordString, interestsStrings).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Toast.makeText(getActivity(), "User has been registered successfully!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Navigation.findNavController(getView()).navigate(R.id.action_sign_in_fragment_to_log_in_fragment);
                    signinButton.setEnabled(true);
                } else {
                    Toast.makeText(getActivity(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    signinButton.setEnabled(true);
                }
            }
        });
    }
}