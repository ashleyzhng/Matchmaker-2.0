/*
   File Name: Virtual.java
   Name: Kathy Wu
   Class: ICS4U1
   Date: June 2026

   Description:
   The Virtual class is a subclass of Event that inherits the properties of an event, alongside additional fields unique to Virtual type events.
*/
import java.time.*;

public class Virtual extends Event {
    private String platform;
    private int meetingID;
    private int password;
    private String usedDiscountCode;
    
    //constructor
    public Virtual(String title, LocalDate date, LocalTime startTime, LocalTime endTime, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re, User initialUser, String platform, int meetingID, int password) {
        super(title, date, startTime, endTime, originalCost, r, s, m, a, t, f, g, c, an, re, initialUser);
        this.platform = platform;
        this.meetingID = meetingID;
        this.password = password;
        usedDiscountCode = null;
    }

    //accessors and mutators
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }


    public boolean addUser(User user) {
        if (getCurrentUser1() == null) {
            setCurrentUser1(user);
            return true;
        } else if (getCurrentUser2() == null) {
            setCurrentUser2(user);
            setBooked(true);
            return true;
        } else {
            System.out.println("Event is already fully booked.");
            return false;
        }
    }

    public double cancelEvent() {
        setApproved(false);
        setBooked(false);

        return 0;
    }
    public String getUsedDiscountCode() {
        return usedDiscountCode;
    }
    public void setUsedDiscountCode(String usedDiscountCode) {
        this.usedDiscountCode = usedDiscountCode;
    }

    public double calculateCost() {
        if (usedDiscountCode == null) {
            return getOriginalCost();
        }
        switch (usedDiscountCode) {
            case "DISCOUNT10":
                return getOriginalCost() * 0.9; // 10% discount
            case "DISCOUNT20":
                return getOriginalCost() * 0.8; // 20% discount
            case "DISCOUNT30":
                return getOriginalCost() * 0.7; // 30% discount
            default:
                System.out.println("Invalid discount code. No discount applied.");
                return getOriginalCost();
        }       
    }

    public boolean autoApproveEvent() {
        if (platform == null || meetingID == 0 || password == 0) {
            setApproved(false);
            setEventAdminFeedback("Rejected: lack of meeting info");
        return false;
        }
        return true;
    }

    public String toString() {
        return  super.toString() +
                "Platform: " + platform + "\n" +
                "Meeting ID:" + meetingID + "\n" +
                "Password:" + password;
    }
}