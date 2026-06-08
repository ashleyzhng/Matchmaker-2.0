public class EventDatabase {
    private Event[] approvedEventList;
    private Event[] pendingEventList;
    private int numApprovedEvents;
    private int numPendingEvents;
    public int maxApprovedEvents = 50;
    public int maxPendingEvents = 50;

    public EventDatabase() {
        approvedEventList = new Event[maxApprovedEvents];
        pendingEventList = new Event[maxPendingEvents];
        numApprovedEvents = 0;
        numPendingEvents = 0;
    }

    public boolean addPendingEvent(Event event) {
        if (numPendingEvents < maxPendingEvents) {
            pendingEventList[numPendingEvents] = event;
            numPendingEvents++;
            return true;
        }
        return false; 
    }

    public boolean addApprovedEvent(Event event) {
        if (numApprovedEvents < maxApprovedEvents) {
            approvedEventList[numApprovedEvents] = event;
            numApprovedEvents++;
            return true;
        }
        return false; 
    }

    public boolean removePendingEvent(Event event) {
        for (int i = 0; i < numPendingEvents; i++) {
            if (pendingEventList[i].equals(event)) {
                
                for (int j = i; j < numPendingEvents - 1; j++) {
                    pendingEventList[j] = pendingEventList[j + 1];
                }
                pendingEventList[numPendingEvents - 1] = null; 
                numPendingEvents--;
                return true;
            }
        }
        return false;
    }

    public boolean removeApprovedEvent(Event event) {
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i].equals(event)) {
                
                for (int j = i; j < numApprovedEvents - 1; j++) {
                    approvedEventList[j] = approvedEventList[j + 1];
                }
                approvedEventList[numApprovedEvents - 1] = null; 
                numApprovedEvents--;
                return true;
            }
        }
        return false;
    }

    public void sortEventsByTime() {
        
    }

    public void sortEventsByType() {

    }

    public void sortEventByAlphaTitle() {

    }

    public void displayVirtualEvents() {

    }

    public void displayIndoorEvents() {

    }

    public void displayOutdoorEvents() {

    }

    public Event searchApprovedEventByTitle(String title) {
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i].getTitle().equalsIgnoreCase(title)) {
                return approvedEventList[i];
            }
        }
        return null; 
    }

    public Event searchPendingEventByTitle(String title) {
        for (int i = 0; i < numPendingEvents; i++) {
            if (pendingEventList[i].getTitle().equalsIgnoreCase(title)) {
                return pendingEventList[i];
            }
        }
        return null; 
    }

    public boolean bookEvent(Event event, User user1, User user2) {
        
        return true; 
    }
}