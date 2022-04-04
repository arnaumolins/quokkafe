package github.com.arnaumolins.quokkafe.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.com.arnaumolins.quokkafe.R;

public class starting_page_fragment extends Fragment {

    private static final String TAG = "StartPageFragment";
    public starting_page_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_starting_page_fragment, container, false);
    }
}