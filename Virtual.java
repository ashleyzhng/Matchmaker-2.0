import java.time.*;
import java.util.*;

public class Virtual extends Event {
    private Scanner sc = new Scanner(System.in);
    private String platform;
    private int meetingID;
    private int password;
    private String[] discountCodes = {"DISCOUNT10", "DISCOUNT20", "DISCOUNT30"};

    public Virtual(String title, LocalDate date, LocalTime startTime, double duration, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re, String platform, int meetingID, int password) {
        super(title, date, startTime, duration, originalCost, r, s, m, a, t, f, g, c, an, re);
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



    public void changeEventDetails() {
        System.out.println("Changing event details for virtual event: " + getTitle());
        System.out.println("1. Current title: " + getTitle());
        System.out.println("2. Current date: " + getDate());
        System.out.println("3. Current start time: " + getStartTime());
        System.out.println("4. Current duration: " + getDuration() + " hours");
        System.out.println("5. Current original cost: $" + getOriginalCost());
        System.out.println("6. Current categories: " + getCategories());
        System.out.println("7. Current platform: " + getPlatform());
        System.out.println("8. Current meeting ID: " + getMeetingID());
        System.out.println("9. Current password: " + getPassword());

        System.out.println("Enter spec you want to change (1-9 or 0 to cancel): ");

        int choice = sc.nextInt();
        sc.nextLine(); 
        switch (choice) {
            case 1:
                System.out.println("Enter new title: ");
                setTitle(sc.nextLine());
                break;
            case 2:
                System.out.println("Enter new date (YYYY-MM-DD): ");
                String userdate = sc.nextLine();
                int useryear = Integer.parseInt(userdate.substring(0,4));
                int usermonth = Integer.parseInt(userdate.substring(5,7));
                int userday = Integer.parseInt(userdate.substring(8));
                LocalDate localuserdate = LocalDate.of(useryear, usermonth, userday);
                setDate(localuserdate);
                break;
            case 3:
                String usertime = sc.nextLine();
                int userhour = Integer.parseInt(usertime.substring(0,2));
                int usermin = Integer.parseInt(usertime.substring(3));
                LocalTime localusertime = LocalTime.of(userhour, usermin);
                setStartTime(localusertime);
                break;
            case 4:
                System.out.println("Enter new duration (in hours): ");
                setDuration(sc.nextDouble());
                sc.nextLine(); 
                break;
            case 5:
                System.out.println("Enter new original cost: ");
                setOriginalCost(sc.nextDouble());
                sc.nextLine(); 
                break;
            case 6:
                System.out.println("Enter new categories (comma-separated): ");
                setCategories(new Tag(sc.nextLine().split(",")));
                break;
            case 7:
                System.out.println("Enter new platform: ");
                setPlatform(sc.nextLine());
                break;
            case 8:
                System.out.println("Enter new meeting ID: ");
                setMeetingID(sc.nextInt());
                sc.nextLine(); 
                break;
            case 9:
                System.out.println("Enter new password: ");
                setPassword(sc.nextInt());
                sc.nextLine(); 
                break;
            case 0:
                System.out.println("No changes made to the event.");
                break;
            default:
                System.out.println("Invalid choice. No changes made.");
        }
    }

    public String approveEvent() {
        
        String response;
        System.out.println(("Title: " + getTitle()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        System.out.println(("Date: " + getDate()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        System.out.println(("Start Time: " + getStartTime()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        System.out.println(("Duration: " + getDuration() + " hours"));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        System.out.println(("Original Cost: $" + getOriginalCost()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        System.out.println(("Categories: " + getCategories()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        System.out.println("Platform: " + getPlatform());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        System.out.println("Meeting ID: " + getMeetingID());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        System.out.println("Password: " + getPassword());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }
        
        setEventAdminFeedback("Virtual event approved");
        setApproved(true);
        return getEventAdminFeedback();
    }

    public double calculateCost() {
        System.out.println("Discount code? (Y/N): ");
        String response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            return getOriginalCost();
        } else {
            System.out.println("Enter discount code: ");
            String code = sc.nextLine();     
                switch (code.toUpperCase()) {
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
    }

    public String toString() {
        return "Virtual Details:" +
                "Platform:'" + platform + '\'' +
                ", Meeting ID:" + meetingID +
                ", Password:" + password +
                "} " + super.toString();
    }
}