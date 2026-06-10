import java.time.*;
public class SingleEventMatch extends EventMatch{
    private User[] pair = new User[2];
    private char status;
    
    public SingleEventMatch(User u){
        super();
        pair[0] = u;
        status = 'P';
    }   

    @Override
    public void execute(Object[] pool){
        if (pool != null && pool.length >0){
            Event[] ePool = new Event[pool.length];
            for (int i = 0; i < pool.length; i ++){
                ePool[i] = (Event)pool[i];
            }
            Event event = execute(ePool, 0, null, -1);
            setScore(determineScore(event));
            LocalTime start = event.getStartTime();
            Duration duration = event.getDuration();
            LocalTime end = start.plus(duration);
            LocalDate date = event.getDate();
            setStart(start);
            setEnd(end);
            setDuration(duration);
            setDate(date);
            
            if(event instanceof Virtual){
                setEventType("Virtual");
            }else{
                setEventType("Outdoor");
            }
            pair[1]=event.getCurrentUser1();
            storeMatch();
            event.addUser(pair[0]);
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


        if(curScore > maxScore && prelimCheck(pool[idx].getCurrentUser1())){
            return execute(pool, idx + 1, current, curScore);
        }
        return execute(pool, idx+1, best, maxScore);
    }

    public User getUser(){return pair[0];}
    public User getMatchedUser(){return pair[1];}
    
    public void updateUserDecision(char x){
        status = x;
    }
    public boolean isRejection(){
        return status == 'R';
    }
    public boolean isAcceptance(){
        return status == 'A';
    }
    public void storeMatch(){
        pair[0].storeMatch(this);
        getEvent().addUser(pair[0]);
    }
    public void deleteMatch(EventDatabase sys){
        pair[0].deleteMatch(this);
        pair[1].deleteMatch(this);
        sys.removeApprovedEvent(getEvent());
    }

    public double determineScore(Event e){
        if(e == null || pair[0] == null){
            return 0;
        }
        double tagScore = countTags(e);
        double distanceScore = (distanceTier(e)*1.0 / DistanceMatch.MAX_DISTANCE_TIER) * 100;
        if(e instanceof Outdoor){
            return tagScore * EventMatch.LOCATION_WEIGHT + distanceScore * EventMatch.LOCATION_WEIGHT;
        }else{
            return tagScore;
        }
    }

    public int distanceTier(Event e){
       if (e instanceof Outdoor) {
           Outdoor outdoorEvent = (Outdoor) e;
           String eventLocation = outdoorEvent.getLocation();
           return DistanceMatch.calculateDistanceTier(pair[0].getPostalCode(), eventLocation);
       }
       return 0;
   }
  
   public double countTags(Event e){
       if (pair[0].getTag() == null || e.getCategories() == null){
           return -1;
       }
       return pair[0].getTag().compareTo(e.getCategories());
   }

   @Override
    public boolean prelimCheck(User u){
        return checkGenderCompatibilty(u) && checkAgeCompatibility(u);
    }

    @Override
    public boolean checkGenderCompatibilty(User u){
        return pair[0].getGender() == u.getPreferredGender() && u.getGender() == pair[0].getPreferredGender();
    }
    
    @Override
    public boolean checkAgeCompatibility(User u){
        if (u instanceof Teen){
            return true;
        }
        if (u.getBirthday() == null){
            return false;
        }
        LocalDate now = LocalDate.now();
        Period elapse = Period.between(u.getBirthday(), now);
        int uAge = elapse.getYears();
        elapse = Period.between(pair[0].getBirthday(), now);
        int mainAge = elapse.getYears();
        
        if(u instanceof Adult){
            Adult user = (Adult)u;
            Adult mainUser = (Adult)pair[0];
            return uAge >= mainUser.getPreferredMinAge() && uAge <= mainUser.getPreferredMaxAge() && mainAge >= user.getPreferredMinAge() && mainAge <= user.getPreferredMaxAge();
        }

        Senior user = (Senior)u;
        Senior mainUser = (Senior)pair[0];
        return uAge >= mainUser.getPreferredMinAge() && uAge <= mainUser.getPreferredMaxAge() && mainAge >= user.getPreferredMinAge() && mainAge <= user.getPreferredMaxAge();
        
    }

   @Override
    public String toString() {
        String s = "=======================\n";
        s += "Match Type: " + getEventType() + "\n";
        s += "Event Name: " + getEvent().getTitle() + "\n";
        s += "Date: " + getDate() + "\n";
        s += "Start Time: " + getStart() + "\n";
        s += "End Time: " + getEnd() + "\n";
        s += "Compatibility Score: " + (getScore()) + "%\n";
        s += "-----------------------\n";
    
        s += "User 1: ";
        if (pair[0] != null) {
            s += pair[0].getName() + "\n";
            s += "User 1 Status: " + status + "\n";
        } else {
            s += "None\n";
        }
        
        s += "-----------------------\n";
    
        s += "User 2: ";
        if (pair[1] != null) {
            s += pair[1].getName() + "\n";
        } else {
            s += "Still Searching...\n";
        }
        
        s += "=======================\n";
        
        return s;
    }


}