package github.com.arnaumolins.quokkafe.Model;

import android.widget.DatePicker;
import android.widget.TimePicker;

public class Booking {

    private String bookingId;
    private String tableId;
    private String userId;

    private DatePicker startingDate;
    private DatePicker endingDate;
    private TimePicker startingHour;
    private TimePicker endingHour;

    public Booking() {}

    public Booking(String bookingId, String tableId, String userId, DatePicker startingDate, DatePicker endingDate, TimePicker startingHour, TimePicker endingHour) {
        this.bookingId = bookingId;
        this.tableId = tableId;
        this.userId = userId;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.startingHour = startingHour;
        this.endingHour = endingHour;
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

    public DatePicker getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(DatePicker startingDate) {
        this.startingDate = startingDate;
    }

    public DatePicker getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(DatePicker endingDate) {
        this.endingDate = endingDate;
    }

    public TimePicker getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(TimePicker startingHour) {
        this.startingHour = startingHour;
    }

    public TimePicker getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(TimePicker endingHour) {
        this.endingHour = endingHour;
    }
}
