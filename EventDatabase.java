/*
   File Name: EventDatabase.java
   Name: Kathy Wu
   Class: ICS4U1
   Date: June 2026

   Description:
   The EventDatabase class is responsible for managing many events the comprise the entire program.
*/

import java.io.*;
import java.time.*;
import java.util.*;
public class EventDatabase {
    private Event[] approvedEventList;
    private Event[] pendingEventList;
    ArrayList<Event> expiredEventList = new ArrayList<Event>();
    ArrayList<Event> allEventList = new ArrayList<Event>();
    private Event[] eventList;
    private int numApprovedEvents = 0;
    private int numPendingEvents = 0;
    private int numEvents = 0;
    public static int maxApprovedEvents = 50;
    public static int maxPendingEvents = 50;
    AccountsDatabase ad = new AccountsDatabase();

    //constructor
    public EventDatabase() {
        approvedEventList = new Event[maxApprovedEvents];
        pendingEventList = new Event[maxPendingEvents];
        numApprovedEvents = 0;
        numPendingEvents = 0;
        loadAllEvents("event.txt");
    }
    
    /*
        Parameters: String
        Return values: void
        Preconditions: file must exist and contain events
        Purpose: for the system to have a dataabase of events read in from a file
    */
    public void loadAllEvents(String filename) {
        try {
            String type;
            BufferedReader in = new BufferedReader(new FileReader(filename));
            numEvents = Integer.parseInt(in.readLine());
            for (int k = 0; k < numEvents; k ++) {
            type = in.readLine();
            if (type.equals("virtual")) {
                String title = in.readLine();
                String date = in.readLine();
                
                LocalDate ld = LocalDate.parse(date);
                String startTime = in.readLine();
                LocalTime lst = LocalTime.parse(startTime);
                String endTime = in.readLine();
                LocalTime ldt = LocalTime.parse(endTime);
                double originalCost = Double.parseDouble(in.readLine());
                int r = Integer.parseInt(in.readLine());
                int s = Integer.parseInt(in.readLine());
                int m = Integer.parseInt(in.readLine());
                int a = Integer.parseInt(in.readLine());
                int t = Integer.parseInt(in.readLine());
                int f = Integer.parseInt(in.readLine());
                int g = Integer.parseInt(in.readLine());
                int c = Integer.parseInt(in.readLine());
                int an = Integer.parseInt(in.readLine());
                int re = Integer.parseInt(in.readLine());
                String platform = in.readLine();
                int meetingid = Integer.parseInt(in.readLine());
                int password = Integer.parseInt(in.readLine());
                
                String approved = in.readLine();
                String strInitialUser = in.readLine();
                User initialUser;
                if (strInitialUser.equals("null")) {
                    initialUser = null;
                } else {
                    initialUser = ad.searchByUsername(strInitialUser);
                }
                Virtual vir = new Virtual(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, initialUser, platform, meetingid, password);
                boolean added;
                if (approved.equals("true")) {
                    added = addApprovedEvent(vir);
                    if (added) {
                        System.out.print("Approved event was succesfuly added.");

                    } else {
                        System.out.print("Reached max capacity for approved events. Wait later to create this event once admin frees more space.");

                    }

                } else {
                    added = addPendingEvent(vir);
                    if (added) {
                        System.out.print("Event was successfully added. To be approved by admin.");

                    } else {
                        System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");
  
                    } 
                }
            } else if (type.equals("outdoor")) {
                String title = in.readLine();
                String date = in.readLine();
                LocalDate ld = LocalDate.parse(date);
                String startTime = in.readLine();
                LocalTime lst = LocalTime.parse(startTime);
                String endTime = in.readLine();
                LocalTime ldt = LocalTime.parse(endTime);
                double originalCost = Double.parseDouble(in.readLine());

                int r = Integer.parseInt(in.readLine());
                int s = Integer.parseInt(in.readLine());
                int m = Integer.parseInt(in.readLine());
                int a = Integer.parseInt(in.readLine());
                int t = Integer.parseInt(in.readLine());
                int f = Integer.parseInt(in.readLine());
                int g = Integer.parseInt(in.readLine());
                int c = Integer.parseInt(in.readLine());
                int an = Integer.parseInt(in.readLine());
                int re = Integer.parseInt(in.readLine());
                
                String location = in.readLine();
                int temp = Integer.parseInt(in.readLine());
                int precip = Integer.parseInt(in.readLine());
                
                String approved = in.readLine();

                String strInitialUser = in.readLine();
                User initialUser;
                if (strInitialUser.equals("null")) {
                    initialUser = null;
                } else {
                    initialUser = ad.searchByUsername(strInitialUser);
                }

                Outdoor outdoor = new Outdoor(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, initialUser, location, temp, precip);

                boolean added;
                if (approved.equals("true")) {
                    added = addApprovedEvent(outdoor);
                    if (added) {
                        System.out.print("Approved event was succesfuly added.");

                    } else {
                        System.out.print("Reached max capacity for approved events. Wait later to create this event once admin frees more space.");

                    }

                } else {
                    added = addPendingEvent(outdoor);
                    if (added) {
                        System.out.print("Event was successfully added. To be approved by admin.");

                    } else {
                        System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");
  
                    } 
                }
            } else {
                String title = in.readLine();
                String date = in.readLine();
                LocalDate ld = LocalDate.parse(date);
                String startTime = in.readLine();
                LocalTime lst = LocalTime.parse(startTime);
                String endTime = in.readLine();
                LocalTime ldt = LocalTime.parse(endTime);
                double originalCost = Double.parseDouble(in.readLine());
                int r = Integer.parseInt(in.readLine());
                int s = Integer.parseInt(in.readLine());
                int m = Integer.parseInt(in.readLine());
                int a = Integer.parseInt(in.readLine());
                int t = Integer.parseInt(in.readLine());
                int f = Integer.parseInt(in.readLine());
                int g = Integer.parseInt(in.readLine());
                int c = Integer.parseInt(in.readLine());
                int an = Integer.parseInt(in.readLine());
                int re = Integer.parseInt(in.readLine());
                String loca = in.readLine();
                int floornum = Integer.parseInt(in.readLine());
                String room = in.readLine();
                
                String approved = in.readLine();
               
                String strInitialUser = in.readLine();
                User initialUser;
                if (strInitialUser.equals("null")) {
                    initialUser = null;
                } else {
                    initialUser = ad.searchByUsername(strInitialUser);
                }
                Indoor indoor = new Indoor(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, initialUser, loca, floornum, room);
                boolean added;
                if (approved.equals("true")) {
                    added = addApprovedEvent(indoor);
                    if (added) {
                        System.out.print("Approved event was succesfuly added.");

                    } else {
                        System.out.print("Reached max capacity for approved events. Wait later to create this event once admin frees more space.");

                    }

                } else {
                    added = addPendingEvent(indoor);
                    if (added) {
                        System.out.print("Event was successfully added. To be approved by admin.");

                    } else {
                        System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");

                    } 
                }
            }
            
        }
        in.close();
        } catch (IOException iox) {
            System.out.println("Cannot load from file!");

        } 
    }

    /*
        Parameters: String, Event
        Return values: boolean
        Preconditions: file and event must exist
        Purpose: for the user to archive events to a file
    */
    public boolean saveEvent(String filename, Event event) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            if (event instanceof Virtual) {
                out.write(event.getTitle());
                out.newLine();
                out.write(event.getDate() + "");
                out.newLine();
                out.write(event.getStartTime() + "");
                out.newLine();
                out.write(event.getEndTime() + "");
                out.newLine();
                out.write(event.getOriginalCost() + "");
                out.newLine();
                out.write(event.getCategories().getReligion() + "");
                out.newLine();
                out.write(event.getCategories().getSports() + "");
                out.newLine();
                out.write(event.getCategories().getMusic() + "");
                out.newLine();
                out.write(event.getCategories().getArt() + "");
                out.newLine();
                out.write(event.getCategories().getTraveling() + "");
                out.newLine();
                out.write(event.getCategories().getFitness() + "");
                out.newLine();
                out.write(event.getCategories().getGaming() + "");
                out.newLine();
                out.write(event.getCategories().getCooking() + "");
                out.newLine();
                out.write(event.getCategories().getAnimal() + "");
                out.newLine();
                out.write(event.getCategories().getReading() + "");
                out.newLine();
                out.write(((Virtual)event).getPlatform());
                out.newLine();
                out.write(((Virtual)event).getMeetingID() + "");
                out.newLine();
                out.write(((Virtual)event).getPassword() + "");
                out.newLine();
                out.write(event.getApproved() + "");
                out.newLine();
                out.write(event.getCurrentUser1().getUsername() + "");
                out.close();
                return true;
            } else if (event instanceof Outdoor) {
                out.write(event.getTitle());
                out.newLine();
                out.write(event.getDate() + "");
                out.newLine();
                out.write(event.getStartTime() + "");
                out.newLine();
                out.write(event.getEndTime() + "");
                out.newLine();
                out.write(event.getOriginalCost() + "");
                out.newLine();
                out.write(event.getCategories().getReligion() + "");
                out.newLine();
                out.write(event.getCategories().getSports() + "");
                out.newLine();
                out.write(event.getCategories().getMusic() + "");
                out.newLine();
                out.write(event.getCategories().getArt() + "");
                out.newLine();
                out.write(event.getCategories().getTraveling() + "");
                out.newLine();
                out.write(event.getCategories().getFitness() + "");
                out.newLine();
                out.write(event.getCategories().getGaming() + "");
                out.newLine();
                out.write(event.getCategories().getCooking() + "");
                out.newLine();
                out.write(event.getCategories().getAnimal() + "");
                out.newLine();
                out.write(event.getCategories().getReading() + "");
                out.newLine();
                out.write(((Outdoor)event).getLocation());
                out.newLine();
                out.write(((Outdoor)event).getTemperature());
                out.newLine();
                out.write(((Outdoor)event).getPrecipitation());
                out.newLine();
                out.write(event.getApproved() + "");
                out.newLine();
                out.write(event.getCurrentUser1().getUsername() + "");
                out.close();
                return true;
            } else {
                out.write(event.getTitle());
                out.newLine();
                out.write(event.getDate() + "");
                out.newLine();
                out.write(event.getStartTime() + "");
                out.newLine();
                out.write(event.getEndTime() + "");
                out.newLine();
                out.write(event.getOriginalCost() + "");
                out.newLine();
                out.write(event.getCategories().getReligion() + "");
                out.newLine();
                out.write(event.getCategories().getSports() + "");
                out.newLine();
                out.write(event.getCategories().getMusic() + "");
                out.newLine();
                out.write(event.getCategories().getArt() + "");
                out.newLine();
                out.write(event.getCategories().getTraveling() + "");
                out.newLine();
                out.write(event.getCategories().getFitness() + "");
                out.newLine();
                out.write(event.getCategories().getGaming() + "");
                out.newLine();
                out.write(event.getCategories().getCooking() + "");
                out.newLine();
                out.write(event.getCategories().getAnimal() + "");
                out.newLine();
                out.write(event.getCategories().getReading() + "");
                out.newLine();
                out.write(((Indoor)event).getLocation());
                out.newLine();
                out.write(((Indoor)event).getFloorNumber());
                out.newLine();
                out.write(((Indoor)event).getRoom());
                out.newLine();
                out.write(event.getApproved() + "");
                out.newLine();
                out.write(event.getCurrentUser1().getUsername() + "");
                out.close();
                return true;
            }
        } catch (IOException iox) {
            System.out.println("Cannot save to file!");
            return false;
        }

    }

    //accessors and mutators
    public Event[] getApprovedEventList() {
        return approvedEventList;
    }

    public Event[] getEventList() {
        return eventList;
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

    /*
        Parameters: Event
        Return values: boolean
        Preconditions: Event must exist
        Purpose: to add a pending event tot he system of pending events
    */
    public boolean addPendingEvent(Event event) {
        if (numPendingEvents < maxPendingEvents) {
            pendingEventList[numPendingEvents] = event;
            numPendingEvents++;
            allEventList.add(event);
            return true;
        }
        return false; 
    }

     /*
        Parameters: Event
        Return values: boolean
        Preconditions: Event must exist
        Purpose: to add an approved event tot he system of approved events
    */
    public boolean addApprovedEvent(Event event) {
        if (numApprovedEvents < maxApprovedEvents) {
            approvedEventList[numApprovedEvents] = event;
            numApprovedEvents++;
            allEventList.add(event);
            return true;
        }
        return false; 
    }

     /*
        Parameters: Event
        Return values: void
        Preconditions: Event must exist
        Purpose: to remove a pending event from the system of pending events
    */
    public boolean removePendingEvent(Event event) {
        for (int i = 0; i < numPendingEvents; i++) {
            if (pendingEventList[i].equals(event)) {
                
                for (int j = i; j < numPendingEvents - 1; j++) {
                    pendingEventList[j] = pendingEventList[j + 1];
                }
                pendingEventList[numPendingEvents - 1] = null; 
                numPendingEvents--;
                allEventList.remove(event);
                expiredEventList.add(event);
                return true;
            }
        }
        return false;
    }


     /*
        Parameters: Event
        Return values: void
        Preconditions: Event must exist
        Purpose: to remove an approved event from the system of pending events
    */
    public boolean removeApprovedEvent(Event event) {
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i].equals(event)) {
                
                for (int j = i; j < numApprovedEvents - 1; j++) {
                    approvedEventList[j] = approvedEventList[j + 1];
                }
                approvedEventList[numApprovedEvents - 1] = null; 
                numApprovedEvents--;
                allEventList.remove(event);
                expiredEventList.add(event);
                return true;
                
            }
        }
        return false;
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: none
        Purpose: sorts approved events by time
    */
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

    /*
        Parameters: none
        Return values: void
        Preconditions: none
        Purpose: prints approved events by alpha order title
    */
    public void printEventByAlphaTitle() {
        sortEventByAlphaTitle();
        for (int i = 0; i < numApprovedEvents; i++) {
            System.out.println(approvedEventList[i].getTitle());
        }
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: none
        Purpose: prints pending events by alpha order title
    */
    public void printPendingByAlphaTitle() {
        sortPendingByAlphaTitle();
        for (int i = 0; i < numPendingEvents; i ++) {
            System.out.println(pendingEventList[i].getTitle());
        }
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: none
        Purpose: prints approved events in order of time soonest to lastest
    */
    public void printEventByTime() {
        sortEventsByTime();
        for (int i = 0; i < numApprovedEvents; i++) {
            System.out.println(approvedEventList[i].getTitle());
        }
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: none
        Purpose: sorts approved events by alpha title
    */
    public void sortEventByAlphaTitle() {
        int maxIndex;
        Event temp;
        for (int upper = numApprovedEvents - 1; upper >= 1; upper--) {
            maxIndex = 0;
            for (int i = 0; i <= upper; i++) {
                if (approvedEventList[i].compareToTitle(approvedEventList[maxIndex]).equals(approvedEventList[i])) {
                    maxIndex = i;
                }
            }
            temp = approvedEventList[upper];
            approvedEventList[upper] = approvedEventList[maxIndex];
            approvedEventList[maxIndex] = temp;
        }
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: there must be 
        Purpose: sorts approved events by alpha title
    */
    public void sortPendingByAlphaTitle() {
        int maxIndex;
        Event temp;
        for (int upper = numPendingEvents - 1; upper >= 1; upper--) {
            maxIndex = 0;
            for (int i = 0; i <= upper; i++) {
                if (pendingEventList[i].compareToTitle(pendingEventList[maxIndex]).equals(pendingEventList[i])) {
                    maxIndex = i;
                }
            }
            temp = pendingEventList[upper];
            pendingEventList[upper] = pendingEventList[maxIndex];
            pendingEventList[maxIndex] = temp;
        }
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: virtual approved events must exist
        Purpose: shows the virtual events that have been sorted alpha order
    */
    public void displayVirtualEvents() {
        sortEventByAlphaTitle();
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i] instanceof Virtual) {
                System.out.println(approvedEventList[i].getTitle());
            }
        }
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: indoor approved events must exist
        Purpose: shows the indoor events that have been sorted alpha order
    */
    public void displayIndoorEvents() {
        sortEventByAlphaTitle();
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i] instanceof Indoor) {
                System.out.println(approvedEventList[i].getTitle());
            }
        }
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: outdoor approved events must exist
        Purpose: shows the outdoor events that have been sorted alpha order
    */
    public void displayOutdoorEvents() {
        sortEventByAlphaTitle();
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i] instanceof Outdoor) {
                System.out.println(approvedEventList[i].getTitle());
            }
        }
    }

    /*
        Parameters: String
        Return values: Event
        Preconditions: title must be string
        Purpose: returns the approved event of the particular title
    */
    public Event searchApprovedEventByTitle(String title) {
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i].getTitle().equalsIgnoreCase(title)) {
                return approvedEventList[i];
            }
        }
        return null; 
    }

    /*
        Parameters: String
        Return values: Event
        Preconditions: title must be string
        Purpose: returns the pending event of the particular title
    */
    public Event searchPendingEventByTitle(String title) {
        sortPendingByAlphaTitle();
        int bottom = 0;
        int top = numPendingEvents - 1;
        boolean found = false;
        int index = -1;
        int middle;
        while (bottom <= top && !found) {
            middle = (bottom + top) / 2;
            if (pendingEventList[middle].getTitle().equalsIgnoreCase(title)) {
                found = true;
                index = middle;
            } else if (pendingEventList[middle].getTitle().compareToIgnoreCase(title) > 0) {
                top = middle - 1;
            } else {
                bottom = middle + 1;
            }
        }
        if (found) {
            return pendingEventList[index];
        } else {
            return null;
        }
        
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: events must exist
        Purpose: sorts all events by title alpha order
    */
    public void sortAllEventsByTitle() {
        int maxIndex;
        Event temp;
        for (int upper = allEventList.size() - 1; upper >= 1; upper--) {
            maxIndex = 0;
            for (int i = 0; i <= upper; i++) {
                if (allEventList.get(i).compareToTitle(allEventList.get(maxIndex)).equals(allEventList.get(i))) {
                    maxIndex = i;
                }
            }
            temp = allEventList.get(upper);
            allEventList.set(upper, allEventList.get(maxIndex));
            allEventList.set(maxIndex, temp);
        }
    }


    /*
        Parameters: String
        Return values: Event
        Preconditions: title must be string
        Purpose: returns the event of the particular title
    */
    public Event searchEventByTitle(String title) {
        sortAllEventsByTitle();
        int bottom = 0;
        int top = allEventList.size() - 1;
        boolean found = false;
        int index = -1;
        int middle;
        while (bottom <= top && !found) {
            middle = (bottom + top) / 2;
            if (allEventList.get(middle).getTitle().equalsIgnoreCase(title)) {
                found = true;
                index = middle;
            } else if (allEventList.get(middle).getTitle().compareToIgnoreCase(title) > 0) {
                top = middle - 1;
            } else {
                bottom = middle + 1;
            }
        }
        if (found) {
        return allEventList.get(index);
        } else {
            return null;
        }
    }
    

    /*
        Parameters: Event
        Return values: boolean
        Preconditions: Event muts be type Event
        Purpose: determines if approved event exists
    */
    public boolean searchForApprovedEvent(Event event) {
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i].equals(event)) {
                return true;
            }
        }
        return false; 
    }

    /*
        Parameters: Event
        Return values: boolean
        Preconditions: Event must be type Event
        Purpose: determines if pending event exists
    */
        public boolean searchForPendingEvent(Event event) {
        for (int i = 0; i < numPendingEvents; i++) {
            if (pendingEventList[i].equals(event)) {
                return true;
            }
        }
        return false; 
    }

    /*
        Parameters: Event
        Return values: voud
        Preconditions: Event muts be type Event
        Purpose: cancels a booked event
    */
    public void cancelBookedEvent(Event event) {
        if (searchForApprovedEvent(event) && event instanceof Indoor) {
            event.setCurrentUser1(null);
            event.setCurrentUser2(null);
            ((Indoor) event).setUser3(null);
            ((Indoor) event).setUser4(null);           
        }
        else {
            event.setCurrentUser1(null);
            event.setCurrentUser2(null);
        }
            event.setBooked(false);
            event.setDate(null);
            System.out.println("Refunded amount: $" + event.cancelEvent());
    }

    /*
        Parameters: User
        Return values: void
        Preconditions: User must be type user
        Purpose: prints the list of events that the user attended previously
    */
    public void viewEventHistory(User u) {
        ArrayList<Event> eh = u.getEventHistory();
        for (int i = 0; i < eh.size(); i ++) {
            System.out.println(eh.get(i).getTitle());
        }
    }

    /*
        Parameters: User
        Return values: void
        Preconditions: User must be type user
        Purpose: prints the list of pending events that the user made
    */
    public void viewPendingEventByUser(User u) {
        for (int i = 0; i < numPendingEvents; i ++) {
            if (pendingEventList[i].getCurrentUser1() != null && pendingEventList[i].getCurrentUser1().equals(u)) {
                System.out.print(pendingEventList[i]);
            }
        }
    }

    /*
        Parameters: User
        Return values: void
        Preconditions: User must be type user
        Purpose: prints the list of approved events made by the user
    */
    public void viewApprovedEventByUser(User u) {
        for (int i = 0; i < numApprovedEvents; i ++) {
            if (approvedEventList[i].getCurrentUser1() != null && approvedEventList[i].getCurrentUser1().equals(u)) {
                System.out.print(approvedEventList[i]);
            }
        }
    }
    
    
    public void SortApprovedByCostThenTitle() {
    boolean sorted = false;

    for (int upperBound = size - 1; upperBound >= 1 && !sorted; upperBound--) {

        sorted = true;

        for (int j = 0; j < upperBound; j++) {

            boolean greater = false;

            // Primary: cost
            if (events[j].getOriginalCost() > events[j + 1].getOriginalCost()) {
                greater = true;
            }

            // Secondary: alphabetical order (if same cost)
            else if (events[j].getOriginalCost() == events[j + 1].getOriginalCost()) {
                if (events[j].getTitle()
                        .compareToIgnoreCase(events[j + 1].getTitle()) > 0) {
                    greater = true;
                }
            }

            if (greater) {
                sorted = false;

                Event temp = events[j];
                events[j] = events[j + 1];
                events[j + 1] = temp;
            }
        }
    }
}
}