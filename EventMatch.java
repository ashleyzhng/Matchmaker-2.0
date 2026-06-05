import java.time.*;

public class EventMatch extends Match{
    public static final double INTEREST_WEIGHT = 0.70;
    public static final double LOCATION_WEIGHT = 0.30;
    
    private String eventType;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private char isAccepted;
    private int rating;
    private User user;
    private Event event;

    public EventMatch(User u){
        super();
        user = u;
        event = null;
        rating = -1;
        isAccepted = 'P';
        eventType = null;
        start = null;
        end = null;
    }

    public ZonedDateTime getStart(){
        return start;
    }
    public ZonedDateTime getEnd(){
        return end;
    }

    public void execute(Object[] pool){}

    public Event execute(Event[] pool, int idx, Event best, double maxScore){
        if (pool == null || pool.length == 0){
            return null;
        }
        if(idx ==pool.length){
            return best;
        }
        Event current = pool[idx];
        double curScore = determineScore(current);

        if(curScore > maxScore){
            return execute(pool, idx + 1, current, curScore);
        }
        return execute(pool, idx+1, best, maxScore);

    }

    public boolean isRejection(){
        return isAccepted == 'R';
    }
    public boolean isAcceptance(){
        return isAccepted == 'A';
    }
    public boolean isExpire(ZonedDateTime current){
        return end.isAfter(current);
    }
    public void storeMatch(){
        user.storeMatch(this);
        event.setUser(user);
    }
    public void deleteMatch(){
        user.deleteMatch(this);
        event.cancelEvent(event);
    }
    
    //TO BE DONE.
    public double determineScore(Event e){
        //IMPLEMENTATION TO BE DONE
        return -1.0;
    }
    //TO BE DONE.
    public int distanceTier(Event e){
        return -1;
    }
    //TO BE DONE.
    public int countTags(Event e){
        return -1;
    }
}