import java.time.*;
public class Indoor extends Event {
    private String location; //Note: This needs to be the postal code
    private int floorNumber;
    private String room;
    private User user3;
    private User user4;

    public double cancelEvent() {
        setApproved(false);
        setBooked(false);

        return getOriginalCost() * 0.5;
    }
    public Indoor(String title, LocalDate date, LocalTime startTime, LocalTime endTime, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re, String location, int floorNumber, String room) {
        super(title, date, startTime, endTime, originalCost, r, s, m, a, t, f, g, c, an, re);
        this.location = location;
        this.floorNumber = floorNumber;
        this.room = room;
        this.user3 = null;
        this.user4 = null;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFloorNumber(){
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber){
        this.floorNumber = floorNumber;
    }

    public String getRoom(){
        return room;
    }

    public void setRoom(String room){
        this.room = room;
    }

    public User getUser3() {
        return user3;
    }

    public void setUser3(User user3) {
        this.user3 = user3;
    }

    public User getUser4() {
        return user4;
    }

    public void setUser4(User user4) {
        this.user4 = user4;
    }

    public boolean addUser(User user) {
        if (getCurrentUser1() == null) {
            setCurrentUser1(user);
            return true;
        } else if (getCurrentUser2() == null) {
            setCurrentUser2(user);
            setBooked(true);
            return true;
        } else if (user3 == null) {
            setUser3(user);
            setBooked(false);
            return true;
        } else if (user4 == null) {
            setUser4(user);
            setBooked(true);
            return true;
        } else {
            System.out.println("Event is already fully booked.");
            return false;
        }
    }
    
    public boolean autoApproveEvent() {
        if (getOriginalCost() > 500) {
            setApproved(false);
            setEventAdminFeedback("Rejected: cost too high in this economy");
            return false;
        }
        return true;
    }


    public double calculateCost() {
        if (user3 != null && user4 != null) {
            return getOriginalCost() * 0.7; 
        } else {
            return getOriginalCost(); 
        }
    }

    public String toString() {
        return "Indoor Details:" +
                "Location:'" + location + '\'' +
                ", Floor Number:" + floorNumber +
                ", Room:'" + room + '\'' +
                "} " + super.toString();
    }
    
}