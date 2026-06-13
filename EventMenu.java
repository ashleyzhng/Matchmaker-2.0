/*
   File Name: EventMenu.java
   Name: Kathy Wu
   Class: ICS4U1
   Date: June 2026

   Description:
   The EventMenu class contains the UI methods to allow user to interact with the program.
*/
import java.time.*;
import java.util.*;

public class EventMenu {
    Scanner sc = new Scanner(System.in);
    EventDatabase evda = new EventDatabase();
    
    /*
        Parameters: none
        Return values: void
        Preconditions: events in both approved and pending must already exist
        Purpose: ui for the admin specifically to veiw events
    */
    public void adminViewEvents() {
        System.out.println("View Event Titles:");

        System.out.println("1. Approved Events");
        System.out.println("2. Pending Events");
        int choice = sc.nextInt();
        if (choice == 1) {
            evda.printEventByAlphaTitle();
            System.out.print("Want to view details? (1 for yes, 2 for no): ");
            choice = sc.nextInt();

            if (choice == 1) {
                System.out.println("Enter title: ");
                sc.nextLine();
                String title = sc.nextLine();
                Event ev = evda.searchApprovedEventByTitle(title);
                while (ev == null) {
                    System.out.println("Event does not exist. enter correct name: ");
                    title = sc.nextLine();
                    ev = evda.searchApprovedEventByTitle(title);
                } 
                System.out.println(ev);
            } 
        } else  {
            evda.printPendingByAlphaTitle();
            System.out.print("Want to view details? (1 for yes, 2 for no): ");
            choice = sc.nextInt();

            if (choice == 1) {
                System.out.println("Enter title: ");
                sc.nextLine();
                String title = sc.nextLine();
                Event ev = evda.searchPendingEventByTitle(title);
                while (ev == null) {
                    System.out.println("Event does not exist. enter correct name: ");
                    title = sc.nextLine();
                    ev = evda.searchPendingEventByTitle(title);
                } 
                System.out.println(ev);
            } 
        } 
    }

    /*
        Parameters: Event, User
        Return values: none
        Preconditions: event and user must exist
        Purpose: give an event a rating after its over
    */

    public void rateEvent (Event event, User user) {
        System.out.println("Rate the event out of 5: ");
        int rate = sc.nextInt();
        event.attendEvent(user, rate);
        evda.removeApprovedEvent(event);
    }
    
    /*
        Parameters: none
        Return values: void
        Preconditions: event must be of valid type
        Purpose: ui for user to pay for event
    */
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
           
                System.out.println("Payment declined. You are still missing $" + (amount-response) +". Reenter full payment: ");
                amount = sc.nextDouble();
        }
        if (amount > response) {
            System.out.println("Successfully paid! Heres your change: $" + (amount-response));
        } else {
            System.out.println("Successfully paid! Enjoy the event");
        }
    }

    /*
        Parameters: Virtual
        Return values: vdouble
        Preconditions: Event must be of virtual type
        Purpose: alter virtual cost using disount code and returns cost
    */
    public double applyVirtualCost(Virtual event) {
        
        System.out.println("Discount code? (Y/N): ");
        String response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            return event.getOriginalCost();
        } else {
            System.out.println("Enter discount code: ");
            ((Virtual)event).setUsedDiscountCode(sc.nextLine());  
            return event.calculateCost();
        }
    }

    /*
        Parameters: Outdoor
        Return values: void
        Preconditions: Event must be of Outdoor type
        Purpose: Returns altered cost of event as double
    */
    public double applyOutdoorCost(Outdoor event) {
        System.out.println("The expected precipitation of the day is: " + event.getPrecipitation() + "%");
        if (event.getPrecipitation() > 70) {
            System.out.println("You get a 50% discount due to the precipitation > 70% chance!");
        } else if (event.getPrecipitation() < 20) {
            System.out.println("You need to pay 20% extra due to good weather conditions of < 20% precipitation chance!");
        }
        return event.calculateCost(); 
    }

    /*
        Parameters: Indoor
        Return values: double
        Preconditions: Indoor event must eb of Indoor type
        Purpose: 
    */
    public double applyIndoorCost(Indoor event) {
        if (event.getUser3() == null) {
            System.out.println("This is a single date, so your cost is the same as the original.");
        } else {
            System.out.println("This is a double date, so you get a 30% discount!");
        }
        return event.calculateCost();
    }

    /*
        Parameters: none
        Return values: void
        Preconditions: none
        Purpose: admin reviews pending events
    */
    public void adminEventReview() {
        boolean cont = true;
        int index = 0;
        Event event = null;
        boolean result;
        while(cont && evda.getNumPendingEvents() > 0 && index < evda.getNumPendingEvents()) {
            event = evda.getPendingEventList()[index];
            if (event.autoApproveEvent()) {
                System.out.println("Passed system checks. Admin review available.");
                if (event instanceof Virtual) {
                    result = manualApproveVirtualEvent(event);
                } else if (event instanceof Outdoor) {
                    result = manualApproveOutdoorEvent(event);
                } else {
                    result = manualApproveIndoorEvent(event);
                }
                if (!result) {
                    index ++;
                }
            } else {
                System.out.println("Event rejected automatically. No admin override allowed.");
                index ++;
            }
            System.out.println("Want to approve next event (Y/N)? ");   
            String response = sc.nextLine();
            if (response.equalsIgnoreCase("N"))  {
                cont = false;
            }   
        }
        System.out.println("Finished approving events."); 
    }


    /*
        Parameters: Event
        Return values: boolean
        Preconditions: Event must exist and be of type Virtual
        Purpose: admin manually approves each spec of event
    */
     public boolean manualApproveVirtualEvent(Event event) {
  
        if (event != null && event instanceof Virtual) {
            String response;
            System.out.println(("Title: " + event.getTitle()));
            System.out.print("Do you approve this spec? (Y/N): ");
            response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        System.out.println(("Date: " + event.getDate()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        System.out.println(("Start Time: " + event.getStartTime()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        System.out.println("End Time: " + event.getEndTime());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());
  
        }

        System.out.println(("Original Cost: $" + event.getOriginalCost()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        System.out.println(("Categories: " + event.getListCategories()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        System.out.println("Platform: " + ((Virtual) event).getPlatform());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        System.out.println("Meeting ID: " + ((Virtual) event).getMeetingID());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine() + "\n");

        }

        System.out.println("Password: " + ((Virtual) event).getPassword());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        
        if (event.getEventAdminFeedback() == null) {
            
            boolean addapp = evda.addApprovedEvent(event);
            if (addapp == true) {
                event.setApproved(true);
                System.out.println("Event succesfully approved!");
                return true;
            } else {
                System.out.println("Reached max capacity of events. Remove old events.");
                return false;
            }
        } else {
            return false;
        }
  
        } else {
            System.out.println("This event does not exist.");
            return false; 
        }
    }

    /*
        Parameters: Event
        Return values: boolean
        Preconditions: Event must be of tyoe Outdoor and not be null
        Purpose: admin approves of each spec of event 
    */
    public boolean manualApproveOutdoorEvent(Event event) {

        if (event != null && event instanceof Outdoor) {
            String response;
        System.out.println(("Title: " + event.getTitle()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        System.out.println(("Date: " + event.getDate()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Start Time: " + event.getStartTime()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println("End Time: " + event.getEndTime());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Original Cost: $" + event.getOriginalCost()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Categories: " + event.getListCategories()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println("Location: " + ((Outdoor)event).getLocation());
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println("Temperature: " + ((Outdoor)event).getTemperature() + "°C");
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println("Precipitation: " + ((Outdoor)event).getPrecipitation() + "%");
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        if (event.getEventAdminFeedback() == null) {
            
            boolean addapp = evda.addApprovedEvent(event);
            if (addapp == true) {
                event.setApproved(true);
                System.out.println("Event succesfully approved!");
                return true;
            } else {
                System.out.println("Reached max capacity of events. Remove old events.");
                return false;
            }
        } else {
            return false;
        }
  
        } else {
            System.out.println("This event does not exist.");
            return false;
        }
    }

    /*
        Parameters: boolean
        Return values: Event
        Preconditions: event must exist and be of type Indoor
        Purpose: for admin to approve of each spec of event
    */
    public boolean manualApproveIndoorEvent(Event event) {

if (event != null && event instanceof Indoor) {
        String response;
        System.out.println(("Title: " + event.getTitle()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        System.out.println(("Date: " + event.getDate()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Start Time: " + event.getStartTime()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("End Time: " + event.getEndTime()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Original Cost: $" + event.getOriginalCost()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Categories: " + event.getListCategories()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Location: " + ((Indoor) event).getLocation()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Floor Number: " + ((Indoor) event).getFloorNumber()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }
        System.out.println(("Room: " + ((Indoor) event).getRoom()));
        System.out.print("Do you approve this spec? (Y/N): ");
        response = sc.nextLine();
        if (response.equalsIgnoreCase("N")) {
            System.out.println("Why was this event rejected? (Enter reason): ");
            event.setEventAdminFeedback(sc.nextLine());

        }

        if (event.getEventAdminFeedback() == null) {
            
            boolean addapp = evda.addApprovedEvent(event);
            if (addapp == true) {
                event.setApproved(true);
                System.out.println("Event succesfully approved!");
                return true;
            } else {
                System.out.println("Reached max capacity of events. Remove old events.");
                return false;
            }
        } else {
            return false;
        }
  
        } else {
            System.out.println("This event does not exist.");
            return false;
        }
    }


    /*
        Parameters: String
        Return values: void
        Preconditions: event of eventname must exist and be type Outdoor
        Purpose: for the user to change details of currently pending events
    */
    public void changeOutdoorPendingEventDetails(String eventname) {
        Event event = evda.searchPendingEventByTitle(eventname);
        if (event != null && event instanceof Outdoor) {

        System.out.println("Changing event details for outdoor event: " + event.getTitle());
        System.out.println("1. Current title: " + event.getTitle());
        System.out.println("2. Current date: " + event.getDate());
        System.out.println("3. Current start time: " + event.getStartTime());
        System.out.println("4. Current end time: " + event.getEndTime());
        System.out.println("5. Current original cost: $" + event.getOriginalCost());
        System.out.println("6. Current categories: " + event.getListCategories());
        System.out.println("7. Current location: " + ((Outdoor)event).getLocation());
        System.out.println("8. Current temperature: " + ((Outdoor)event).getTemperature() + "°C");
        System.out.println("9. Current precipitation: " + ((Outdoor)event).getPrecipitation() + "%");

        System.out.println("Enter spec you want to change (1-9 or 0 to cancel): ");
        int choice = sc.nextInt();
        sc.nextLine(); 
        switch (choice) {
            case 1:
                System.out.println("Enter new title: ");
                event.setTitle(sc.nextLine());
                System.out.println("Change successful!");
                break;
            case 2:
                System.out.println("Enter new date (YYYY-MM-DD): ");
                String userdate = sc.nextLine();
                LocalDate localuserdate = LocalDate.parse(userdate);                
                event.setDate(localuserdate);
                System.out.println("Change successful!");
                break;
            case 3:
                System.out.println("Enter new start time (HH:MM): ");
                String usertime = sc.nextLine();
                LocalTime localusertime = LocalTime.parse(usertime);
                event.setStartTime(localusertime);
                System.out.println("Change successful!");
                break;
            case 4:
                System.out.println("Enter new end time (HH:MM): ");
                String endusertime = sc.nextLine();
                LocalTime endlocalusertime = LocalTime.parse(endusertime);
                event.setEndTime(endlocalusertime);
                System.out.println("Change successful!");
                break;
            case 5:
                System.out.println("Enter new original cost: ");
                event.setOriginalCost(sc.nextDouble());
                sc.nextLine(); 
                System.out.println("Change successful!");
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
                event.setCategories(newtags);
                System.out.println("Change successful!");
                break;
            case 7:
                System.out.println("Enter new location: ");
                ((Outdoor)event).setLocation(sc.nextLine());
                System.out.println("Change successful!");
                break;
            case 8:
                System.out.println("Enter new temperature (in °C): ");
                ((Outdoor)event).setTemperature(sc.nextInt());
                System.out.println("Change successful!");
                sc.nextLine(); 
                break;
            case 9:
                System.out.println("Enter new precipitation (in %): ");
                ((Outdoor)event).setPrecipitation(sc.nextInt());
                System.out.println("Change successful!");
                sc.nextLine(); 
                break;
            case 0:
                System.out.println("No changes made to the event.");
                break;
            default:
                System.out.println("Invalid choice. No changes made.");
        }
    }
    }

    /*
        Parameters: String
        Return values: void
        Preconditions: event of eventname must exist and be type Virtual
        Purpose: for the user to change details of currently pending Virtual events
    */
    public void changeVirtualPendingEventDetails(String eventname) {
        Event event = evda.searchPendingEventByTitle(eventname);
        if (event != null && event instanceof Virtual) {
        System.out.println("Changing event details for virtual event: " + event.getTitle());
        System.out.println("1. Current title: " + event.getTitle());
        System.out.println("2. Current date: " + event.getDate());
        System.out.println("3. Current start time: " + event.getStartTime());
        System.out.println("4. Current end time: " + event.getEndTime());
        System.out.println("5. Current original cost: $" + event.getOriginalCost());
        System.out.println("6. Current categories: " + event.getListCategories());
        System.out.println("7. Current platform: " + ((Virtual)event).getPlatform());
        System.out.println("8. Current meeting ID: " + ((Virtual)event).getMeetingID());
        System.out.println("9. Current password: " + ((Virtual)event).getPassword());

        System.out.println("Enter spec you want to change (1-9 or 0 to cancel): ");

        int choice = sc.nextInt();
        sc.nextLine(); 
        switch (choice) {
            case 1:
                System.out.println("Enter new title: ");
                event.setTitle(sc.nextLine());
                System.out.println("Change successful!");
                break;
            case 2:
                System.out.println("Enter new date (YYYY-MM-DD): ");
                String userdate = sc.nextLine();
                LocalDate localuserdate = LocalDate.parse(userdate);                
                event.setDate(localuserdate);
                System.out.println("Change successful!");
                break;
            case 3:
                System.out.println("Enter new start time (HH:MM): ");
                String usertime = sc.nextLine();
                LocalTime localusertime = LocalTime.parse(usertime);
                event.setStartTime(localusertime);
                System.out.println("Change successful!");
                break;
            case 4:
                System.out.println("Enter new end time (HH:MM): ");
                String endusertime = sc.nextLine();
                LocalTime endlocalusertime = LocalTime.parse(endusertime);
                event.setEndTime(endlocalusertime);
                System.out.println("Change successful!");
                break;
            case 5:
                System.out.println("Enter new original cost: ");
                event.setOriginalCost(sc.nextDouble());
                sc.nextLine(); 
                System.out.println("Change successful!");
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
                event.setCategories(newtags);
                System.out.println("Change successful!");
                break;
            case 7:
                System.out.println("Enter new platform: ");
                ((Virtual)event).setPlatform(sc.nextLine());
                System.out.println("Change successful!");
                break;
            case 8:
                System.out.println("Enter new meeting ID: ");
                ((Virtual)event).setMeetingID(sc.nextInt());
                System.out.println("Change successful!");
                sc.nextLine(); 
                break;
            case 9:
                System.out.println("Enter new password: ");
                ((Virtual)event).setPassword(sc.nextInt());
                System.out.println("Change successful!");
                sc.nextLine(); 
                break;
            case 0:
                System.out.println("No changes made to the event.");
                break;
            default:
                System.out.println("Invalid choice. No changes made.");
        }
    }
}

    /*
        Parameters: String
        Return values: void
        Preconditions: event of eventname must exist and be type indoor
        Purpose: for the user to change details of currently pending events
    */
    public void changeIndoorPendingEventDetails(String eventname) {
Event event = evda.searchPendingEventByTitle(eventname);
        if (event != null && event instanceof Indoor) {

        System.out.println("Changing event details for indoor event: " + event.getTitle());
        System.out.println("1. Current title: " + event.getTitle());
        System.out.println("2. Current date: " + event.getDate());
        System.out.println("3. Current start time: " + event.getStartTime());
        System.out.println("4. Current end time:" + event.getEndTime());
        System.out.println("5. Current original cost: $" + event.getOriginalCost());
        System.out.println("6. Current categories: " + event.getListCategories());
        System.out.println("7. Current location: " + ((Indoor) event).getLocation());
        System.out.println("8. Current floor number: " + ((Indoor) event).getFloorNumber());
        System.out.println("9. Current room: " + ((Indoor) event).getRoom());

        System.out.println("Enter spec you want to change (1-9 or 0 to cancel): ");
        int choice = sc.nextInt();
        sc.nextLine(); 
        switch (choice) {
             case 1:
                System.out.println("Enter new title: ");
                event.setTitle(sc.nextLine());
                System.out.println("Change successful!");
                break;
            case 2:
                System.out.println("Enter new date (YYYY-MM-DD): ");
                String userdate = sc.nextLine();
                LocalDate localuserdate = LocalDate.parse(userdate);                
                event.setDate(localuserdate);
                System.out.println("Change successful!");
                break;
            case 3:
                System.out.println("Enter new start time (HH:MM): ");
                String usertime = sc.nextLine();
                LocalTime localusertime = LocalTime.parse(usertime);
                event.setStartTime(localusertime);
                System.out.println("Change successful!");
                break;
            case 4:
                System.out.println("Enter new end time (HH:MM): ");
                String endusertime = sc.nextLine();
                LocalTime endlocalusertime = LocalTime.parse(endusertime);
                event.setEndTime(endlocalusertime);
                System.out.println("Change successful!");
                break;
            case 5:
                System.out.println("Enter new original cost: ");
                event.setOriginalCost(sc.nextDouble());
                sc.nextLine(); 
                System.out.println("Change successful!");
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
                event.setCategories(newtags);
                System.out.println("Change successful!");
                break;
            case 7:
                System.out.println("Enter new location: ");
                ((Indoor) event).setLocation(sc.nextLine());
                System.out.println("Change successful!");
                break;
            case 8:
                System.out.println("Enter new floor number: ");
                ((Indoor) event).setFloorNumber(sc.nextInt());
                System.out.println("Change successful!");
                sc.nextLine(); 
                break;
            case 9:
                System.out.println("Enter new room: ");
                ((Indoor) event).setRoom(sc.nextLine());
                System.out.println("Change successful!");
                break;
            case 0:
                System.out.println("No changes made to the event.");
                break;
            default:
                System.out.println("Invalid choice. No changes made.");

        }

    }
}
    /*
        Parameters: User
        Return values: void
        Preconditions: User must exist
        Purpose: for the user to create a new virtual event
    */
    public void createVirtualEvent(User u) {
        System.out.print("Enter title: ");
        String title = sc.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        LocalDate ld = LocalDate.parse(date);
        System.out.print("Enter starting time (HH:MM): ");
        String startTime = sc.nextLine();
        LocalTime lst = LocalTime.parse(startTime);
        System.out.print("Enter ending time (HH:MM): ");
        String endTime = sc.nextLine();
        LocalTime ldt = LocalTime.parse(endTime);
        System.out.print("Enter base cost $: ");
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
        sc.nextLine();
        System.out.print("Platform: ");
        String platform = sc.nextLine();
        System.out.print("Meeting ID: ");
        int meetingid = sc.nextInt();
        System.out.print("Password: ");
        int password = sc.nextInt();

        Virtual vir = new Virtual(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, u, platform, meetingid, password);
        boolean added = evda.addPendingEvent(vir);

        if (added) {
            System.out.print("Event was successfully added. To be approved by admin.");
        } else {
            System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");
        }
    }

    /*
        Parameters: User
        Return values: void
        Preconditions: User must exist
        Purpose: for the user to create a new outdoor event
    */
    public void createOutdoorEvent(User u) {
        System.out.println("Enter title: ");
        String title = sc.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        LocalDate ld = LocalDate.parse(date);
        System.out.print("Enter starting time (HH:MM): ");
        String startTime = sc.nextLine();
        LocalTime lst = LocalTime.parse(startTime);
        System.out.print("Enter ending time (HH:MM): ");
        String endTime = sc.nextLine();
        LocalTime ldt = LocalTime.parse(endTime);
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
        sc.nextLine();
        System.out.print("Location: (postal code): ");
        String location = sc.nextLine();

        System.out.print("Temperature (celsius): ");
        int temp = sc.nextInt();

        System.out.print("Chance of precipitation %: ");
        int precip = sc.nextInt();
        Outdoor out = new Outdoor(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, u, location, temp, precip);
        boolean added = evda.addPendingEvent(out);

        if (added) {
            System.out.print("Event was successfully added. To be approved by admin.");
        } else {
            System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");
        }
    }

    /*
        Parameters: User
        Return values: void
        Preconditions: User must exist
        Purpose: for the user to create a new indoor event
    */
    public void createIndoorEvent(User u) {
        System.out.println("Enter title: ");
        String title = sc.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        LocalDate ld = LocalDate.parse(date);
        System.out.print("Enter starting time (HH:MM): ");
        String startTime = sc.nextLine();
        LocalTime lst = LocalTime.parse(startTime);
        System.out.print("Enter ending time (HH:MM): ");
        String endTime = sc.nextLine();
        LocalTime ldt = LocalTime.parse(endTime);
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
        sc.nextLine();
        System.out.print("Location (postal code): ");
        String loca = sc.nextLine();
        System.out.print("Floor number: ");
        int floornum = sc.nextInt();
        sc.nextLine();
        System.out.print("Room (name or number): ");
        String room = sc.nextLine();

        Indoor in = new Indoor(title, ld, lst, ldt, originalCost, r, s, m, a, t, f, g, c, an, re, u, loca, floornum, room);

        boolean added = evda.addPendingEvent(in);

        if (added) {
            System.out.print("Event was successfully added. To be approved by admin.");
        } else {
            System.out.print("Reached max capacity for pending events. Wait later to create this event once more have been approved.");
        }
    }
    
    /*
        Parameters: User
        Return values: void
        Preconditions: User must exist
        Purpose: The primary event menu screen.
    */
    public void getEventScreen(User user) {
        System.out.println("=================================");
        System.out.println("EVENT OPTIONS:");
        System.out.println("1. Create Event");
        System.out.println("2. Choose Pre-existing Event");
        System.out.println("3. Alter pending event");
        System.out.println("4. View event history");
        System.out.println("5. View hosted events");
        System.out.println("=================================");
        int choice = sc.nextInt();

        while (choice < 1 && choice > 5) {
            System.out.println("That is not a valid choice");
            System.out.println("1. Create Event");
            System.out.println("2. Choose Pre-existing Event");
            System.out.println("3. Alter pending event");
            System.out.println("4. View event history");
            System.out.println("5. View hosted events");
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
            if (choice == 1) {
                createVirtualEvent(user);
            } else if (choice == 2) {
                createOutdoorEvent(user);
            } else {
                createIndoorEvent(user);
            }
            
        } else if (choice == 2){
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
        else if (choice == 3) {
            sc.nextLine();
            System.out.println("Enter event title: ");
            String title = sc.nextLine();
            Event e = evda.searchPendingEventByTitle(title);
            while (e == null) {
                System.out.println("Event does not exist. Enter valid event title: ");
                title = sc.nextLine();
                e = evda.searchPendingEventByTitle(title);
            }

            if (e instanceof Virtual) {
                changeVirtualPendingEventDetails(title);
            } else if (e instanceof Outdoor) {
                changeOutdoorPendingEventDetails(title);
            } else {
                changeIndoorPendingEventDetails(title);
            }
            
        }
        else if (choice == 4) {
            System.out.println("Your hosted pending events:");
            evda.viewPendingEventByUser(user);
            System.out.println("Your hosted approved events");
            evda.viewApprovedEventByUser(user);
        } else {
            System.out.println("Your Event History:");
            System.out.println("-----------------------------");
            evda.viewEventHistory(user);
        }
    }
}