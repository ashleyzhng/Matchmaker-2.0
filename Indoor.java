import java.util.*;
public class Indoor extends Event {
    private Scanner sc = new Scanner(System.in);
    private String location; //Note: This needs to be the postal code
    private int floorNumber;
    private String room;
    private User user3;
    private User user4;

    public Indoor(String title, String date, String startTime, double duration, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re, String location, int floorNumber, String room) {
        super(title, date, startTime, duration, originalCost, r, s, m, a, t, f, g, c, an, re);
        this.location = location;
        this.floorNumber = floorNumber;
        this.room = room;
        this.user3 = null;
        this.user4 = null;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
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
            return true;
        } else if (user3 == null) {
            setUser3(user);
            return true;
        } else if (user4 == null) {
            setUser4(user);
            return true;
        } else {
            System.out.println("Event is already fully booked.");
            return false;
        }
    }

    public void changeEventDetails() {
        System.out.println("Changing event details for indoor event: " + getTitle());
        System.out.println("1. Current title: " + getTitle());
        System.out.println("2. Current date: " + getDate());
        System.out.println("3. Current start time: " + getStartTime());
        System.out.println("4. Current duration: " + getDuration() + " hours");
        System.out.println("5. Current original cost: $" + getOriginalCost());
        System.out.println("6. Current categories: " + getCategories());
        System.out.println("7. Current location: " + getLocation());
        System.out.println("8. Current floor number: " + getFloorNumber());
        System.out.println("9. Current room: " + getRoom());

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
                setDate(sc.nextLine());
                break;
            case 3:
                System.out.println("Enter new start time (HH:MM): ");
                setStartTime(sc.nextLine());
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
                System.out.println("Enter new location: ");
                setLocation(sc.nextLine());
                break;
            case 8:
                System.out.println("Enter new floor number: ");
                setFloorNumber(sc.nextInt());
                sc.nextLine(); 
                break;
            case 9:
                System.out.println("Enter new room: ");
                setRoom(sc.nextLine());
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
        System.out.println(("Location: " + getLocation()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }
        System.out.println(("Floor Number: " + getFloorNumber()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }
        System.out.println(("Room: " + getRoom()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        setEventAdminFeedback("Indoor event approved");
        setApproved(true);
        return getEventAdminFeedback();
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