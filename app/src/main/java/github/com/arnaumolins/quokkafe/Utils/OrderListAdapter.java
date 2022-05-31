package github.com.arnaumolins.quokkafe.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import github.com.arnaumolins.quokkafe.Model.Order;
import github.com.arnaumolins.quokkafe.R;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{

    public static final String TAG = "OrderListAdapter";
    private ArrayList<Order> orders;
    private final Context context;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.itemView.setTag(order);

        holder.name.setText(order.getTableName());

        StringBuilder periodS = new StringBuilder(order.getStartingHour()).append(":").append(order.getStartingMinute()).append(" - ").append(order.getEndingHour()).append(":").append(order.getEndingMinute());
        holder.date.setText(periodS);

        StringBuilder priceS = new StringBuilder("Price: ").append(order.getTotalPrice()).append(" kr.");
        holder.price.setText(priceS);

        holder.itemView.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(this.action.navigate(order.getOrderId()));
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, date;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_name);
            date = itemView.findViewById(R.id.order_date);
            price = itemView.findViewById(R.id.order_price);
        }
    }
}
