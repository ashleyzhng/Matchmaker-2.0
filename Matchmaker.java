import java.util.*;

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
            if (all[i] != null){
                boolean alreadyMatched = false;
                for (int k = 0; i < matchHistory.size(); k ++){
                   Match cur = matchHistory.get(i);
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
            
            User cur1 = all.get(i).getCurrentUser1();
            User cur2 = all.get(i).getCurrentUser2();
            
            if(all.get(i) instanceof Virtual || all.get(i) instanceof Outdoor){
                if (cur1 != null && cur2!= null){
                    if(cur1 != user && cur2!= user){
                        pool.add(all.get(i));
                    }
                }
            }else{
                User cur3 = ((Indoor)all.get(i)).getUser3();
                User cur4 = ((Indoor)all.get(i)).getUser4();
                    if (cur1 != null && cur2!= null && cur3!= null && cur4!= null){
                        if(cur1 != user && cur2!= user && cur3 != user && cur4 != user){
                            pool.add(all.get(i));
                        }
                    }
            }
        }
        Object[] poolArray = new Object[pool.size()];
        for (int i = 0; i < pool.size(); i++){
            poolArray[i] = pool.get(i);
        }
        return poolArray;
        
    }

    public boolean changePass(User u, String curPass, String newPass){
        if(u.correctPass(curPass)){
            u.setPassword(newPass);
            return true;
        }else{
            return false;
        }
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

    public void getMainMenu(){
        System.out.println("Welcome to Matchmaker!");
        System.out.println("=======================");
        System.out.println("1. Login");
        System.out.println("2. Reset Password");
        System.out.println("3. Create Account");
        System.out.println("4. Exit Program");
        System.out.println("=======================");
    }
}
