package github.com.arnaumolins.quokkafe.Model;

public class Table {
    private String tableId;
    private String tableNumber;
    private int numberOfCustomers;
    private Boolean isAvailable;

    public Table() {}

    public Table(String tableNumber, int numberOfCustomers) {
        this.tableNumber = tableNumber;
        this.numberOfCustomers = numberOfCustomers;
        this.isAvailable = true;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public String getImagePath() {
        return getTableId() + "/" + getTableId() + ".jpg";
    }

}
