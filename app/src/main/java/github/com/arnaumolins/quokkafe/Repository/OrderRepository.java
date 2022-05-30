package github.com.arnaumolins.quokkafe.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Order;
import github.com.arnaumolins.quokkafe.Model.User;

public class OrderRepository {
    private static final String TAG = "OrdersRepository";
    private static OrderRepository instance;
    private static MutableLiveData<ArrayList<Order>> ordersLiveData;

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
            ordersLiveData = new MutableLiveData<>(null);
        }
        return instance;
    }

    public MutableLiveData<Boolean> setOrder(MutableLiveData<Order> order, MutableLiveData<User> user) {
        MutableLiveData<Boolean> setOrderState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Orders");
        String orderId = dbRef.push().getKey();
        order.getValue().setOrderId(orderId);
        Log.d(TAG, "Set order with id " + orderId);
        dbRef.child(orderId).setValue(order.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Event created with id " + orderId);
                    FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(user.getValue().userId)
                            .child("ownedOrdersIds")
                            .child(orderId)
                            .setValue(orderId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User " + user.getValue().userId + " is owner of " + orderId);
                                        setOrderState.setValue(true);
                                    } else {
                                        Log.d("TAG", "User " + user.getValue().userId + " is not owner of " + orderId);
                                        setOrderState.setValue(false);
                                    }
                                }
                            });
                } else {
                    Log.e(TAG, "Can not create the event!");
                    setOrderState.setValue(false);
                }
            }
        });
        return setOrderState;
    }

    public MutableLiveData<ArrayList<Order>> getAllOrders() {
        Log.i(TAG, "Getting all the orders");
        if (ordersLiveData.getValue() == null) {
            FirebaseDatabase.getInstance().getReference("Orders").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Order> orders = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Order order = snap.getValue(Order.class);
                        if (order != null) {
                            order.setOrderId(snap.getKey());
                            Log.d(TAG, "Child with id " + snap.getKey());
                            Log.d(TAG, order.toString());
                            orders.add(order);
                        } else {
                            Log.e(TAG, "Orders with id " + snap.getKey() + " is not valid");
                        }
                    }
                    ordersLiveData.setValue(orders);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "getting all events is cancelled");
                    Log.e(TAG, error.getMessage());
                    Log.e(TAG, error.getDetails());
                }
            });
        }
        return ordersLiveData;
    }

    public MutableLiveData<Boolean> deleteOrder(String orderId) {
        MutableLiveData<Boolean> delOrder = new MutableLiveData<>();
        FirebaseDatabase.getInstance().getReference("Orders").child(orderId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Log.d(TAG, error.getMessage());
                    Log.d(TAG, error.getDetails());
                }
                Log.d(TAG, "Order deleted");
                delOrder.setValue(true);
            }
        });
        return delOrder;
    }
}
