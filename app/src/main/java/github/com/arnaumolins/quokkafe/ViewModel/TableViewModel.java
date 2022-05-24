package github.com.arnaumolins.quokkafe.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Table;
import github.com.arnaumolins.quokkafe.Repository.TableRepository;

public class TableViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Table>> getTableMutableLiveData(){
        return TableRepository.getInstance().getAllTables();
    }
}
