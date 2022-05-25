package github.com.arnaumolins.quokkafe.Model;

import java.util.ArrayList;

public class Food {
    private String foodName;
    private float foodPrice;


    public Food() {}

    public Food (String foodName, float foodPrice) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(float foodPrice) {
        this.foodPrice = foodPrice;
    }

}
