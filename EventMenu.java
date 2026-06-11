import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class EventMenu {
    Scanner sc = new Scanner(System.in);
    

    public void payForEvent(Event event) {
        double amount;
        System.out.println("The event is starting...");
        System.out.println("Please make the following payments: ");
        if (event instanceof Virtual) {
            amount = applyVirtualCost((Virtual) event);
        } else if (event instanceof Outdoor) {
            amount = applyOutdoorCost((Outdoor) event);
        } else {
            amount = applyIndoorCost((Indoor) event);
        }
        System.out.println("Please pay $" + amount);
        double response = sc.nextDouble();
        
        while (response < amount) {
           
                System.out.println("Payment declined. You are still missing $" + (amount-response));
                amount = sc.nextDouble();
        }
        if (amount > response) {
            System.out.println("Successfully paid! Heres your change: $" + (amount-response));
        } else {
            System.out.println("Successfully paid! Enjoy the event");
        }
    }

    public double applyVirtualCost(Virtual event) {
        
        System.out.println("Discount code? (Y/N): ");
        String response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            return event.getOriginalCost();
        } else {
            System.out.println("Enter discount code: ");
            event.setUsedDiscountCode(sc.nextLine());  
            return event.calculateCost();
        }
    }

    public double applyOutdoorCost(Outdoor event) {
        System.out.println("The expected precipitation of the day is: " + event.getPrecipitation() + "%");
        if (event.getPrecipitation() > 70) {
            System.out.println("You get a 50% discount due to the precipitation > 70% chance!");
        } else if (event.getPrecipitation() < 20) {
            System.out.println("You need to pay 20% extra due to good weather conditions of < 20% precipitation chance!");
        }
        return event.calculateCost(); 
    }

    public double applyIndoorCost(Indoor event) {
        if (event.getUser3() == null) {
            System.out.println("This is a single date, so your cost is the same as the original.");
        } else {
            System.out.println("This is a double date, so you get a 30% discount!");
        }
        return event.calculateCost();
    }

    public void adminEventReview(Event event) {
        if (evda.searchForPendingEvent(event)) {
            if (event.autoApproveEvent()) {
                System.out.println("Passed system checks. Admin review available.");
                if (event instanceof Virtual) {
                    manualApproveVirtualEvent(event);
                } else if (event instanceof Outdoor) {
                    manualApproveOutdoorEvent(event);
                } else {
                    manualApproveIndoorEvent(event);
                }
            } else {
                System.out.println("Event rejected automatically. No admin override allowed.");
            }
        } else if (evda.searchForApprovedEvent(event)){ 
            System.out.println("Event had already been approved!");
        } else {
            System.out.println("Event does not exist!");
        }
    }

    //virtual
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

    //outdoor
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

    //indoor
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
        System.out.println(("End Time: " + getEndTime()));
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

    //change pending details things
    public boolean changePendingEventDetails() {
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
                return false;
        }
        return true;
    }

    public boolean changePendingEventDetails() {
        System.out.println("Changing event details for virtual event: " + getTitle());
        System.out.println("1. Current title: " + getTitle());
        System.out.println("2. Current date: " + getDate());
        System.out.println("3. Current start time: " + getStartTime());
        System.out.println("4. Current end time: " + getEndTime());
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
                return false;
        }
        return true;
    }

    public boolean changePendingEventDetails() {
        System.out.println("Changing event details for indoor event: " + getTitle());
        System.out.println("1. Current title: " + getTitle());
        System.out.println("2. Current date: " + getDate());
        System.out.println("3. Current start time: " + getStartTime());
        System.out.println("4. Current end time:" + getEndTime());
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
                return false;
        }
        return true;
    }
    
    public void getEventScreen() {
        System.out.println("EVENT OPTIONS:");
        System.out.println("1. Create Event");
        System.out.println("2. Choose Pre-existing Event");
        int choice = sc.nextInt();

        while (choice != 1 && choice != 2) {
            System.out.println("That is not a valid choice");
            System.out.println("1. Create Event");
            System.out.println("2. Choose Pre-existing Event");
            choice = sc.nextInt();
        }

        if (choice == 1) {
            System.out.println("Which of the following types of events do you wanna create?");
            System.out.println("1. Virtual");
            System.out.println("2. Outdoor");
            System.out.println("3. Indoor");
            choice = sc.nextInt();

            while (choice < 1 || choice > 3){
                System.out.println("That is an invalid choice");
                System.out.println("Which of the following types of events do you wanna create?");
                System.out.println("1. Virtual");
                System.out.println("2. Outdoor");
                System.out.println("3. Indoor");
                choice = sc.nextInt();
            }

            sc.nextLine();
            System.out.println("Enter title: ");
            String title = sc.nextLine();
            System.out.println("Enter starting time (HH:MM): ");
            String startTime = sc.nextLine();
            System.out.println("Enter duration (in hours): ");
            double duration = sc.nextDouble();
            System.out.println("Enter base cost $: ");
            double originalCost = sc.nextDouble();
            System.out.println("For the following categories, enter 0 for N/A, enter 1 for applied: ");
            System.out.print("Religion (0/1): ");
            int r = sc.nextInt();
            System.out.print("Sports (0/1): ");
            int s = sc.nextInt();
            System.out.print("Music (0/1): ");
            int m = sc.nextInt();
            System.out.print("Art (0/1): ");
            int a = sc.nextInt();
            System.out.print("Traveling (0/1): ");
            int t = sc.nextInt();
            System.out.print("Fitness (0/1): ");
            int f = sc.nextInt();
            System.out.print("Gaming (0/1): ");
            int g = sc.nextInt();
            System.out.print("Cooking (0/1): ");
            int c = sc.nextInt();
            System.out.print("Animal (0/1): ");
            int an = sc.nextInt();
            System.out.print("Reading (0/1): ");
            int re = sc.nextInt();
                
            if (choice == 1) {

            } else if (choice == 2) {

            } else {

            }
            
        } else {
            System.out.println("OPTIONS");
            System.out.println("1. Search for an event by title");
            System.out.println("2. Show all events");
            choice = sc.nextInt();
            while (choice != 1 && choice != 2) {
                System.out.println("Invalid choice");
                System.out.println("OPTIONS");
                System.out.println("1. Search for an event by title");
                System.out.println("2. Show all events");
                choice = sc.nextInt();
            }

            if (choice == 1) {
                sc.nextLine();
                System.out.println("Enter event title name (lowercase): ");
                String name = sc.nextLine();
                Event ev = evda.searchApprovedEventByTitle(name);
                while (ev == null) {
                    System.out.print("Event not found. Enter a different name: "); 
                    name = sc.nextLine();
                    ev = evda.searchApprovedEventByTitle(name);
                }
                System.out.println("Event details: ");
                System.out.println(ev);
            } else {
                System.out.println("Sort Event Options:");
                System.out.println("1. Sort by Title");
                System.out.println("2. Sort by Date and Time");
                choice = sc.nextInt();
                while (choice != 1 && choice != 2) {
                    System.out.println("That is not a valid option");
                    System.out.println("Sort Event Options:");
                    System.out.println("1. Sort by Title");
                    System.out.println("2. Sort by Date and Time");
                    choice = sc.nextInt();
                }
                System.out.println("List of all events:");
                if (choice == 1) {
                    evda.printEventByAlphaTitle();
                } else {
                    evda.printEventByTime();
                }
            }
        }
    }
}
