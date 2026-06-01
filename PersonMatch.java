import java.time.*;
public abstract class PersonMatch extends Match{
    public static final char PENDING = 'P';
    public static final char ACCEPT = 'A';
    public static final char REJECT = 'R';

    public static final int MATCH_LIFETIME = 1;
    private String matchType;
    private ZonedDateTime expiryDate;
    private User[] pair = new User [2];
    private char[] status = new char[2];
    private int[] rating = new int[2];

   
    public PersonMatch(String m, ZonedDateTime exp, User u){
        matchType = m;
        //not proper for expiry date
        expiryDate = exp;
        pair[0]= u;
        pair[1]=null;
        status[0] = PENDING;
        status[1] = PENDING;
        rating[0] = -1;
        rating[1] = -1;
    }
    
    public abstract void execute(Object[] pool);
    
    public boolean isRejection(){
        return status[0]== REJECT && status[1]==REJECT;
    }
    public boolean isAcceptance(){
        return status[0]== ACCEPT && status[1]==ACCEPT;
    }
    
    public boolean isExpire(ZonedDateTime cur){
        return cur.isBefore(expiryDate);
    }

    public void deleteMatch(){
        pair[0].deleteMatch(this);
        pair[1].deleteMatch(this);
    }

    public boolean storeMatch(){
        if (isAvailable(pair[0]) && isAvailable(pair[1])){
            pair[0].storeMatch(this);
            pair[1].storeMatch(this);
            return true;
        }
        return false;
    }
    
    public boolean isAvailable(User u){
        return u.isAvailable();
    }

    public void updateUserDecision(User u, char c){
        if (u == pair[0]){
            status[0]=c;
        }else{
            status[1]=c;
        }
    }

    public double getAverageRating(){
        return (rating[0]+rating[1])/2.0;
    }

    public boolean checkGenderCompatibilty(){
        return pair[0].getGender() == pair[1].getPreferredGender() && pair[1].getGender() == pair[0].getPreferredGender();
    }

}
