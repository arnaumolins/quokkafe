package github.com.arnaumolins.quokkafe.Model;

public class Booking {

    private String bookingId;
    private String tableId;
    private String userId;
    private String tableName;

    private String bookingYear;
    private String bookingMonth;
    private String bookingDay;

    private String startingHour;
    private String startingMinute;

    private String endingHour;
    private String endingMinute;

    public Booking() {}

    public Booking(String bookingId, String tableName, String tableId, String userId,
                   String bookingYear,
                   String bookingMonth,
                   String bookingDay,
                   String startingHour,
                   String startingMinute,
                   String endingHour,
                   String endingMinute) {
        this.bookingId = bookingId;
        this.tableName = tableName;
        this.tableId = tableId;
        this.userId = userId;
        this.bookingYear = bookingYear;
        this.bookingMonth = bookingMonth;
        this.bookingDay = bookingDay;
        this.startingHour = startingHour;
        this.startingMinute = startingMinute;
        this.endingHour = endingHour;
        this.endingMinute = endingMinute;

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

    public String getBookingYear() {
        return bookingYear;
    }

    public void setBookingYear(String bookingYear) {
        this.bookingYear = bookingYear;
    }

    public String getBookingMonth() {
        return bookingMonth;
    }

    public void setBookingMonth(String bookingMonth) {
        this.bookingMonth = bookingMonth;
    }

    public String getBookingDay() {
        return bookingDay;
    }

    public void setBookingDay(String bookingDay) {
        this.bookingDay = bookingDay;
    }

    public String getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(String startingHour) {
        this.startingHour = startingHour;
    }

    public String getStartingMinute() {
        return startingMinute;
    }

    public void setStartingMinute(String startingMinute) {
        this.startingMinute = startingMinute;
    }

    public String getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(String endingHour) {
        this.endingHour = endingHour;
    }

    public String getEndingMinute() {
        return endingMinute;
    }

    public void setEndingMinute(String endingMinute) {
        this.endingMinute = endingMinute;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
