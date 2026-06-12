import java.time.*;
/*
   File Name: DoubleEventMatch.java
   Name: Ashley Zhong
   Class: ICS4U1
   Date: June 2026

   Description:
   The DoubleEventMatch class extends EventMatch and represents a group-based
   matchmaking system involving two pairs of users matched to a single event.
   It evaluates compatibility across multiple users, organizes participants
   into pairs, and manages match storage, validation, and scoring.
*/
public class DoubleEventMatch extends EventMatch {
    //First pair of users (primary match group)
    private User[] pair1 = new User[2];
    //Second pair of users (secondary match group)
    private User[] pair2 = new User[2];
    //status tracking for up to 4 users (P=Pending, A = Accepted, R = Rejected)
    private char[] status = new char[4]; 
    //rating tracking for up to 4 users
    private int[] rating = new int [4];  // -1 if there is no rating

    /*
     * Constructor for DoubleEventMatch
     * Parameters: u - the first user in the primary match group
     * Returns: None
     */
    public DoubleEventMatch(User u) {
        super();
        pair1[0] = u;    
        status[0] = 'P'; 
        status[1] = 'P'; 
        status[2] = 'P';
        status[3] = 'P'; 
    }

    /*
     * Constructor for DoubleEventMatch
     * Parameters: score - the compatibility score of the match
     *             e - the event title
     *             s - the event location
     *             en - the event description
     *             d - the event date
     *             dur - the event duration
     *             eve - the event object
     *             u1, u2, u3, u4 - the four users involved in the match
     *             s1, s2, s3, s4 - the status of each user (P = Pending, A = Accepted, R = Rejected)
     *             r1, r2, r3, r4 - the rating of each user (-1 if no rating)
     * Returns: None
     */
    public DoubleEventMatch(double score, String e, String s, String en, String d, String dur, Event eve, User u1, User u2, User u3, User u4, char s1, char s2, char s3, char s4, int r1, int r2, int r3, int r4) {
        super(score, e, s, en, d, dur, eve);
        pair1[0] = u1;
        pair1[1] = u2;
        pair2[0] = u3;
        pair2[1] = u4;
        status[0] = s1;
        status[1] = s2;
        status[2] = s3;
        status[3] = s4;
        rating[0] = r1;
        rating[1] = r2;
        rating[2] = r3;
        rating[3] = r4;
    }
    
    // accessors and mutators
    public User getUser(int idx){
        if (idx >= 0 && idx < 4) {
            if (idx < 2) {
                return pair1[idx];
            } else {
                return pair2[idx - 2];
            }
        }
        return null;
    }

    public void completed(){
        for (int i = 0; i < status.length; i++) {
            status[i] = 'C';
        }
    }
     /*
      * Executes the matchmaking process for a double event match
      * Parameters: pool - an array of potential events to match against
      * Returns: None (the method updates the state of the match and involved users)
      */
     @Override   
     public void execute(Object[] pool) {
         if (pool != null && pool.length > 0) {
             Event[] ePool = new Event[pool.length];
             for (int i = 0; i < pool.length; i++) {
                 ePool[i] = (Event) pool[i];
             }
             
             Event event = execute(ePool, 0, null, -1);
             
             if (event != null) {
                 LocalTime start = event.getStartTime();
                 Duration duration = event.getDuration();
                 LocalTime end = start.plus(duration);
                 LocalDate date = event.getDate();
                 
                 setStart(start);
                 setEnd(end);
                 setDuration(duration);
                 setDate(date);
                 setEventType("Indoor");
                 setEvent(event); 
                 
                 sortIncomingUsers(event.getCurrentUser1(), event.getCurrentUser2(), ((Indoor)event).getUser3());
                 setEventTitle(event.getTitle());
                 
                 if (pair1[1] != null) {
                     status[1] = ((DoubleEventMatch)(pair1[1].getEventMatch(getEvent().getTitle()))).getStatus(pair1[1]);
                 }
                 if (pair2[0] != null) {
                     status[2] = ((DoubleEventMatch)(pair2[0].getEventMatch(getEvent().getTitle()))).getStatus(pair2[0]);
                 }
                 if (pair2[1] != null) {
                     status[3] = ((DoubleEventMatch)(pair2[1].getEventMatch(getEvent().getTitle()))).getStatus(pair2[1]);
                 }

                 storeMatch();
                 event.addUser(pair1[0]); 
             }
         }
     }

    /*
     * Executes the matchmaking process for a double event match
     * Parameters: pool - an array of potential events to match against
     *             idx - the current index in the event pool
     *             best - the best event found so far
     *             maxScore - the maximum compatibility score found so far
     * Returns: The best matching event or null if none found
     */
    private Event execute(Event[] pool, int idx, Event best, double maxScore) {
        if (pool == null || pool.length == 0) {
            return null;
        }
        if (idx == pool.length) {
            return best;
        }
        
        Event current = pool[idx];
        double curScore = determineScore(current);

        if (curScore > maxScore && isValidEventCandidate(current) && canBeSortedSuccessfully(current)) {
            return execute(pool, idx + 1, current, curScore);
        }
        
        return execute(pool, idx + 1, best, maxScore);
    }

    /*
     * Checks if the people in an event has one that is a valid candidate for matching with the initial user
     * Parameters: e - the event to check
     * Returns: true if the event is valid, false otherwise
     */
    private boolean isValidEventCandidate(Event e) {
        User u1 = e.getCurrentUser1();
        User u2 = e.getCurrentUser2();
        User u3 = ((Indoor)e).getUser3();
        User u4 = ((Indoor)e).getUser4();

        if (u1 != null && !isSameAgeGroup(pair1[0], u1)) return false;
        if (u2 != null && !isSameAgeGroup(pair1[0], u2)) return false;
        if (u3 != null && !isSameAgeGroup(pair1[0], u3)) return false;
        if (u4 != null && !isSameAgeGroup(pair1[0], u4)) return false;
        return true;
    }

    /*
     * Sorts the incoming users for the event match
     * Parameters: u1, u2, u3 - the users to sort
     * Returns: true if the users can be sorted successfully, false otherwise
     */
    private boolean sortIncomingUsers(User u1, User u2, User u3) {
        User[] attendees = {u1, u2, u3};
        
        boolean found = false;
        for (int i = 0; i < attendees.length && !found; i++) {
            if (attendees[i] != null) {
                if (prelimCheck(attendees[i])) {
                    pair1[1] = attendees[i];
                    attendees[i] = null; 
                    found = true;
                }
            }
        }
        if(!found && (u1 != null || u2 != null || u3!= null)){
            return false;
        }

        for (int i = 0; i < attendees.length; i++) {
            if (attendees[i] != null) {
                if (pair2[0] == null) {
                    pair2[0] = attendees[i];
                    attendees[i] = null;
                }
            }
        }

        for (int i = 0; i < attendees.length; i++) {
            if (attendees[i] != null) {
                if (pair2[1] == null) {
                    if (checkGenderCompatibilityForPair(pair2[0], attendees[i])) {
                        if (checkAgeCompatibilityForPair(pair2[0], attendees[i])) {
                            pair2[1] = attendees[i];
                            attendees[i] = null;
                        }
                    }
                }
            }
        }
        return true;
    }

    /*
     * Checks if the users in an event can be sorted successfully
     * Parameters: e - the event to check
     * Returns: true if the users can be sorted successfully, false otherwise
     */
    private boolean canBeSortedSuccessfully(Event e) {
        User u1 = e.getCurrentUser1();
        User u2 = e.getCurrentUser2();
        User u3 = ((Indoor) e).getUser3();
            
        User[] attendees = {u1, u2, u3};
        boolean eligible = false;
            
        for (int i = 0; i < attendees.length && !eligible; i++) {
            if (attendees[i] != null && prelimCheck(attendees[i])) {
                attendees[i] = null;
                eligible = true;
            }
        }
            
        if (!eligible && (u1 != null || u2 != null || u3 != null)) {
            return false;
        }
            
        User p2_0 = null;
        for (int i = 0; i < attendees.length; i++) {
            if (attendees[i] != null) {
                if (p2_0 == null) {
                    p2_0 = attendees[i];
                    attendees[i] = null;
                }
            }
        }
        
            for (int i = 0; i < attendees.length; i++) {
                if (attendees[i] != null) {
                    if (checkGenderCompatibilityForPair(p2_0, attendees[i]) && checkAgeCompatibilityForPair(p2_0, attendees[i])) {
                        attendees[i] = null;
                    }
                }
            }
        
            for (int i = 0; i < attendees.length; i++) {
                if (attendees[i] != null) {
                    return false;
                }
            }
        
            return true;
        }

        /*
         * Checks if two users are in the same age group
         * Parameters: u1, u2 - the users to check
         * Returns: true if the users are in the same age group, false otherwise
         */
        public boolean isSameAgeGroup(User u1, User u2) {
            if (u1 == null || u2 == null) {
                return false;
            }
            if (u1 instanceof Teen && u2 instanceof Teen) {
                return true;
            }
            if (u1 instanceof Adult && u2 instanceof Adult) {
                return true;
            }
            if (u1 instanceof Senior && u2 instanceof Senior) {
                return true;
            }
            return false;
        }
    /*
        * Updates the decision for the user in the match
        * Parameters: u - the user making the decision
        *            x - the decision (A = Accept, R = Reject)
        * Returns: None (the method updates the status of the user in the match)
     */    
    public void updateUserDecision(User u, char x) {
        if (u == pair1[0]) {
            status[0] = x;
            if (x == 'R'){
                deleteMatch(pair1[0]);
                pair1[0] = null;
                getEvent().setCurrentUser1(null);
            }
        } else if (u == pair1[1]) {
            status[1] = x;
            if (x == 'R'){
                deleteMatch(pair1[1]);
                pair1[1] = null;
                getEvent().setCurrentUser2(null);
            } 
        } else if (u == pair2[0]) {
            status[2] = x;
            if (x == 'R'){
                deleteMatch(pair2[0]);
                pair2[0] = null;
                ((Indoor)getEvent()).setUser3(null);
            }
        } else if (u == pair2[1]) {
            status[3] = x;
            if (x == 'R'){
                deleteMatch(pair2[1]);
                pair2[1] = null;
                ((Indoor)getEvent()).setUser4(null);
            }
        }
    }

    /*
     * Checks if all users have accepted the match
     * Parameters: None
     * Returns: true if all users have accepted, false otherwise
     */
    @Override
    public boolean isAcceptance() {
        return status[0] == 'A' && status[1] == 'A' && status[2] == 'A' && status[3] == 'A';
    }

    /*
     * Stores the match information for all users
     * Parameters: None
     * Returns: None
     */
    @Override
    public void storeMatch() {
        if (pair1[0] != null) {
            pair1[0].deleteMatch(getEventTitle());
            pair1[0].storeMatch(this);
        }
        if (pair1[1] != null) {
            pair1[1].deleteMatch(getEventTitle());
            pair1[1].storeMatch(this);
        }
        if (pair2[0] != null) {
            pair2[0].deleteMatch(getEventTitle());
            pair2[0].storeMatch(this);
        }
        if (pair2[1] != null) {
            pair2[1].deleteMatch(getEventTitle());
            pair2[1].storeMatch(this);
        }

        if (getEvent() != null) {
            if (pair1[0] != null) {
                getEvent().addUser(pair1[0]);
            }
        }
    }
    /* 
     * Deletes the match information for one users 
     * Parameters: sys - the event database system to remove the event from
     * Returns: None
     */
    public void deleteMatch (User u) {
        if (pair1[0] == u){
            pair1[0].deleteMatch(this);
        }else if (pair1[1] == u){
            pair1[1].deleteMatch(this);
        }else if (pair2[0] == u){
            pair2[0].deleteMatch(this);
        }else if (pair2[1] == u){
            pair2[1].deleteMatch(this);
        }
    }

    /* 
     * Deletes the match information for all users and removes the event from the system
     * Parameters: sys - the event database system to remove the event from
     * Returns: None
     */
    public void deleteMatch(EventDatabase sys) {
        if (pair1[0] != null) {
            pair1[0].deleteMatch(this);
        }
        if (pair1[1] != null) {
            pair1[1].deleteMatch(this);
        }
        if (pair2[0] != null) {
            pair2[0].deleteMatch(this);
        }
        if (pair2[1] != null) {
            pair2[1].deleteMatch(this);
        }
        if (sys != null) {
            sys.removeApprovedEvent(getEvent());
        }
    }

    /*
     * Determines the score for the match based on event criteria
     * Parameters: e - the event for which to determine the score
     * Returns: the calculated score
     */
    @Override
    public double determineScore(Event e) {
        if (e == null || pair1[0] == null) {
            return 0;
        }
        double tagScore = countTags(e);
        double distanceScore = (distanceTier(e) * 1.0 / 10) * 100; 
        return tagScore * EventMatch.INTEREST_WEIGHT + distanceScore * EventMatch.LOCATION_WEIGHT;
    }

    /*
     * Determines the distance tier for the match based on event location
     * Parameters: e - the event for which to determine the distance tier
     * Returns: the calculated distance tier
     */
    @Override
    public int distanceTier(Event e) {
        if (e instanceof Indoor) {
           Indoor indoor = (Indoor) e;
           String eventLocation = indoor.getLocation();
           return DistanceMatch.calculateDistanceTier(pair1[0].getPostalCode(), eventLocation);
       }
       return 0;
    }

    /*
     * Counts the number of matching tags between the user and the event
     * Parameters: e - the event for which to count tags
     * Returns: the number of matching tags
     */
    @Override
    public double countTags(Event e) {
        if (pair1[0].getTag() == null || e.getCategories() == null) {
            return -1;
        }
        return pair1[0].getTag().compareTo(e.getCategories());
    }
    /*
     * Performs a preliminary check to determine if the user is compatible with the match
     * Parameters: u - the user for which to perform the check
     * Returns: true if the user is compatible, false otherwise
     */
    @Override
    public boolean prelimCheck(User u) {
        return checkGenderCompatibilty(u) && checkAgeCompatibility(u);
    }
    /*
     * Checks if two users are gender compatible
     * Parameters: u - the user for which to perform the check
     * Returns: true if the users are gender compatible, false otherwise
     */
    @Override
    public boolean checkGenderCompatibilty(User u) {
        return checkGenderCompatibilityForPair(pair1[0], u);
    }
    /*
     * Checks if two users are gender compatible
     * Parameters: u1, u2 - the users for which to perform the check
     * Returns: true if the users are gender compatible, false otherwise
     */
    private boolean checkGenderCompatibilityForPair(User u1, User u2) {
        if (u1 == null || u2 == null) return false;
        return u1.getGender() == u2.getPreferredGender() && u2.getGender() == u1.getPreferredGender();
    }

    /*
     * Checks if two users are age compatible
     * Parameters: u1, u2 - the users for which to perform the check
     * Returns: true if the users are age compatible, false otherwise
     */
    @Override
    public boolean checkAgeCompatibility(User u) {
        return checkAgeCompatibilityForPair(pair1[0], u);
    }

    /*
     * Checks if two users are age compatible
     * Parameters: u1, u2 - the users for which to perform the check
     * Returns: true if the users are age compatible, false otherwise
     */
    private boolean checkAgeCompatibilityForPair(User u1, User u2) {
        if (u1 == null || u2 == null) return false;
        if (!isSameAgeGroup(u1, u2)) {
            return false;
        }
        if (u1 instanceof Teen) {
            return true;
        }
        if (u1.getBirthday() == null || u2.getBirthday() == null) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        int age1 = Period.between(u1.getBirthday(), now).getYears();
        int age2 = Period.between(u2.getBirthday(), now).getYears();
        
        if (u1 instanceof Adult) {
            Adult mainUser = (Adult) u1;
            Adult tgtUser = (Adult) u2;
            return age2 >= mainUser.getPreferredMinAge() && age2 <= mainUser.getPreferredMaxAge() && 
                   age1 >= tgtUser.getPreferredMinAge() && age1 <= tgtUser.getPreferredMaxAge();
        }

        Senior mainUser = (Senior) u1;
        Senior tgtUser = (Senior) u2;
        return age2 >= mainUser.getPreferredMinAge() && age2 <= mainUser.getPreferredMaxAge() && 
               age1 >= tgtUser.getPreferredMinAge() && age1 <= tgtUser.getPreferredMaxAge();
    }

    public char getStatus(User u) {
        if (u == pair1[0]) {
            return status[0];
        } else if (u == pair1[1]) {
            return status[1];
        } else if (u == pair2[0]) {
            return status[2];
        } else if (u == pair2[1]) {
            return status[3];
        }
        return 'X';
    }

    /*
     * The string representation of the match
     * Returns: a string containing the match details
     */
    @Override
    public String toString() {
        String s = "=======================\n";
        s += "Match Type: " + getEventType() + " (Double Date)\n";
        s += "Event Name: " + getEvent().getTitle() + "\n";
        s += "Date: " + getDate() + "\n";
        s += "Start Time: " + getStart() + "\n";
        s += "End Time: " + getEnd() + "\n";
        s += "Compatibility Score: " + (getScore()) + "%\n";
        s += "=======================\n";
        s += "       PAIR 1\n";
        s += "-----------------------\n";
    
        s += "User 1 (Host): ";
        if (pair1[0] != null) {
            s += pair1[0].getName() + "\n";
            s += "User 1 Status: " + status[0] + "\n";
        } else {
            s += "None\n";
        }
        
        s += "-----------------------\n";
    
        s += "User 2 (Date): ";
        if (pair1[1] != null) {
            s += pair1[1].getName() + "\n";
            s += "User 2 Status: " + status[1] + "\n";
        } else {
            s += "Still Searching...\n";
        }
        
        s += "=======================\n";
        s += "       PAIR 2\n";
        s += "-----------------------\n";
    
        s += "User 3: ";
        if (pair2[0] != null) {
            s += pair2[0].getName() + "\n";
            s += "User 3 Status: " + status[2] + "\n";
        } else {
            s += "Still Searching...\n";
        }
        
        s += "-----------------------\n";
    
        s += "User 4: ";
        if (pair2[1] != null) {
            s += pair2[1].getName() + "\n";
            s += "User 4 Status: " + status[3] + "\n";
        } else {
            s += "Still Searching...\n";
        }
        
        s += "=======================\n";
        
        return s;
    }

    public void setRating(User u, int r){
        if (u == pair1[0]) {
            rating[0] = r;
        } else if (u == pair1[1]) {
            rating[1] = r;
        } else if (u == pair2[0]) {
            rating[2] = r;
        } else if (u == pair2[1]) {
            rating[3] = r;
        }
    }
}