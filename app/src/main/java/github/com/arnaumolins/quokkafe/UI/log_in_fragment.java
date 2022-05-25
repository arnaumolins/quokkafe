package github.com.arnaumolins.quokkafe.UI;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.ViewModel.AuthViewModel;

public class log_in_fragment extends Fragment {

    public log_in_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText email, password;
    private Button log_in_button;
    private ProgressBar progressBar;
    AuthViewModel authViewModel;

    @Override
    public void onResume(){
        super.onResume();
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appbar_top).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in_fragment, container, false);
        email = (EditText) view.findViewById(R.id.email_log);
        password = (EditText) view.findViewById(R.id.password_log);
        log_in_button = (Button) view.findViewById(R.id.log_in_action);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar2);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        log_in_button.setOnClickListener(l -> {
            log_in_button.setEnabled(false);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            logInFunction();
        });
        return view;
    }

    public void logInFunction(){
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);


        authViewModel.signIn(emailString,passwordString).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    User user = authViewModel.getCurrentUser().getValue();
                    if(user != null){
                        Log.d("TAG", user.userName);
                        Toast.makeText(getActivity(), "Login passed successfully!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        ((TextView)getActivity().findViewById(R.id.header_username)).setText("Logged as : " + user.userName);
                        Navigation.findNavController(getView()).navigate(R.id.action_log_in_fragment_to_order_interface_fragment);
                    }else{
                        Toast.makeText(getActivity(), "Login failed! Check your credentials!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getActivity(), "Login failed! Check your credentials!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                log_in_button.setEnabled(true);
            }
        });
    }
}