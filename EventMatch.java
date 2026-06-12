import java.time.*;

/*
   File Name: EventMatch.java
   Name: Ashley Zhong
   Class: ICS4U1
   Date: June 2026

   Description:
   The EventMatch class is an abstract subclass of Match. It stores 
   event-related information
   such as date, time, duration, rating, and event details. It also
   defines abstract methods that subclasses must implement to matching alogrithims,
   calculate score, and manage event-based matches.
*/

public abstract class EventMatch extends Match{
    // weight constants used in scoring calculations
    public static final double INTEREST_WEIGHT = 0.60;
    public static final double LOCATION_WEIGHT = 0.40;
    
    //event related attributes
    private String eventType;
    private LocalTime start;
    private LocalTime end;
    private LocalDate date;
    private String eventTitle;
    private Event event;

    /*
    Constructor
    Initalizes an EventMatch object with default values
    */
    public EventMatch(){
        super();
        eventType = null;
        event = null;
        eventType = null;
        start = null;
        end = null;
        date = null;
    }
    /*
    Constructor
    Initalizes an EventMatch object with given values, used for reading in file for User
    */
    public EventMatch(double score,String e, String s, String en, String d, Event eve){
        super(score);
        eventType = e;
        start = LocalTime.parse(s);
        end = LocalTime.parse(en);
        date = LocalDate.parse(d);
        event = eve; 
    }

    //---accessors and mutators---
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public void setStart(LocalTime x) { start = x; }
    public void setEnd(LocalTime x) { end = x; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate x) { date = x; }
    public Event getEvent() { return event; }
    public void setEventType(String x){eventType = x;}
    public void setEvent(Event e) {event = e;}
    public String getEventType(){return eventType;}
    public String getEventTitle(){return eventTitle;}
    public void setEventTitle(String t){eventTitle = t;}
    
    //---abstract methods that subclasses must implement, refer to subclasses for further details---
    public abstract boolean isAcceptance();
    public abstract String toString();
    public abstract void execute(Object[] pool);
    public abstract void storeMatch();
    public abstract void deleteMatch(EventDatabase sys);
    public abstract double determineScore(Event e);
    public abstract int distanceTier(Event e);
    public abstract double countTags(Event e);
    public abstract boolean prelimCheck(User u);
    public abstract boolean checkAgeCompatibility(User u);
    public abstract boolean checkGenderCompatibilty(User u);
    public abstract void completed();

    /*
        Determines whether the event match has expired based on date and time.
        Notes: Overrides isExpire() method in Match
        Parameters: None
        Returns: boolean - true if the event is expired, false otherwise
        Precondition: none
    */
     @Override
    public boolean isExpire(){
        if(date == null || end == null){
            return false;
        }
        return date.isBefore(LocalDate.now()) || (date.isEqual(LocalDate.now()) && end.isBefore(LocalTime.now()));
    }
}