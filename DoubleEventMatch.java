import java.time.*;

public class DoubleEventMatch extends EventMatch {
    private User[] pair1 = new User[2];
    private User[] pair2 = new User[2];
    private char[] status = new char[4]; 

    public DoubleEventMatch(User u) {
        super();
        pair1[0] = u;    
        status[0] = 'P'; 
        status[1] = 'P'; 
        status[2] = 'P'; 
    }

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
                
                storeMatch();
                event.addUser(pair1[0]); 
            }
        }
    }

    public Event execute(Event[] pool, int idx, Event best, double maxScore) {
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

    public void updateUserDecision(User u, char x) {
        if(u == pair1[0]){
            status[0]=x;
        }else if (u == pair1[1]){
            status[1]=x;
        }else if (u == pair2[0]){
            status[2]=x;
        }else if (u == pair2[1]){
            status[3]=x;
        }
    }

    @Override
    public boolean isRejection() {
        return status[0] == 'R' || status[1] == 'R' || status[2] == 'R' || status[3] == 'R';
    }

    @Override
    public boolean isAcceptance() {
        return status[0] == 'A' && status[1] == 'A' && status[2] == 'A' || status[3]=='A';
    }

    @Override
    public void storeMatch() {
        if (pair1[0] != null) {
            pair1[0].storeMatch(this);
        }
        if (pair1[1] != null) {
            pair1[1].storeMatch(this);
        }
        if (pair2[0] != null) {
            pair2[0].storeMatch(this);
        }
        if (pair2[1] != null) {
            pair2[1].storeMatch(this);
        }

        if (getEvent() != null) {
            if (pair1[0] != null) {
                getEvent().addUser(pair1[0]);
            }
        }
    }

    //deletes for the singular user
    public void deleteMatch() {
        if (pair1[0] != null){
            pair1[0].deleteMatch(this);
        }
    }

    //deletes for all, also deletes event!
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

    @Override
    public double determineScore(Event e) {
        if (e == null || pair1[0] == null) {
            return 0;
        }
        double tagScore = countTags(e);
        double distanceScore = (distanceTier(e) * 1.0 / 10) * 100; 
        return tagScore * EventMatch.INTEREST_WEIGHT + distanceScore * EventMatch.LOCATION_WEIGHT;
    }

    @Override
    public int distanceTier(Event e) {
        if (e instanceof Indoor) {
           Indoor indoor = (Indoor) e;
           String eventLocation = indoor.getLocation();
           return DistanceMatch.calculateDistanceTier(pair1[0].getPostalCode(), eventLocation);
       }
       return 0;
    }

    @Override
    public double countTags(Event e) {
        if (pair1[0].getTag() == null || e.getCategories() == null) {
            return -1;
        }
        return pair1[0].getTag().compareTo(e.getCategories());
    }

    @Override
    public boolean prelimCheck(User u) {
        return checkGenderCompatibilty(u) && checkAgeCompatibility(u);
    }

    @Override
    public boolean checkGenderCompatibilty(User u) {
        return checkGenderCompatibilityForPair(pair1[0], u);
    }

    private boolean checkGenderCompatibilityForPair(User u1, User u2) {
        if (u1 == null || u2 == null) return false;
        return u1.getGender() == u2.getPreferredGender() && u2.getGender() == u1.getPreferredGender();
    }

    @Override
    public boolean checkAgeCompatibility(User u) {
        return checkAgeCompatibilityForPair(pair1[0], u);
    }

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
}