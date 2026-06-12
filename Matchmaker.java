import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Matchmaker {
    EventDatabase events;
    AccountsDatabase accounts;
    AppealDatabase appeals;

    public Matchmaker(){
        events = new EventDatabase();
        accounts = new AccountsDatabase(this);
        appeals = new AppealDatabase(); // changes
        accounts.loadMatches(this);
        events.loadAllEvents("event.txt");
    }
    public AccountsDatabase getAccounts(){
        return accounts;
    }
    public AppealDatabase getAppeals(){
        return appeals;
    }
    public EventDatabase getEvents(){
        return events;
    }
    public ArrayList <User> eligibleUserPool(User user){
        User[] all = accounts.getUsers();
        ArrayList <Match> matchHistory = user.getMatchHistory();
        
        if (user.getMatched() || user.maxMatches() || all == null || all.length == 0){
            return new ArrayList<>();
        }
        
        ArrayList <User> pool = new ArrayList<User>();
        for (int i = 0; i < all.length; i ++){
            if (all[i] != null && all[i] != user){
                boolean alreadyMatched = false;
                for (int k = 0; k < matchHistory.size(); k ++){
                   Match cur = matchHistory.get(k);
                   User check = all[i];
                   if(cur instanceof PersonMatch && ( ((PersonMatch)cur).getSoulmate() == check) || ((PersonMatch)cur).getOwner() == check){
                     alreadyMatched = true;
                   }
                   if (cur instanceof EventMatch){
                        Event event = ((EventMatch)cur).getEvent();
                        User cur1 = event.getCurrentUser1();
                        User cur2 = event.getCurrentUser2();
                        if(cur1 == check || cur2 == check){
                            alreadyMatched = true;
                        }
                   }
                }
                if(!alreadyMatched){
                    pool.add(all[i]);
                }
            }
        }
        return pool;
    }
    public Object[] generateUserPool(User user){
        ArrayList <User> pool = eligibleUserPool(user);
        Object[] poolArray = new Object[pool.size()];
        for (int i = 0; i < pool.size(); i++){
            poolArray[i] = pool.get(i);
        }
        return poolArray; 
    }
        
    
    public Object[] generateSingleEventPool(User user){
        if (user == null){
            return null;
        }
        Event[] all = events.getApprovedEventList();
        ArrayList <Event> pool = new ArrayList<Event>();
        ArrayList <User> eligibleUsers = eligibleUserPool(user);

        for (int i = 0; i < all.length; i ++){
            Event curEvent = all[i];
            if (curEvent instanceof Virtual || curEvent instanceof Outdoor){
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

    public Object[] generateDoubleEventPool(User user){
        if(user == null){
            return null;
        }
        Event[] all = events.getApprovedEventList();
        ArrayList<Event> pool = new ArrayList<Event>();
        ArrayList<User> eligibleUsers = eligibleUserPool(user);
        for (int i = 0; i < all.length; i ++){
            Event curEvent = all[i];
            if(curEvent instanceof Indoor){
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
    public InterestMatch generateInterestMatch(User user){
        InterestMatch match = new InterestMatch(user);
        Object[] pool = generateUserPool(user);
        match.execute(pool);
        return match;
    }
    public DistanceMatch generateDistanceMatch(User user){
        DistanceMatch match = new DistanceMatch(user);
        Object[] pool = generateUserPool(user);
        match.execute(pool);
        return match;
    }
    public EventMatch generateSingleEventMatch(User user){
        EventMatch match = new SingleEventMatch(user);
        Object[] pool = generateSingleEventPool(user);
        match.execute(pool);
        return match;
    }
    public EventMatch generateDoubleEventMatch(User user){
        EventMatch match = new DoubleEventMatch(user);
        Object[] pool = generateDoubleEventPool(user);
        match.execute(pool);
        return match;
    }
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
                out.write(a.get(i).getUsername());
                out.write(a.get(i).getPassword());
                out.write(a.get(i).getEmail());
                out.write("" + a.get(i).isEmailVerified());
                if(a.get(i) instanceof User){
                    out.write("" + ((User) (a.get(i))).getName());
                    out.write("" + ((User) (a.get(i))).getBirthday());
                    out.write("" + ((User) (a.get(i))).getPostalCode());
                    out.write("" + ((User) (a.get(i))).getCity());
                    out.write("" + ((User) (a.get(i))).getBio());
                    out.write("" + ((User) (a.get(i))).getGender());
                    out.write("" + ((User) (a.get(i))).getPreferredGender());
                    out.write("" + ((User) (a.get(i))).getMatched());
                    //out.write("" + ((User) (a.get(i))).InterestTagInt());
                }
                if(a.get(i) instanceof Adult){
                    out.write("" + ((Adult) (a.get(i))).getOccupation());
                    out.write("" + ((Adult) (a.get(i))).getMaritalStatus());
                    out.write("" + ((Adult) (a.get(i))).getPreferredMaxAge());
                    out.write("" + ((Adult) (a.get(i))).getPreferredMinAge());
                }   
                if(a.get(i) instanceof Senior){
                    out.write("" + ((Adult) (a.get(i))).getOccupation());
                    out.write("" + ((Adult) (a.get(i))).getMaritalStatus());
                    out.write("" + ((Adult) (a.get(i))).getPreferredMaxAge());
                    out.write("" + ((Adult) (a.get(i))).getPreferredMinAge());
                    out.write("" + ((Senior) (a.get(i))).hasMobilityLimitations());
                }    
            }
            out.close();
        } catch (IOException e){
            System.out.println("Error Accessing File"); 
        }
    }

    public void loadAccountsFromFile(String file){
        accounts.loadFromFile(file, this);
    }

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

    public User searchByUsername(String targetUsername){
        return accounts.searchByUsername(targetUsername);
    }

    public Account searchAccountByUsername(String targetUsername){
        return accounts.searchAccountByUsername(targetUsername);
    }

}
