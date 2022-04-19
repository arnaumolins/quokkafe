package github.com.arnaumolins.quokkafe.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {
    private String eventId;
    private String eventName;
    private int guestId;
    private String theme;
    private String eventDescription;
    private List<User> assistants;
    private Date date;

    public Event() {}

    public Event(String eventId, String eventName, int guestId, String theme, String eventDescription, Date date) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.guestId = guestId;
        this.theme = theme;
        this.eventDescription = eventDescription;
        assistants = new ArrayList<>();
        this.date = date;
    }

    public String  getEventId() {
        return eventId;
    }

    public void setEventId(String  eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public List<User> getAssistants() {
        return assistants;
    }

    public void setAssistants(List<User> assistants) {
        this.assistants = assistants;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
