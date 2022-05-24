package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.ViewModel.ViewTableViewModel;
/***
public class inside_view_table_fragment extends Fragment {
    MutableLiveData<Table> tableLiveData;
    LiveData<String> tableIdLiveData;
    private TextView tableNumber, tableAvailability, tableCostumers;

    ViewTableViewModel viewTableViewModel;
    MutableLiveData<User> userMutableLiveData;
    MutableLiveData<Boolean> usersBooksTable;

    public inside_view_table_fragment() {
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
        inside_view_table_fragmentArgs args = inside_view_table_fragmentArgs.fromBundle(getArguments());
        tableIdLiveData = new MutableLiveData<>(args.getItemId());
        View view = inflater.inflate(R.layout.fragment_inside_view_table_fragment, container, false);

        viewTableViewModel = new ViewModelProvider(this).get(ViewTableViewModel.class);

        userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();

        usersBooksTable = new MutableLiveData<>();
        tableLiveData = new MutableLiveData<>();

        usersBooksTable.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean books) {
                if (books) {

                } else {

                }
            }
        });

        return view;
    }
}***/