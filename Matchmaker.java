import java.util.*;
import java.time.*;

public class Matchmaker {
    EventDatabase events;
    AccountsDatabase accounts;

    public Matchmaker(){
        events = new EventDatabase();
        accounts = new AccountsDatabase();
    }
    public ArrayList <User> eligibleUserPool(User user){
        User[] all = accounts.getUsers();
        ArrayList <Match> matchHistory = user.getMatchHistory();
        
        if (user.getMatched() || user.maxMatches() || all == null || all.length == 0){
            return null;
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
                
                if ((cur1 != user && cur2 != user) && (cur1 == null || cur2 == null)) {
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


    /* 
    public boolean changeUsername(String pass, String newUsername){
        if(correctPass(pass)){ // and check if uesrname exists or not
            /* 
            if(AccountsDatabase.searchByUsername(newUsername) == null){
                this.username = newUsername;
                return true;
            }else{
                
                System.out.println("Username Already Exsits");
                return false;
            }
        }else{
            System.out.println("Incorrect Password");
            return false;
        }
    }
    
    public boolean resetPass(String email, String newPass){
        if(this.email == email && emailVerified){
            System.out.println(verifyCode);
            //idk how im gonna do this
            return true;
        }else{
            return false;
        }
    }
    public boolean verifyEmail(String pass){
        if(correctPass(pass)){
            emailVerified = true;
            verifyCode = (int)(Math.random() * 1000000);
            return true;
        }else{
            return false;
        }
    }
    public boolean inactivateAccount(String pass){
        if(correctPass(pass)){
            accountStatus = false;
            //cals id.logout()
            return true;
        }else{
            return false;
        }
    }
    public boolean activateAccount(String pass){
        if(correctPass(pass)){
            /*
            if ID in list of accounts
            if status is inactive
            checks if current password matches 
            if matches
            ^^ add all this stuff in a diff method in matchmaker
            
           accountStatus = true;
           return true;
        }else{
            return false;
        }
    } */
    public User searchByUsername(String targetUsername){
        return accounts.searchByUsername(targetUsername);
    }

}
