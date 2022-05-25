package github.com.arnaumolins.quokkafe.Utils;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Event;
import github.com.arnaumolins.quokkafe.Model.Order;

public class OrderListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>{

    public static final String TAG = "OrderListAdapter";
    private ArrayList<Order> orders;
    private Context context;
    private OrderItemAction action;

    public OrderListAdapter(Context context, OrderItemAction action) {
        this.context = context;
        this.action = action;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
