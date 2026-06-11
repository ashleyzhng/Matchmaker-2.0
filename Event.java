import java.time.*;
import java.util.Scanner;

public abstract class Event {
    Scanner sc = new Scanner(System.in);
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration duration;
    private double originalCost;
    private double avgRating;

    private Tag categories;
    private boolean approved;
    private String eventAdminFeedback;
    private boolean booked;


    private User currentUser1;
    private User currentUser2;

    private boolean attended;
    private User[] attendanceHistory;
    private int numPastAttendees;
    public static int maxPast = 100;

    LocalTime todayTime = LocalTime.now();
    LocalDate todayDate = LocalDate.now();

    public abstract double cancelEvent();

    public Event(String title, LocalDate date, LocalTime startTime, LocalTime endTime, double originalCost, int r, int s, int m, int a, int t, int f, int g, int c, int an, int re) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime);
        this.originalCost = originalCost;
        this.categories = new Tag(r, s, m, a, t, f, g, c, an, re);
        this.approved = false;
        this.attended = false;
        this.attendanceHistory = new User[maxPast];
        this.eventAdminFeedback = null;
        this.booked = false;
        this.numPastAttendees = 0;
    }

    public User getCurrentUser1() {
        return currentUser1;
    }

    public void setCurrentUser1(User currentUser1) {
        this.currentUser1 = currentUser1;
    }

    public User getCurrentUser2() {
        return currentUser2;
    }

    public void setCurrentUser2(User currentUser2) {
        this.currentUser2 = currentUser2;
    }

    public Tag getCategories(){
        return categories;
    }

    public void setCategories(Tag categories) {
        this.categories = categories;
    }
    public LocalTime getStartTime(){
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {

        return endTime;
    }

    public void setEndTime(LocalTime endTime) {

        this.endTime = endTime;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getOriginalCost() {
        return originalCost;
    }
    
    public void setOriginalCost(double originalCost) {
        this.originalCost = originalCost;
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getEventAdminFeedback() {
        return eventAdminFeedback;
    }

    public void setEventAdminFeedback(String eventAdminFeedback) {
        this.eventAdminFeedback = eventAdminFeedback;
    }
    
    public boolean getBooked() {
        return booked;
    }

    

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public boolean getAttended() {

        return attended;
    }

    public void setAttended(boolean attended) {

        this.attended = attended;
    }
    public Duration getDuration(){
        return duration;
    }

    public boolean determineDatePassed() {
        int yearthis = date.getYear();
        int monththis = date.getMonthValue();
        int daythis = date.getDayOfMonth();
  
        int yearnow = todayDate.getYear();
        int monthnow = todayDate.getMonthValue();
        int daynow = todayDate.getDayOfMonth();
        
        if (yearthis < yearnow) {
            return true;
        } else if (yearthis > yearnow) {
            return false;
        } else { 
            if (monththis < monthnow) {
                return true;
            } else if (monththis > monthnow) {
                return false;
            } else {
                if (daythis < daynow) {
                    return true;
                } else {
                    return false;
                } 
            }
        }
    
    }

    public Event compareToTitle(Event other) {
        if (this.title.compareToIgnoreCase(other.title) > 0) {
            return this;
        } else if (this.title.compareToIgnoreCase(other.title) < 0) {
            return other;
        } else {
            return this;
        }
    }

    public Event compareToTime(Event other) {
        int yearthis = this.date.getYear();
        int monththis = this.date.getMonthValue();
        int daythis = this.date.getDayOfMonth();
        int hourthis = this.startTime.getHour();
        int minthis = this.startTime.getMinute();
       
        int yearother = other.date.getYear();
        int monthother = other.date.getMonthValue();
        int dayother = other.date.getDayOfMonth();
        int hourother = other.startTime.getHour();
        int minother = other.startTime.getMinute();
        
        if (yearthis < yearother) {
            return this;
        } else if (yearthis > yearother) {
            return other;
        } else { 
            if (monththis < monthother) {
                return this;
            } else if (monththis > monthother) {
                return other;
            } else {
                if (daythis < dayother) {
                    return this;
                } else if (daythis > dayother) {
                    return other;
                } else { 
                    if (hourthis < hourother) {
                        return this;
                    } else if (hourthis > hourother) {
                        return other;
                    } else { 
                        if (minthis < minother) {
                            return this;
                        } else if (minthis > minother) {
                            return other;
                        } else { 
                            return this;
                        }
                    }
                }
            }
        }
    }

    public boolean addPastAttendee(User user)   {
        if (numPastAttendees < maxPast) {
            attendanceHistory[numPastAttendees] = user;
            numPastAttendees++;
            return true;
        } else {
            for (int i = 0; i < numPastAttendees-1; i ++) {
                attendanceHistory[i] = attendanceHistory[i+1];
            }
            attendanceHistory[numPastAttendees-1] = null;

            numPastAttendees --;
        }
        
        return false;
    }

    public void attendEvent(User user) {
        if (determineDatePassed() && getBooked() == true) {
            setAttended(true);
            System.out.println("How would you rate this event out of 5: ");
            int rate = sc.nextInt();
            addPastAttendee(user);
            avgRating = ((numPastAttendees-1)*avgRating+rate)/numPastAttendees;
        } else {
            System.out.println("Event has not occurred yet.");
        }
    }

    public abstract boolean addUser(User user);

    public abstract boolean autoApproveEvent();

    public abstract String approveEvent();

    public abstract double calculateCost();

    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration=" + duration +
                ", originalCost=" + originalCost +
                ", categories=" + getCategories() +
                ", approved=" + approved +
                '}';
    }
}