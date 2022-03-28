package github.com.arnaumolins.quokkafe.Model;

import java.util.Date;

public class Booking {
    private int bookingId;
    private int tableId;
    private int userId;
    private Date initDate;
    private Date endDate;

    public Booking() {}

    public Booking(int bookingId, int tableId, int userId, Date initDate, Date endDate) {
        this.bookingId = bookingId;
        this.tableId = tableId;
        this.userId = userId;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
