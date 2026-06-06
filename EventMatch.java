import java.time.*;

public class EventMatch extends Match{
    public static final double INTEREST_WEIGHT = 0.60;
    public static final double LOCATION_WEIGHT = 0.40;
    
    private String eventType;
    private LocalTime start;
    private LocalTime end;
    private char isAccepted;
    private int rating;
    private User user;
    private Event event;

    public EventMatch(User u, String e){
        super();
        user = u;
        eventType = e;
        event = null;
        rating = -1;
        isAccepted = 'P';
        eventType = null;
        start = null;
        end = null;
    }

    public LocalTime getStart(){
        return start;
    }
    public LocalTime getEnd(){
        return end;
    }

    public void execute(Object[] pool){
        if (pool != null && pool.length >0){
            Event[] ePool = new Event[pool.length];
            for (int i = 0; i < pool.length; i ++){
                ePool[i] = (Event)pool[i];
            }
            Event other = execute(ePool, 0, null, -1);
            start = LocalTime.parse(other.getStartTime());
            double duration = other.getDuration();
            long hours = (long)duration;
            long minutes = Math.round((duration - hours) * 60);
            end = start.plusHours(hours).plusMinutes(minutes);
            event = other;
            storeMatch();
            //NEED to update Event somehow
        }
        
    }

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
    public boolean isExpire(){
        return end.isAfter(LocalTime.now());
    }
    public void storeMatch(){
        user.storeMatch(this);
        event.setUser(user);
    }
    public void deleteMatch(){
        user.deleteMatch(this);
        event.cancelEvent(event);
    }
    
    
    public double determineScore(Event e){
        if (e == null){
            return -1;
        }
        if (eventType.equals("Virtual")){
            return countTags(e);
        }
        
        return distanceTier(e) * LOCATION_WEIGHT +countTags(e) *INTEREST_WEIGHT;
        
    }
    
    public int distanceTier(Event e){
        if (e instanceof Virtual){
            return 0;
        }
        
        String eventLocation = "";
        
        if (e instanceof Indoor) {
            Indoor indoorEvent = (Indoor) e;
            eventLocation = indoorEvent.getLocation();
        } 
        
        if (e instanceof Outdoor) {
            Outdoor outdoorEvent = (Outdoor) e;
            eventLocation = outdoorEvent.getLocation();
        }
        
        return DistanceMatch.calculateDistanceTier(user.getPostalCode(), eventLocation);
    }
    
    public double countTags(Event e){
        if (user.getTag() == null || e.getCategories() == null){
            return -1;
        }
        return user.getTag().compareTo(e.getCategories());
    }
}