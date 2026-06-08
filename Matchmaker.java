import java.util.*;

public class Matchmaker {
    EventDatabase events;
    AccountsDatabase accounts;

    public Matchmaker(){
        events = new EventDatabase();
        accounts = new AccountsDatabase();
    }
    public Object[] generatePool(User user, String type){
        return null;
    }
    public Object[] userPool(User user){
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

    public Object[] generateEvent(User user){
        ArrayList <Event> all = events.getApprovedEventList();
        ArrayList <Event> pool = new ArrayList<Event>();
        for (int i = 0; i < all.size(); i ++){
            User cur1 = all.get(i).getCurrentUser1();
            User cur2 = all.get(i).getCurrentUser2();
            if(cur1 != user && cur2 != null && cur2 != user){
                pool.add(all[i]);
            }
        }
        Object[] poolArray = new Object[pool.size()];
        for (int i = 0; i < pool.size(); i++){
            poolArray[i] = pool.get(i);
        }
        return poolArray;
        
    }
}
