package github.com.arnaumolins.quokkafe.ViewModel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import github.com.arnaumolins.quokkafe.Model.Order;
import github.com.arnaumolins.quokkafe.Model.User;
import github.com.arnaumolins.quokkafe.Repository.OrderRepository;

public class CreateOrderViewModel {
    public MutableLiveData<Boolean> setOrder(MutableLiveData<Order> order, MutableLiveData<User> user) {
        return OrderRepository.getInstance().setOrder(order, user);
    }
}
