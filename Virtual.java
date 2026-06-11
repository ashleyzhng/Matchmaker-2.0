import java.time.*;
import java.util.*;

public class Virtual extends Event {
    private Scanner sc = new Scanner(System.in);
    private String platform;
    private int meetingID;
    private int password;
    private String[] discountCodes = {"DISCOUNT10", "DISCOUNT20", "DISCOUNT30"};

    public Virtual(String title, LocalDate date, LocalTime startTime, LocalTime endTime, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re, String platform, int meetingID, int password) {
        super(title, date, startTime, endTime, originalCost, r, s, m, a, t, f, g, c, an, re);
        this.platform = platform;
        this.meetingID = meetingID;
        this.password = password;
    }

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

    public boolean autoApproveEvent() {
        if (platform == null || meetingID == 0 || password == 0) {
            setApproved(false);
            setEventAdminFeedback("Rejected: lack of meeting info");
        return false;
    }
        return true;
    }

    public String toString() {
        return "Virtual Details:" +
                "Platform:'" + platform + '\'' +
                ", Meeting ID:" + meetingID +
                ", Password:" + password +
                "} " + super.toString();
    }
}