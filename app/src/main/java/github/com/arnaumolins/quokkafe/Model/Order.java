package github.com.arnaumolins.quokkafe.Model;

import java.util.List;

public class Order {
    private String orderId;
    private String tableId;
    private List<Food> foodList;
    private float totalPrice;

    public Order() {}

    public Order(String orderId, String tableId, List<Food> foodList) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.foodList = foodList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
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
}
