import java.util.*;
import java.time.*;

public class Matchmaker {
    EventDatabase events;
    AccountsDatabase accounts;

    public Matchmaker(){
        events = new EventDatabase();
        accounts = new AccountsDatabase();
    }
    public Object[] generatePool(User user, String type){
        if(type.equals("PersonMatch")){
            return generateUserPool(user);
        }
        return generateEventPool(user);
    }

    public Object[] generateUserPool(User user){
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
                   if(cur instanceof PersonMatch && ((PersonMatch)cur).getSoulmate() == all[i]){
                     alreadyMatched = true;
                   }
                }
                if(!alreadyMatched){
                    pool.add(all[i]);
                }
            }
        }
        
        Object[] poolArray = new Object[pool.size()];
        for (int i = 0; i < pool.size(); i++){
            poolArray[i] = pool.get(i);
        }
        return poolArray; 
    }

    public Object[] generateEventPool(User user){
        ArrayList <Event> all = events.getApprovedEventList();
        ArrayList <Event> pool = new ArrayList<Event>();
        for (int i = 0; i < all.size(); i ++){
            Event curEvent = all.get(i);
            User cur1 = all.get(i).getCurrentUser1();
            User cur2 = all.get(i).getCurrentUser2();
            
            if (curEvent instanceof Virtual || curEvent instanceof Outdoor) {
                if ((cur1 != user && cur2 != user) && (cur1 == null || cur2 == null)) {
                    pool.add(curEvent);
                }
            }else if (curEvent instanceof Indoor) {
                Indoor indoor = (Indoor) curEvent;
                User cur3 = indoor.getUser3();
                User cur4 = indoor.getUser4();
                
                if ((cur1 != user && cur2 != user && cur3 != user && cur4 != user) && 
                    (cur1 == null || cur2 == null || cur3 == null || cur4 == null)) {
                    pool.add(curEvent);
                }
            }

        }
        Object[] poolArray = new Object[pool.size()];
        for (int i = 0; i < pool.size(); i++){
            poolArray[i] = pool.get(i);
        }
        return poolArray;
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
    public EventMatch generateEventMatch(User user){
        EventMatch match = new EventMatch(user);
        Object[] pool = generateEventPool(user);
        match.execute(pool);
        return match;
    }
    public Match generateMatch(User user, int type){
        if(type == 1){
            return generateInterestMatch(user);
        }else if (type == 2){
            return generateDistanceMatch(user);
        }else if (type == 3){
             return generateEventMatch(user);
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
