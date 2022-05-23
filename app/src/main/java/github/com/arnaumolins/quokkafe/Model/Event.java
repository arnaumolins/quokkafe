package github.com.arnaumolins.quokkafe.Model;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventName;
    private String eventId;
    private String interest;
    private String eventDescription;
    private List<String> assistants = new ArrayList<>();
    private String date;

    public Event() {}

    public Event(String eventId, String eventName, String eventDescription, String date, String interest) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.date = date;
        this.interest = interest;
    }

    public String getEventName(){return eventName;}

    public void setEventName(String eventName){this.eventName = eventName;}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public List<String> getAssistants() {
        return assistants;
    }

    public void addAssistant(String assistant) {
        if (!assistants.contains(assistant)) {
            assistants.add(assistant);
        }
    }

    public void removeAssistant(String assistant) {
        assistants.remove(assistant);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImagePath() {
        return getEventId() + "/" + getEventId() + ".jpg";
    }

    public String getAssistansString(){
        StringBuilder totalAssistants = new StringBuilder();
        for (String user : getAssistants()) {
            totalAssistants.append(user).append("\n");
        }
        return totalAssistants.toString();
    }
}
