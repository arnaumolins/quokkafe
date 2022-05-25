package github.com.arnaumolins.quokkafe.Model;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String eventId;
    private String eventName;
    private String eventDescription;
    private String date;
    private List<String> assistants = new ArrayList<>();
    private String interest;

    public Event() {}

    public Event(String eventId, String eventName, String eventDescription, String date, String interest) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.date = date;
        this.interest = interest;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName(){
        return eventName;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getAssistantsString(){
        StringBuilder totalAssistants = new StringBuilder();
        for (String user : getAssistants()) {
            totalAssistants.append(user).append("\n");
        }
        return totalAssistants.toString();
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getImagePath() {
        return getEventId() + "/" + getEventId() + ".jpg";
    }
}
