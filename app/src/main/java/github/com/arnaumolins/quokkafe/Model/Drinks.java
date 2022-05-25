package github.com.arnaumolins.quokkafe.Model;

public class Drinks {
    private String drinkName;
    private float drinkPrice;


    public Drinks(){}

    public Drinks (String drinkName, float drinkPrice){
        this.drinkName = drinkName;
        this.drinkPrice = drinkPrice;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public float getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(float drinkPrice) {
        this.drinkPrice = drinkPrice;
    }
}
