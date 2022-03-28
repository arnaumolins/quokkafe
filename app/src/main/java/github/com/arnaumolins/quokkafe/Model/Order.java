package github.com.arnaumolins.quokkafe.Model;

import java.util.List;

public class Order {
    private int orderId;
    private int tableId;
    private List<Food> foodList;
    private float totalPrice;

    public Order() {}

    public Order(int orderId, int tableId, List<Food> foodList) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.foodList = foodList;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public float getTotalPrice() {
        for (Food food: foodList) {
            totalPrice += food.getFoodPrice();
        }
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
