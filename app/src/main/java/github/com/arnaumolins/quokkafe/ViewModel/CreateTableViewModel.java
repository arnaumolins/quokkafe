package github.com.arnaumolins.quokkafe.ViewModel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.Repository.EventRepository;
import github.com.arnaumolins.quokkafe.Repository.ImageRepository;

public class CreateTableViewModel {
    public MutableLiveData<Boolean> setTable(MutableLiveData<Table> table, MutableLiveData<User> user) {
        return TableRepository.getInstance().setTable(table, user);
    }

    public MutableLiveData<Boolean> setTableImage(String imagePath, MutableLiveData<Uri> uriMutableLiveData) {
        return ImageRepository.getInstance().uploadImage(imagePath, uriMutableLiveData);

    }
}
