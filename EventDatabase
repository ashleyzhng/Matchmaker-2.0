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
        return false; // Pending event list is full
    }

    public boolean addApprovedEvent(Event event) {
        if (numApprovedEvents < maxApprovedEvents) {
            approvedEventList[numApprovedEvents] = event;
            numApprovedEvents++;
            return true;
        }
        return false; // Approved event list is full
    }

    public boolean removePendingEvent(Event event) {
        for (int i = 0; i < numPendingEvents; i++) {
            if (pendingEventList[i].equals(event)) {
                // Shift remaining events to the left
                for (int j = i; j < numPendingEvents - 1; j++) {
                    pendingEventList[j] = pendingEventList[j + 1];
                }
                pendingEventList[numPendingEvents - 1] = null; // Clear last element
                numPendingEvents--;
                return true;
            }
        }
        return false; // Event not found in pending list
    }

    public boolean removeApprovedEvent(Event event) {
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i].equals(event)) {
                // Shift remaining events to the left
                for (int j = i; j < numApprovedEvents - 1; j++) {
                    approvedEventList[j] = approvedEventList[j + 1];
                }
                approvedEventList[numApprovedEvents - 1] = null; // Clear last element
                numApprovedEvents--;
                return true;
            }
        }
        return false; // Event not found in approved list
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
        return null; // Event not found
    }

    public Event searchPendingEventByTitle(String title) {
        for (int i = 0; i < numPendingEvents; i++) {
            if (pendingEventList[i].getTitle().equalsIgnoreCase(title)) {
                return pendingEventList[i];
            }
        }
        return null; // Event not found
    }

    public boolean bookEvent(Event event, User user1, User user2) {
        // Implementation for booking an event for two users
        return true; // Placeholder return value
    }
}