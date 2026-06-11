import java.util.*;
import java.time.*;
public class Outdoor extends Event {
    private Scanner sc = new Scanner(System.in);
    private String location; //note: This needs to be the postal code
    private int temperature;
    private int precipitation;

    public Outdoor(String title, LocalDate date, LocalTime startTime, LocalTime endTime, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re, String location, int temperature, int precipitation) {
        super(title, date, startTime, endTime, originalCost, r, s, m, a, t, f, g, c, an, re);
        this.location = location;
        this.temperature = temperature;
        this.precipitation = precipitation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
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
        System.out.println("Changing event details for outdoor event: " + getTitle());
        System.out.println("1. Current title: " + getTitle());
        System.out.println("2. Current date: " + getDate());
        System.out.println("3. Current start time: " + getStartTime());
        System.out.println("4. Current end time: " + getEndTime());
        System.out.println("5. Current original cost: $" + getOriginalCost());
        System.out.println("6. Current categories: " + getCategories());
        System.out.println("7. Current location: " + getLocation());
        System.out.println("8. Current temperature: " + getTemperature() + "°C");
        System.out.println("9. Current precipitation: " + getPrecipitation() + "%");

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
                System.out.println("Enter new start time (HH:MM): ");
                String usertime = sc.nextLine();
                int userhour = Integer.parseInt(usertime.substring(0,2));
                int usermin = Integer.parseInt(usertime.substring(3));
                LocalTime localusertime = LocalTime.of(userhour, usermin);
                setStartTime(localusertime);
                break;
            case 4:
                System.out.println("Enter new end time (HH:MM): ");
                String endusertime = sc.nextLine();
                int enduserhour = Integer.parseInt(endusertime.substring(0,2));
                int endusermin = Integer.parseInt(endusertime.substring(3));
                LocalTime endlocalusertime = LocalTime.of(enduserhour, endusermin);
                setEndTime(endlocalusertime);
                break;
            case 5:
                System.out.println("Enter new original cost: ");
                setOriginalCost(sc.nextDouble());
                sc.nextLine(); 
                break;
            case 6:
                System.out.println("Enter new applicable categories (space-separated, lowercase): ");
                String usertags = sc.nextLine();
                String[] taglist = usertags.split(" ");
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
                Tag newtags = new Tag(r, s, m, a, t, f, g, c, an, re);
                setCategories(newtags);
                break;
            case 7:
                System.out.println("Enter new location: ");
                setLocation(sc.nextLine());
                break;
            case 8:
                System.out.println("Enter new temperature (in °C): ");
                setTemperature(sc.nextInt());
                sc.nextLine(); 
                break;
            case 9:
                System.out.println("Enter new precipitation (in %): ");
                setPrecipitation(sc.nextInt());
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
        System.out.println("End Time: " + getEndTime());
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
        System.out.println("Location: " + getLocation());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }
        System.out.println("Temperature: " + getTemperature() + "°C");
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }
        System.out.println("Precipitation: " + getPrecipitation() + "%");
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            setEventAdminFeedback(sc.nextLine());
            return getEventAdminFeedback();
        }

        setEventAdminFeedback("Outdoor event approved");
        setApproved(true);
        return getEventAdminFeedback();
    }

    public double calculateCost() {
        System.out.println("What is the change in precipitation compared to the original spec? (Enter percentage change, e.g., 20 for 20% increase, -10 for 10% decrease): ");
        int precipitationChange = sc.nextInt();
        sc.nextLine();
        setPrecipitation(getPrecipitation() + precipitationChange);
        if (precipitationChange >= 50) {
            return getOriginalCost() * 0.5; 
        } else if (precipitationChange <= -50) {
            return getOriginalCost() * 1.5; 
        } else {
            return getOriginalCost(); 
        }
    }

    public String toString() {
        return "Outdoor Details:" +
                "Location:'" + location + '\'' +
                ", Temperature:" + temperature +
                ", Precipitation:" + precipitation +
                "} " + super.toString();
    }
}