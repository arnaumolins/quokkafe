package github.com.arnaumolins.quokkafe.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {
    private String orderId;
    private ArrayList<Food> foodArrayList;
    private ArrayList<Drinks> drinksArrayList;
    private float totalPrice;

    public Order() {}

    public Order(String orderId, ArrayList<Food> foodArrayList, ArrayList<Drinks> drinksArrayList, float totalPrice) {
        this.orderId = orderId;
        this.foodArrayList = foodArrayList;
        this.drinksArrayList = drinksArrayList;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<Food> getFoodArrayList() {
        return foodArrayList;
    }

    public void setFoodArrayList(ArrayList<Food> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }

    public ArrayList<Drinks> getDrinksArrayList() {
        return drinksArrayList;
    }

    public void setDrinksArrayList(ArrayList<Drinks> drinksArrayList) {
        this.drinksArrayList = drinksArrayList;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
