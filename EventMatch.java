import java.time.*;

public abstract class EventMatch extends Match{
    public static final double INTEREST_WEIGHT = 0.60;
    public static final double LOCATION_WEIGHT = 0.40;
    
    private String eventType;
    private LocalTime start;
    private LocalTime end;
    private LocalDate date;
    private Duration duration;
    private int rating;
    private Event event;

    public EventMatch(){
        super();
        eventType = null;
        event = null;
        rating = -1;
        eventType = null;
        start = null;
        end = null;
        date = null;
    }

    //accessors and mutators
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public void setStart(LocalTime x) { start = x; }
    public void setEnd(LocalTime x) { end = x; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate x) { date = x; }
    public void setDuration(Duration x) { duration = x; }
    public Duration getDuration() { return duration; }
    public Event getEvent() { return event; }
    public void setRating(int r) { rating = r; }
    public int getRating() { return rating; }
    public void setEventType(String x){eventType = x;}
    public void setEvent(Event e) {event = e;}
    @Override
    public boolean isExpire(){
        if(date == null || end == null){
            return false;
        }
        return date.isBefore(LocalDate.now()) || (date.isEqual(LocalDate.now()) && end.isBefore(LocalTime.now()));
    }
    
    public abstract boolean isRejection();
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

}