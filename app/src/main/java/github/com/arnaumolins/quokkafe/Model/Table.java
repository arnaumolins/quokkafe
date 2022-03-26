package github.com.arnaumolins.quokkafe.Model;

public class Table {
    public int tableId;
    public int tableNumber;
    public int numberOfCustomers;
    public boolean isAvailable;

    public Table() {}

    public Table(int tableId, int tableNumber, int numberOfCustomers) {
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.numberOfCustomers = numberOfCustomers;
        this.isAvailable = true;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

}
