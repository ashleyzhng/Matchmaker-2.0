import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
/**
 * ----------------------------------------------------------------------------
 * File Name:    Matchmaker.java
 * Authors:      Ashley Zhong, Joyce Lin, Kathy Wu
 * Course:       ICS4U1
 * Description:  The central core of the Matchmaker system. It connects the 
 * account, event, and appeal databases together. It handles 
 * filtering eligible users, setting up pools for events, 
 * generating different match types, and saving or loading data.
 * ----------------------------------------------------------------------------
 */
public class Matchmaker {
    // Stores and manages all community events
    EventDatabase events;
    // Stores and manages all system accounts and user profiles
    AccountsDatabase accounts;
    // Stores and manages all user-submitted reports and appeals
    AppealDatabase appeals;

    /**
     * Initializes a new Matchmaker backend coordinator.
     * Sets up the databases and loads existing match history from storage.
     */
    public Matchmaker(){
        events = new EventDatabase();
        accounts = new AccountsDatabase(this);
        appeals = new AppealDatabase(); // changes
        accounts.loadMatches(this);
    }

    /**
     * Gets the database that stores all accounts.
     *
     * @return      The database containing all system accounts
     */
    public AccountsDatabase getAccounts(){
        return accounts;
    }

    /**
     * Gets the database that stores all accounts.
     *
     * @return      The database containing all system accounts
     */
    public AppealDatabase getAppeals(){
        return appeals;
    }

    /**
     * Gets the database that stores all events.
     *
     * @return      The database containing all created events
     */
    public EventDatabase getEvents(){
        return events;
    }

    /**
     * Verifies user credentials for quick authentication bypass tests.
     *
     * @param username The typed login name
     * @param password The typed login security pass
     * @return      True if credentials match an active user, false if not
     */
    public boolean login(String username, String password){
        User[] users = accounts.getUsers();
        for(int i = 0; i < users.length; i++){
            if(users[i] != null && users[i].getUsername().equals(username) && users[i].correctPass(password)){
                System.out.println("Login Successful");
                return true;
            }
        }
        System.out.println("Login Failed");
        return false;
    }

    /**
     * Filters all registered accounts to find eligible matching candidates.
     * Skips the user themselves, past matches, and active current matches.
     *
     * @param user  The user looking for candidate matches
     * @return      An ArrayList of valid, unmatched users
     */
    public ArrayList<User> eligibleUserPool(User user) {
        User[] all = accounts.getUsers();
        ArrayList<Match> matchHistory = user.getMatchHistory();
        if (user.getMatched() || user.maxMatches() || all == null || all.length == 0) {
            return new ArrayList<>();
        }
        ArrayList<User> pool = new ArrayList<User>();
        for (int i = 0; i < all.length; i++) {
            User check = all[i];
            if (check != null && check != user) {
                boolean alreadyMatched = false;
                // Check match history
                for (int k = 0; k < matchHistory.size() && !alreadyMatched; k++) {
                    Match cur = matchHistory.get(k);
                    if (cur instanceof PersonMatch &&
                    (((PersonMatch) cur).getSoulmate() == check ||
                        ((PersonMatch) cur).getOwner() == check)) {
                        alreadyMatched = true;
                    }
                    if (cur instanceof EventMatch) {
                        Event event = ((EventMatch) cur).getEvent();
                        if (event != null && (event.getCurrentUser1() == check ||
                            event.getCurrentUser2() == check)) {
                            alreadyMatched = true;
                        }
                    }
                }
                // Check CURRENT matches
                if (!alreadyMatched) {
                    Match[] currentMatches = user.getMatches();
                    for (int k = 0; k < currentMatches.length && !alreadyMatched; k++) {
                        Match cur = currentMatches[k];
                        if (cur instanceof PersonMatch &&
                        (((PersonMatch) cur).getSoulmate() == check ||
                            ((PersonMatch) cur).getOwner() == check)) {
                            alreadyMatched = true;
                        }
                        if (cur instanceof EventMatch) {
                            Event event = ((EventMatch) cur).getEvent();
                            if (event!= null && (event.getCurrentUser1() == check ||
                                event.getCurrentUser2() == check)) {
                                alreadyMatched = true;
                            }
                        }
                    }
                }
                if (!alreadyMatched) {
                    pool.add(check);
                }
            }
        }
        return pool;
    }

    /**
     * Converts the list of eligible users into a basic object array.
     *
     * @param user  The user requesting the candidate pool
     * @return      An Object array containing eligible target users
     */
    public Object[] generateUserPool(User user){
        ArrayList <User> pool = eligibleUserPool(user);
        Object[] poolArray = new Object[pool.size()];
        for (int i = 0; i < pool.size(); i++){
            poolArray[i] = pool.get(i);
        }
        return poolArray; 
    }
        
    /**
     * Finds approved 2-person events (Virtual or Outdoor) available for matching.
     * Verifies that the event slots are open and attendees are compatible.
     *
     * @param user  The user looking for an event match
     * @return      An Object array of eligible small event matches
     */
    public Object[] generateSingleEventPool(User user){
        if (user == null){
            return null;
        }
        Event[] all = events.getApprovedEventList();
        ArrayList <Event> pool = new ArrayList<Event>();
        ArrayList <User> eligibleUsers = eligibleUserPool(user);

        for (int i = 0; i < all.length; i ++){
            Event curEvent = all[i];
            if (curEvent!= null && (curEvent instanceof Virtual || curEvent instanceof Outdoor)){
                User cur1 = all[i].getCurrentUser1();
                User cur2 = all[i].getCurrentUser2();
                
                if ((cur1 != user && cur2 != user) && (cur1 == null || cur2 == null)
                    && (cur1 == null || eligibleUsers.contains(cur1)) && (cur2 == null || eligibleUsers.contains(cur2))){
                    pool.add(curEvent);
                }
            }
            
        }
        return pool.toArray();
    }

    /**
     * Finds approved 4-person events (Indoor) available for matching.
     * Ensures the event has open space and existing attendees are valid match candidates.
     *
     * @param user  The user looking for a double-date event match
     * @return      An Object array of eligible large event matches
     */
    public Object[] generateDoubleEventPool(User user){
        if(user == null){
            return null;
        }
        Event[] all = events.getApprovedEventList();
        ArrayList<Event> pool = new ArrayList<Event>();
        ArrayList<User> eligibleUsers = eligibleUserPool(user);
        for (int i = 0; i < all.length; i ++){
            Event curEvent = all[i];
            
            if(curEvent != null && curEvent instanceof Indoor){
                Indoor indoor = (Indoor)curEvent;
                User cur1 = indoor.getCurrentUser1();
                User cur2 = indoor.getCurrentUser2();
                User cur3 = indoor.getUser3();
                User cur4 = indoor.getUser4();
                if (cur1!= user && cur2!= user && cur3!= user && cur4!= user){
                    int attendeeCount = 0;
                    if (cur1 != null) attendeeCount++;
                    if (cur2 != null) attendeeCount++;
                    if (cur3 != null) attendeeCount++;
                    if (cur4 != null) attendeeCount++;
                    boolean allAttendeesEligible = true;
                    if(attendeeCount >= 1 && attendeeCount <= 3){
                        if (cur1 != null && !eligibleUsers.contains(cur1)) {
                            allAttendeesEligible = false;
                        }
                        if (cur2 != null && !eligibleUsers.contains(cur2)) {
                            allAttendeesEligible = false;
                        }
                        if (cur3 != null && !eligibleUsers.contains(cur3)) {
                            allAttendeesEligible = false;
                        }
                        if (cur4 != null && !eligibleUsers.contains(cur4)) {
                            allAttendeesEligible = false;
                        }

                        if (allAttendeesEligible) {
                            pool.add(indoor);
                        }
                    }
                }
            }

        }
        return pool.toArray();
    }

    /**
     * Creates a new match card based on shared hobbies and tags.
     *
     * @param user  The user requesting a match
     * @return      An initialized InterestMatch object
     */
    public InterestMatch generateInterestMatch(User user){
        InterestMatch match = new InterestMatch(user);
        Object[] pool = generateUserPool(user);
        match.execute(pool);
        return match;
    }
    /**
     * Creates a new match card based on closest regional location.
     *
     * @param user  The user requesting a match
     * @return      An initialized DistanceMatch object
     */
    public DistanceMatch generateDistanceMatch(User user){
        DistanceMatch match = new DistanceMatch(user);
        Object[] pool = generateUserPool(user);
        match.execute(pool);
        return match;
    }
    /**
     * Creates a new match card centered around a 2-person event pairing.
     *
     * @param user  The user requesting an event match
     * @return      An initialized SingleEventMatch object
     */
    public EventMatch generateSingleEventMatch(User user){
        EventMatch match = new SingleEventMatch(user);
        Object[] pool = generateSingleEventPool(user);
        match.execute(pool);
        return match;
    }
    /**
     * Creates a new match card centered around a 4-person date event group.
     *
     * @param user  The user requesting a group match
     * @return      An initialized DoubleEventMatch object
     */
    public EventMatch generateDoubleEventMatch(User user){
        EventMatch match = new DoubleEventMatch(user);
        Object[] pool = generateDoubleEventPool(user);
        match.execute(pool);
        return match;
    }
    /**
     * Main router method to generate a match by a specific numeric category.
     *
     * @param user  The user requesting a match
     * @param type  The type code (1=Interest, 2=Distance, 3=Single Event, 4=Double Event)
     * @return      The newly generated Match object, or null if the type number is wrong
     */
    public Match generateMatch(User user, int type){
        if(type == 1){
            return generateInterestMatch(user);
        }else if (type == 2){
            return generateDistanceMatch(user);
        }else if (type == 3){
             return generateSingleEventMatch(user);
        }else if (type == 4){
            return generateDoubleEventMatch(user);
        }
        return null;
    }
    /**
     * Exports all account records cleanly into a text file storage.
     * Automatically formats details based on specific type (Teen, Adult, Senior, Admin).
     *
     * @param file  The path destination filename string
     */
    public void saveAccountsToFile(String file){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            ArrayList<Account> a = accounts.getAccounts(); 
            out.write(a.size());
            for(int i = 0; i< a.size(); i++){
                if(a.get(i) instanceof Admin){
                    out.write("admin");
                }
                if(a.get(i) instanceof Teen){
                    out.write("teen");
                }
                if(a.get(i) instanceof Adult){
                    out.write("adult");
                }
                if(a.get(i) instanceof Senior){
                    out.write("senior");
                }
                out.newLine();
                out.write(a.get(i).getUsername());
                out.newLine();
                out.write(a.get(i).getPassword());
                out.newLine();
                out.write(a.get(i).getEmail());
                out.newLine();
                out.write("" + a.get(i).isEmailVerified());
                out.newLine();
                if(a.get(i) instanceof User){
                    out.write("" + ((User) (a.get(i))).getName());
                    out.newLine();
                    out.write("" + ((User) (a.get(i))).getBirthday());
                    out.newLine();
                    out.write("" + ((User) (a.get(i))).getPostalCode());
                    out.newLine();
                    out.write("" + ((User) (a.get(i))).getCity());
                    out.newLine();
                    out.write("" + ((User) (a.get(i))).getBio());
                    out.newLine();
                    out.write("" + ((User) (a.get(i))).getGender());
                    out.newLine();
                    out.write("" + ((User) (a.get(i))).getPreferredGender());
                    out.newLine();
                    out.write("" + ((User) (a.get(i))).getMatched());
                    out.newLine();
                    //out.write("" + ((User) (a.get(i))).InterestTagInt());
                }
                if(a.get(i) instanceof Adult){
                    out.write("" + ((Adult) (a.get(i))).getOccupation());
                    out.newLine();
                    out.write("" + ((Adult) (a.get(i))).getMaritalStatus());
                    out.newLine();
                    out.write("" + ((Adult) (a.get(i))).getPreferredMaxAge());
                    out.newLine();
                    out.write("" + ((Adult) (a.get(i))).getPreferredMinAge());
                    out.newLine();
                }   
                if(a.get(i) instanceof Senior){
                    out.write("" + ((Senior) (a.get(i))).getOccupation());
                    out.newLine();
                    out.write("" + ((Senior) (a.get(i))).getMaritalStatus());
                    out.newLine();
                    out.write("" + ((Senior) (a.get(i))).getPreferredMaxAge());
                    out.newLine();
                    out.write("" + ((Senior)(a.get(i))).getPreferredMinAge());
                    out.newLine();
                    out.write("" + ((Senior) (a.get(i))).hasMobilityLimitations());
                } 
                out.newLine();   
            }
            out.close();
        } catch (IOException e){
            System.out.println("Error Accessing File"); 
        }
        accounts.saveMatches(this);
    }
    /**
     * Directs the database component to import account records from a file.
     *
     * @param file  The source data file path string
     */
    public void loadAccountsFromFile(String file){
        accounts.loadFromFile(file, this);
    }
    /**
     * Saves all user reports and reasons out to a plain text file.
     *
     * @param file  The path destination filename string
     */
    public void saveAppealsToFile(String file){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            ArrayList<Appeal> a = appeals.getAppeals();
            out.write(a.size());
            for(int i = 0; i< a.size(); i++){
                out.write(a.get(i).getUser().getUsername());
                out.write(a.get(i).getReason());
            }
            out.close();
        }catch (IOException e){
            System.out.println("Error Accessing File"); 
        }
    }
    /**
     * Parses and builds the appeal tracking data back from a text storage file.
     *
     * @param file  The source report data file path string
     */
    public void loadAppealsFromFile(String file){
        ArrayList<Appeal> a = new ArrayList<>();
        try{
            BufferedReader in = new BufferedReader(new FileReader(file));
            int num = Integer.parseInt(in.readLine());
            User u = null;
            for(int i = 0; i<num; i++){
                u = accounts.searchByUsername(in.readLine());
                a.add(new Appeal(u, in.readLine()));
            }
            in.close();
            if (appeals == null) { // changes
                appeals = new AppealDatabase();
            }
            appeals.setAppeals(a); // changes
        }catch (IOException e){
            System.out.println("Error Accessing File"); 
        }
    }

    /**
     * Finds a standard user profile by their exact username identifier string.
     *
     * @param targetUsername The target username to look up
     * @return      The matching User object instance, or null if not found
     */
    public User searchByUsername(String targetUsername){
        return accounts.searchByUsername(targetUsername);
    }

    /**
     * Finds any account type (User, Admin, Parent) matching a specific username.
     *
     * @param targetUsername The target username to look up
     * @return      The matching Account object instance, or null if not found
     */
    public Account searchAccountByUsername(String targetUsername){
        return accounts.searchAccountByUsername(targetUsername);
    }

}
