public class EventDatabase {
    private Event[] approvedEventList;
    private Event[] pendingEventList;
    private int numApprovedEvents;
    private int numPendingEvents;
    public static int maxApprovedEvents = 50;
    public static int maxPendingEvents = 50;

    public EventDatabase() {
        approvedEventList = new Event[maxApprovedEvents];
        pendingEventList = new Event[maxPendingEvents];
        numApprovedEvents = 0;
        numPendingEvents = 0;
    }

    public Event[] getApprovedEventList() {
        return approvedEventList;
    }

    public void setApprovedEventList(Event[] approvedEventList) {
        this.approvedEventList = approvedEventList;
    }

    public Event[] getPendingEventList() {
        return pendingEventList;
    }

    public void setPendingEventList(Event[] pendingEventList) {
        this.pendingEventList = pendingEventList;
    }

    public int getNumApprovedEvents() {
        return numApprovedEvents;
    }

    public void setNumApprovedEvents(int numApprovedEvents) {
        this.numApprovedEvents = numApprovedEvents;
    }

    public int getNumPendingEvents() {
        return numPendingEvents;
    }

    public void setNumPendingEvents(int numPendingEvents) {
        this.numPendingEvents = numPendingEvents;
    }

    public static int getMaxApprovedEvents() {
        return maxApprovedEvents;
    }

    public static void setMaxApprovedEvents(int maxApprovedEvents) {
        EventDatabase.maxApprovedEvents = maxApprovedEvents;
    }

    public static int getMaxPendingEvents() {
        return maxPendingEvents;
    }

    public static void setMaxPendingEvents(int maxPendingEvents) {
        EventDatabase.maxPendingEvents = maxPendingEvents;
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
        Event itemToCompare;
        int blankIndex;
        for (int i = 0; i < numApprovedEvents; i ++) {
            itemToCompare = approvedEventList[i];
            blankIndex = i;
            while (blankIndex > 0 && itemToCompare.compareToTime(approvedEventList[blankIndex - 1]).equals(itemToCompare)) {
                approvedEventList[blankIndex] = approvedEventList[blankIndex - 1];
                blankIndex--;
            }
            approvedEventList[blankIndex] = itemToCompare;
        }
    }

    public void sortEventByAlphaTitle() {
        int maxIndex;
        Event temp;
        for (int upper = numApprovedEvents - 1; upper > 0; upper--) {
            maxIndex = 0;
            for (int i = 0; i < upper; i++) {
                if (approvedEventList[i].compareToTitle(approvedEventList[maxIndex]).equals(approvedEventList[i])) {
                    maxIndex = i;
                }
            }
            temp = approvedEventList[upper];
            approvedEventList[upper] = approvedEventList[maxIndex];
            approvedEventList[maxIndex] = temp;
        }
    }

    public void displayVirtualEvents() {
        sortEventByAlphaTitle();
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i] instanceof Virtual) {
                System.out.println(approvedEventList[i].getTitle());
            }
        }
    }

    public void displayIndoorEvents() {
        sortEventByAlphaTitle();
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i] instanceof Indoor) {
                System.out.println(approvedEventList[i].getTitle());
            }
        }
    }

    public void displayOutdoorEvents() {
        sortEventByAlphaTitle();
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i] instanceof Outdoor) {
                System.out.println(approvedEventList[i].getTitle());
            }
        }
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