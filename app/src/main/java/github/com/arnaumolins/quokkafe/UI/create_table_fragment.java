package github.com.arnaumolins.quokkafe.UI;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.R;
import github.com.arnaumolins.quokkafe.Repository.AuthRepository;
import github.com.arnaumolins.quokkafe.ViewModel.CreateTableViewModel;

public class create_table_fragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 234;
    private View createButton, addPhoto;
    private EditText tableName, numbCustomers;
    private ProgressBar progressBar;
    private ImageView imagePlaceholder;

    private CreateTableViewModel createTableViewModel;
    private MutableLiveData<Uri> filePathLiveData;

    public create_table_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_table_fragment, container, false);
        createButton = (View) view.findViewById(R.id.createEvent2);
        addPhoto = (View) view.findViewById(R.id.uploadPhotoIcon2);
        tableName = (EditText) view.findViewById(R.id.tableName);
        numbCustomers = (EditText) view.findViewById(R.id.numbCustomers);
        imagePlaceholder = view.findViewById(R.id.uploadPhotoIcon2);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        createButton.setOnClickListener(v -> {
            newTable();
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

    private void newTable() {

        String tableNameString = tableName.getText().toString().trim();
        String numbCustomersString = numbCustomers.getText().toString();

        if (tableNameString.isEmpty()) {
            tableName.setError("Table name is required!");
            tableName.requestFocus();
            return;
        }

        if (numbCustomersString.isEmpty()) {
            numbCustomers.setError("Table description is required!");
            numbCustomers.requestFocus();
            return;
        }

        if (filePathLiveData == null || filePathLiveData.getValue() == null) {
            Toast.makeText(getActivity(), "Can't load image! Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("CreatingEventFragment", "1");
        Integer numberOfCustomers = Integer.parseInt(numbCustomersString);
        progressBar.setVisibility(View.VISIBLE);
        MutableLiveData<User> userMutableLiveData = AuthRepository.getAuthRepository().getCurrentUser();
        Table table = new Table("null",
                tableNameString,
                numberOfCustomers);
        MutableLiveData<Table> tableMutableLiveData = new MutableLiveData<>();
        tableMutableLiveData.setValue(table);

        createTableViewModel.setTable(tableMutableLiveData, userMutableLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean setEventFinished) {
                if (setEventFinished != null && setEventFinished) {
                    Log.d("TAG2", table.getTableNumber());
                    String imagePath = table.getTableId() + "/" + table.getTableId() + ".jpg";
                    createTableViewModel.setTableImage(imagePath, filePathLiveData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean setImageFinished) {
                            if (setImageFinished != null && setImageFinished){
                                Log.d("TAG", "Event with id " + table.getTableId() + " has been registered successfully.");
                                Toast.makeText(getActivity(), "Event has been registered successfully!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                Navigation.findNavController(getView()).navigate(R.id.action_create_event_fragment_to_inside_view_event_fragment);
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