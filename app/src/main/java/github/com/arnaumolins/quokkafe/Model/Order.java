package github.com.arnaumolins.quokkafe.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {

    private String orderId, tableName, startingHour, endingHour, startingMinute, endingMinute;
    private ArrayList<Food> foodArrayList;
    private ArrayList<Drinks> drinksArrayList;
    private float totalPrice;

    public Order() {}

    public Order(String orderId, String tableName, String startingHour, String endingHour, String startingMinute, String endingMinute, ArrayList<Food> foodArrayList, ArrayList<Drinks> drinksArrayList, float totalPrice) {
        this.orderId = orderId;
        this.tableName = tableName;
        this.startingHour = startingHour;
        this.endingHour = endingHour;
        this.startingMinute = startingMinute;
        this.endingMinute = endingMinute;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(String startingHour) {
        this.startingHour = startingHour;
    }

    public String getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(String endingHour) {
        this.endingHour = endingHour;
    }

    public String getStartingMinute() {
        return startingMinute;
    }

    public void setStartingMinute(String startingMinute) {
        this.startingMinute = startingMinute;
    }

    public String getEndingMinute() {
        return endingMinute;
    }

    public void setEndingMinute(String endingMinute) {
        this.endingMinute = endingMinute;
    }
}
