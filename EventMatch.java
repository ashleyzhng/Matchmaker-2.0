import java.time.*;

public class EventMatch extends Match{
    public static final double INTEREST_WEIGHT = 0.60;
    public static final double LOCATION_WEIGHT = 0.40;
    
    private String eventType;
    private LocalTime start;
    private LocalTime end;
    private LocalDate date;
    private Duration duration;
    private char isAccepted;
    private int rating;
    private User user;
    private Event event;

    public EventMatch(User u){
        super();
        user = u;
        eventType = null;
        event = null;
        rating = -1;
        isAccepted = 'P';
        eventType = null;
        start = null;
        end = null;
        date = null;
    }

    public LocalTime getStart(){
        return start;
    }
    public LocalTime getEnd(){
        return end;
    }
    public LocalDate getDate(){
        return date;
    }
    public Duration getDuration(){
        return duration;
    }

    @Override
    public void execute(Object[] pool){
        if (pool != null && pool.length >0){
            Event[] ePool = new Event[pool.length];
            for (int i = 0; i < pool.length; i ++){
                ePool[i] = (Event)pool[i];
            }
            Event other = execute(ePool, 0, null, -1);
            start = LocalTime.parse(other.getStartTime());
            double dur = other.getDuration();
            duration = Duration.between(start, end);
            long hours = (long)dur;
            long minutes = Math.round((dur - hours) * 60);
            end = start.plusHours(hours).plusMinutes(minutes);
            event = other;
            if(event instanceof Virtual){
                eventType = "Virtual";
            }else if (event instanceof Indoor){
                eventType = "Indoor";
            }else{
                eventType = "Outdoor";
            }
            storeMatch();
            //NEED to update Event somehow
        }
        
    }

    public Event execute(Event[] pool, int idx, Event best, double maxScore){
        if (pool == null || pool.length == 0){
            return null;
        }
        if(idx == pool.length){
            return best;
        }
        Event current = pool[idx];
        double curScore = determineScore(current);

        if(curScore > maxScore){
            return execute(pool, idx + 1, current, curScore);
        }
        return execute(pool, idx+1, best, maxScore);

    }
    public void updateDecision(char decision){
        isAccepted = decision;
    }

    @Override
    public boolean isRejection(){
        return isAccepted == 'R';
    }
    @Override
    public boolean isAcceptance(){
        return isAccepted == 'A';
    }
    @Override
    public boolean isExpire(){
        return date.isBefore(LocalDate.now()) && end.isAfter(LocalTime.now());
    }
    @Override
    public void storeMatch(){
        user.storeMatch(this);
        event.setUser(user);
    }
    @Override
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

    @Override
    public String toString(){
        String s = "=======================\n";
        s += "Match Number: " + this.getMatchNum() + "\n";
        s += "Match Type: Event Match\n";
        s += "Date: " + date + "\n";
        s += "Start time: " + start + " End time: " + end + "\n";
        s += "Score: " + this.getScore() + "\n";
        s += "-----------------------\n";
    
        // User Info
        s += "User 1: ";
        if (user != null) {
            s += user.getName() + "\n";
        } 
        s += "User Status: " + isAccepted + "\n";
        s += "User Rating: " + rating + "\n";
        s += "-----------------------\n";
    
        // Event Info
        s += "Event: ";
        if (event != null) {
            s +=  event.getTitle() + "\n";
        } else {
            s += "Searching...\n";
        }
        return s;
    }

    public void setRating(int r){
        rating = r;
    }
}