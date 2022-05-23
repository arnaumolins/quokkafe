package github.com.arnaumolins.quokkafe.Model;

public class Booking {
    private String bookingId;
    private String tableId;
    private String userId;
    private String initDate;
    private String endDate;

    public Booking() {}

    public Booking(String bookingId, String tableId, String userId, String initDate, String endDate) {
        this.bookingId = bookingId;
        this.tableId = tableId;
        this.userId = userId;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
