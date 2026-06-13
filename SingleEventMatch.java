import java.time.*;

/*
   File Name: SingleEventMatch.java
   Name: Ashley Zhong
   Class: ICS4U1
   Date: June 2026

   Description:
   The SingleEventMatch class extends EventMatch and represents a match
   between two users and an event. It is responsible for selecting
   the best event from a pool, calculating compatibility scores, and
   storing match results for both users involved.
*/

public class SingleEventMatch extends EventMatch{
    //store the two users involved in the match
    private User[] pair = new User[2];
    //stores match status (P= Pending, A = Accepted, R = Rejected)
    private char[] status = new char[2];
    //Stores ratings for both users in match
    private int[] rating = new int[2];
    
    /*
    Constructs a SingleEventMatch object given user
    Paramters: u - the user initaiting match
    Returns: none
    Preconditions: u must not be null
     */
    public SingleEventMatch(User u){
        super();
        pair[0] = u;
        status[0]= 'P';
        status[1]= 'P';
    }
    /*
    Constructs a SingleEventMatch object given all attributes, used for reading in file for User
    Paramters: score - the compatibility score of the match
               e - the event type of the match
               s - the start time of the event
               en - the end time of the event
               d - the date of the event
               dur - the duration of the event
               eve - the event object of the match
               u1 - the first user in the match
               u2 - the second user in the match
               stat - the status of the match (P, A, R)
    Returns: none
    Preconditions: u1 must not be null, stat must be 'P', 'A', or 'R'
    */
    public SingleEventMatch(String e, double score, String s, String en, String d, Event eve, User u1, User u2, char stat1, char stat2, int r1, int r2){
        super(e, score,s,en,d,eve);
        pair[0] = u1;
        pair[1] = u2;
        status[0] = stat1;
        status[1] = stat2;
        rating[0]= r1;
        rating[1]= r2;
    }  
    
    /*
    Executes the event matching alogorithm using a pool of events
    Paramters: pool - array of objects representing events
    Returns: None
    Precondition: pool must contain Event objects and not be null in order to actually generate a potential match
    */
    @Override
    public void execute(Object[] pool){
        if (pool != null && pool.length >0){
            Event[] ePool = new Event[pool.length];
            for (int i = 0; i < pool.length; i ++){
                ePool[i] = (Event)pool[i];
            }
            Event event = execute(ePool, 0, null, -1);
            if (event == null){
                pair[0].deleteMatch(this);
            }else{
                // attach the chosen event to this match
                setEvent(event);
                setScore(determineScore(event));
                LocalTime start = event.getStartTime();
                LocalTime end = event.getEndTime();
                LocalDate date = event.getDate();
                setStart(start);
                setEnd(end);
                setDate(date);

                if(event instanceof Virtual){
                    setEventType("Virtual");
                }else{
                    setEventType("Outdoor");
                }
                pair[1]= event.getCurrentUser1();
                if (pair[1] != null){
                    EventMatch old = pair[1].getEventMatch(event.getTitle());
                    if (old instanceof SingleEventMatch){
                        status[1] = ((SingleEventMatch)old).status[1];
                    }else{
                        status[1] = 'P';
                    }
                }
                
                setEventTitle(event.getTitle());
                storeMatch();
                event.addUser(pair[0]);
            }
        }
        
    }

    /*
    Recursively executes the event matching algorithm using a pool of events
    Parameters: pool - array of objects representing events
                idx - current index in the event pool
                best - the best event found so far
                maxScore - the maximum compatibility score found so far
    Returns: The best event found, or null if no valid event is found
    Precondition: pool must contain Event objects and not be null (or else will return null)
     */
    private Event execute(Event[] pool, int idx, Event best, double maxScore){
        if (pool == null || pool.length == 0){
            return null;
        }
        if(idx == pool.length){
            return best;
        }
        Event current = pool[idx];
        double curScore = determineScore(current);


        if(curScore > maxScore && prelimCheck(current.getCurrentUser1())){
            return execute(pool, idx + 1, current, curScore);
        }
        return execute(pool, idx+1, best, maxScore);
    }

    // --- accessors and mutators ---
    public User getUser(){return pair[0];}
    public User getMatchedUser(){return pair[1];}
    public int getRating(User u){
        if (u == pair[0]) {
            return rating[0];
        } else if (u == pair[1]) {
            return rating[1];
        }
        return -1;
    }
    public void setRating(User u, int r){
        if (u == pair[0]) {
            rating[0] = r;
        } else if (u == pair[1]) {
            rating[1] = r;
        }
    }
    public char getStatus(User u){
        if (u == pair[0]) {
            return status[0];
        } else if (u == pair[1]) {
            return status[1];
        }
        return 'X';
    }
    public char getStatus1(){
        return status[0];
    }
    public char getStatus2(){
        return status[1];
    }
    public int getRating1(){
        return rating[0];
    }
    public int getRating2(){
        return rating[1];
    }
    public User getOwner(){
        return pair[0];
    }
    public User getSoulmate(){
        return pair[1];
    }

    public void completed(){
        status[0] = 'C';
        status[1] = 'C';
        pair[0].deleteMatch(this);
        pair[1].deleteMatch(this);
    }
    public boolean isCompleted(){
        return status[0]=='C' && status[1]=='C';
    }
    
    /*
     * Updates the decision for the user in the match
     * @param x the new decision ('P', 'A', or 'R')
     * Precondition: x must be 'P', 'A', or 'R'
     * Returns: None
     */
    public void updateUserDecision(User u, char x){
        if (u == pair[0]) {
            status[0] = x;
            if (x == 'R'){
                pair[0] = null;
                getEvent().setCurrentUser1(null);
            }
        } else if (u == pair[1]) {
            status[1] = x;
            if (x == 'R'){
                pair[1] = null;
                getEvent().setCurrentUser2(null);
            }   
        }
    }
    /*
     * Checks if the match has been accepted
     * Parameters: none
     * Returns: true if the match has been accepted, false otherwise   
     */
    public boolean isAcceptance(){
        return status[0] == 'A' && status[1] == 'A';
    }

    /*
     * Stores the match in the users' profiles
            if another user is involved, also updates the match for the other user (by deleting the old match and storing the new match)
     * Parameters: none
     * Returns: none
     */
    public void storeMatch(){
        pair[0].storeMatch(this);
        if (pair[1] != null){
            pair[1].deleteMatch(getEventTitle());
            pair[1].storeMatch(this);
        }else if (getEvent() != null){
            getEvent().addUser(pair[0]);
        }
    }
    /*
     * Deletes the match from the users' profiles and also the event database
     * Parameters: sys - the event database
     * Returns: none
     */
    public void deleteMatch(EventDatabase sys){
        pair[0].deleteMatch(this);
        pair[1].deleteMatch(this);
        sys.removeApprovedEvent(getEvent());
    }

    /*
     * Determines the compatibility score for a given event
     * Parameters: e - the event to evaluate
     * Returns: the compatibility score as a percentage (0-100)
     */
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

    /*
     * Calculates the distance tier for a given event
     * Parameters: e - the event to evaluate
     * Returns: the distance tier (0-6), the higher the better
     */
    public int distanceTier(Event e){
       if (e instanceof Outdoor) {
           Outdoor outdoorEvent = (Outdoor) e;
           String eventLocation = outdoorEvent.getLocation();
           return DistanceMatch.calculateDistanceTier(pair[0].getPostalCode(), eventLocation);
       }
       return 0;
   }
  
    /*
        * Counts the number of matching tags between the user and the event
        * Parameters: e - the event to evaluate
        * Returns: the number of matching tags, or -1 if either the user or event has no tags
    */
   public double countTags(Event e){
       if (pair[0].getTag() == null || e.getCategories() == null){
           return -1;
       }
       return pair[0].getTag().compareTo(e.getCategories());
   }

   /*
     * Checks if the match is valid based on preliminary criteria
     * Parameters: u - the user to evaluate
     * Returns: true if the match is valid, false otherwise
     */
   @Override
    public boolean prelimCheck(User u){
        if (u == null){
            return true;
        }
        return checkGenderCompatibilty(u) && checkAgeCompatibility(u);
    }

    /*
     * Checks gender compatibility between the user and the event
     * Parameters: u - the user to evaluate
     * Returns: true if the genders are compatible, false otherwise
     */
    @Override
    public boolean checkGenderCompatibilty(User u){
        return pair[0].getGender() == u.getPreferredGender() && u.getGender() == pair[0].getPreferredGender();
    }
    /*
     * Checks age compatibility between the user and the event
     * Parameters: u - the user to evaluate
     * Returns: true if the ages are compatible, false otherwise
     */
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

    /*
     * The string representation of the match
     * Returns: a string containing the match details
     */
    @Override
    public String toString() {
        String s = "=======================\n";

        s += "Match Type: Single Event\n";

        Event e = getEvent();
        if (e != null) {
            s += "Event Name: " + e.getTitle() + "\n";

            s += "Date: " + getDate() + "\n";
            s += "Start Time: " + getStart() + "\n";
            s += "End Time: " + getEnd() + "\n";
            s += "Compatibility Score: " + getScore() + "%\n";

            s += "-----------------------\n";

            s += "User 1: ";
            if (pair[0] != null) {
                s += pair[0].getName() + "\n";
                s += "User 1 Status: " + status[0] + "\n";
            } else {
                s += "None\n";
            }

            s += "-----------------------\n";

            s += "User 2: ";
            if (pair[1] != null) {
                s += pair[1].getName() + "\n";
                s += "User 2 Status: " + status[1] + "\n";
            } else {
                s += "Still Searching...\n";
            }
        } else {
            s += "Event Name: No Event Compatible\n";
        }

        s += "=======================\n";

        return s;
    }
}