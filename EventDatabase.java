import java.io.*;
import java.time.*;
public class EventDatabase {
    private Event[] approvedEventList;
    private Event[] pendingEventList;
    private int numApprovedEvents;
    private int numPendingEvents;
    public static int maxApprovedEvents = 50;
    public static int maxPendingEvents = 50;
    EventMenu em = new EventMenu();

    public EventDatabase() {
        approvedEventList = new Event[maxApprovedEvents];
        pendingEventList = new Event[maxPendingEvents];
        numApprovedEvents = 0;
        numPendingEvents = 0;
    }


    public boolean loadEvent(String filename) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String type = in.readLine();
            if (type.equals("virtual")) {
                String title = in.readLine();
                String date = in.readLine();
                LocalDate ld = em.stringToLocalDate(date);
                String startTime = in.readLine();
                LocalTime lst = em.stringToLocalTime(startTime);
                String endTime = in.readLine();
                LocalTime ldt = em.stringToLocalTime(endTime);
                double originalCost = Double.parseDouble(in.readLine());
                String categories = in.readLine();
                String[] taglist = categories.split(" ");
                int r = 0, s = 0, m = 0, a = 0, t = 0, f = 0, g = 0, c = 0, an = 0, re = 0;
                for (int i = 0; i < taglist.length; i ++) {
                    if (taglist[i].equals("religion")) {
                        r = 1;
                    }
                    if (taglist[i].equals("sports")) {
                        s = 1;
                    } 
                    if (taglist[i].equals("music")) {
                        m = 1;
                    } 
                    if (taglist[i].equals("art")) {
                        a = 1;
                    }
                    if (taglist[i].equals("traveling")) {
                        t = 1;
                    } 
                    if (taglist[i].equals("fitness")) {
                        f = 1;
                    } 
                    if (taglist[i].equals("gaming")) {
                        g = 1;
                    } 
                    if (taglist[i].equals("cooking")) {
                        c = 1;
                    } 
                    if (taglist[i].equals("animal")) {
                        an = 1;
                    } 
                    if (taglist[i].equals("reading")) {
                        re = 1;
                    }    
                }
                String platform = in.readLine();
                int meetingid = Integer.parseInt(in.readLine());
                int password = Integer.parseInt(in.readLine());
                Virtual vir = new Virtual(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, platform, meetingid, password);
                String approved = in.readLine();
                in.close();
                boolean added;
                if (approved.equals("true")) {
                    added = addApprovedEvent(vir);
                    if (added) {
                        System.out.print("Approved event was succesfuly added.");
                        return true;
                    } else {
                        System.out.print("Reached max capacity for approved events. Wait later to create this event once admin frees more space.");
                        return false;
                    }

                } else {
                    added = addPendingEvent(vir);
                    if (added) {
                        System.out.print("Event was successfully added. To be approved by admin.");
                        return true;
                    } else {
                        System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");
                        return false;
                    } 
                }
            } else if (type.equals("outdoor")) {
                String title = in.readLine();
                String date = in.readLine();
                LocalDate ld = em.stringToLocalDate(date);
                String startTime = in.readLine();
                LocalTime lst = em.stringToLocalTime(startTime);
                String endTime = in.readLine();
                LocalTime ldt = em.stringToLocalTime(endTime);
                double originalCost = Double.parseDouble(in.readLine());
                String categories = in.readLine();
                String[] taglist = categories.split(" ");
                int r = 0, s = 0, m = 0, a = 0, t = 0, f = 0, g = 0, c = 0, an = 0, re = 0;
                for (int i = 0; i < taglist.length; i ++) {
                    if (taglist[i].equals("religion")) {
                        r = 1;
                    }
                    if (taglist[i].equals("sports")) {
                        s = 1;
                    } 
                    if (taglist[i].equals("music")) {
                        m = 1;
                    } 
                    if (taglist[i].equals("art")) {
                        a = 1;
                    }
                    if (taglist[i].equals("traveling")) {
                        t = 1;
                    } 
                    if (taglist[i].equals("fitness")) {
                        f = 1;
                    } 
                    if (taglist[i].equals("gaming")) {
                        g = 1;
                    } 
                    if (taglist[i].equals("cooking")) {
                        c = 1;
                    } 
                    if (taglist[i].equals("animal")) {
                        an = 1;
                    } 
                    if (taglist[i].equals("reading")) {
                        re = 1;
                    }    
                }
                String location = in.readLine();
                int temp = Integer.parseInt(in.readLine());
                int precip = Integer.parseInt(in.readLine());
                Outdoor outdoor = new Outdoor(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, location, temp, precip);
                String approved = in.readLine();
                boolean added;
                in.close();
                if (approved.equals("true")) {
                    added = addApprovedEvent(outdoor);
                    if (added) {
                        System.out.print("Approved event was succesfuly added.");
                        return true;
                    } else {
                        System.out.print("Reached max capacity for approved events. Wait later to create this event once admin frees more space.");
                        return false;
                    }

                } else {
                    added = addPendingEvent(outdoor);
                    if (added) {
                        System.out.print("Event was successfully added. To be approved by admin.");
                        return true;
                    } else {
                        System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");
                        return false;
                    } 
                }
            } else {
                String title = in.readLine();
                String date = in.readLine();
                LocalDate ld = em.stringToLocalDate(date);
                String startTime = in.readLine();
                LocalTime lst = em.stringToLocalTime(startTime);
                String endTime = in.readLine();
                LocalTime ldt = em.stringToLocalTime(endTime);
                double originalCost = Double.parseDouble(in.readLine());
                String categories = in.readLine();
                String[] taglist = categories.split(" ");
                int r = 0, s = 0, m = 0, a = 0, t = 0, f = 0, g = 0, c = 0, an = 0, re = 0;
                for (int i = 0; i < taglist.length; i ++) {
                    if (taglist[i].equals("religion")) {
                        r = 1;
                    }
                    if (taglist[i].equals("sports")) {
                        s = 1;
                    } 
                    if (taglist[i].equals("music")) {
                        m = 1;
                    } 
                    if (taglist[i].equals("art")) {
                        a = 1;
                    }
                    if (taglist[i].equals("traveling")) {
                        t = 1;
                    } 
                    if (taglist[i].equals("fitness")) {
                        f = 1;
                    } 
                    if (taglist[i].equals("gaming")) {
                        g = 1;
                    } 
                    if (taglist[i].equals("cooking")) {
                        c = 1;
                    } 
                    if (taglist[i].equals("animal")) {
                        an = 1;
                    } 
                    if (taglist[i].equals("reading")) {
                        re = 1;
                    }    
                }
                String loca = in.readLine();
                int floornum = Integer.parseInt(in.readLine());
                String room = in.readLine();
                Indoor indoor = new Indoor(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, loca, floornum, room);
                String approved = in.readLine();
                boolean added;
                in.close();
                if (approved.equals("true")) {
                    added = addApprovedEvent(indoor);
                    if (added) {
                        System.out.print("Approved event was succesfuly added.");
                        return true;
                    } else {
                        System.out.print("Reached max capacity for approved events. Wait later to create this event once admin frees more space.");
                        return false;
                    }

                } else {
                    added = addPendingEvent(indoor);
                    if (added) {
                        System.out.print("Event was successfully added. To be approved by admin.");
                        return true;
                    } else {
                        System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");
                        return false;
                    } 
                }
            }
        } catch (IOException iox) {
            System.out.println("Cannot load from file!");
            return false;
        }
    }

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
                
                if (event.getCategories().getReligion() == 1) {
                    out.write("religion ");
                }
                if (event.getCategories().getSports() == 1) {
                    out.write("sports ");
                }
                if (event.getCategories().getMusic() == 1) {
                    out.write("music ");
                }
                if (event.getCategories().getArt() == 1) {
                    out.write("art ");
                }
                if (event.getCategories().getTraveling() == 1) {
                    out.write("traveling ");
                }
                if (event.getCategories().getFitness() == 1) {
                    out.write("fitness ");
                }
                if (event.getCategories().getGaming() == 1) {
                    out.write("gaming ");
                }  
                if (event.getCategories().getCooking() == 1) {
                    out.write("cooking ");
                }
                if (event.getCategories().getAnimal() == 1) {
                    out.write("animal ");
                }
                if (event.getCategories().getReading()== 1) {
                    out.write("reading");
                }
                out.newLine();
                out.write(((Virtual)event).getPlatform());
                out.write(((Virtual)event).getMeetingID());
                out.write(((Virtual)event).getPassword());
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
                
                if (event.getCategories().getReligion() == 1) {
                    out.write("religion ");
                }
                if (event.getCategories().getSports() == 1) {
                    out.write("sports ");
                }
                if (event.getCategories().getMusic() == 1) {
                    out.write("music ");
                }
                if (event.getCategories().getArt() == 1) {
                    out.write("art ");
                }
                if (event.getCategories().getTraveling() == 1) {
                    out.write("traveling ");
                }
                if (event.getCategories().getFitness() == 1) {
                    out.write("fitness ");
                }
                if (event.getCategories().getGaming() == 1) {
                    out.write("gaming ");
                }  
                if (event.getCategories().getCooking() == 1) {
                    out.write("cooking ");
                }
                if (event.getCategories().getAnimal() == 1) {
                    out.write("animal ");
                }
                if (event.getCategories().getReading()== 1) {
                    out.write("reading");
                }
                out.newLine();
                out.write(((Outdoor)event).getLocation());
                out.write(((Outdoor)event).getTemperature());
                out.write(((Outdoor)event).getPrecipitation());
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
                
                if (event.getCategories().getReligion() == 1) {
                    out.write("religion ");
                }
                if (event.getCategories().getSports() == 1) {
                    out.write("sports ");
                }
                if (event.getCategories().getMusic() == 1) {
                    out.write("music ");
                }
                if (event.getCategories().getArt() == 1) {
                    out.write("art ");
                }
                if (event.getCategories().getTraveling() == 1) {
                    out.write("traveling ");
                }
                if (event.getCategories().getFitness() == 1) {
                    out.write("fitness ");
                }
                if (event.getCategories().getGaming() == 1) {
                    out.write("gaming ");
                }  
                if (event.getCategories().getCooking() == 1) {
                    out.write("cooking ");
                }
                if (event.getCategories().getAnimal() == 1) {
                    out.write("animal ");
                }
                if (event.getCategories().getReading()== 1) {
                    out.write("reading");
                }
                out.newLine();
                out.write(((Indoor)event).getLocation());
                out.write(((Indoor)event).getFloorNumber());
                out.write(((Indoor)event).getRoom());
                out.close();
                return true;
            }
        } catch (IOException iox) {
            System.out.println("Cannot save to file!");
            return false;
        }

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

    public void printEventByAlphaTitle() {
        sortEventByAlphaTitle();
        for (int i = 0; i < numApprovedEvents; i++) {
            System.out.println(approvedEventList[i].getTitle());
        }
    }

    public void printEventByTime() {
        sortEventsByTime();
        for (int i = 0; i < numApprovedEvents; i++) {
            System.out.println(approvedEventList[i].getTitle());
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
        sortEventByAlphaTitle();
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
        return pendingEventList[index];
    }

    public boolean searchForApprovedEvent(Event event) {
        for (int i = 0; i < numApprovedEvents; i++) {
            if (approvedEventList[i].equals(event)) {
                return true;
            }
        }
        return false; 
    }

        public boolean searchForPendingEvent(Event event) {
        for (int i = 0; i < numPendingEvents; i++) {
            if (pendingEventList[i].equals(event)) {
                return true;
            }
        }
        return false; 
    }

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
            System.out.println("Refunded amount: $" + event.calculateCost());
    }
}