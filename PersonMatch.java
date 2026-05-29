public abstract class PersonMatch extends Match{
    public static final char PENDING = 'P';
    public static final char ACCEPT = 'A';
    public static final char REJECT = 'R';

    public static final int MATCH_LIFETIME = 1;
    private String matchType;
    private int expiryTime;
    private User[] pair = new User [2];
    private char[] status = new char[2];
    private int[] satisfactionRating = new int[2];

   /* 
    public PersonMatch(String m, String exp, User u){
        matchType = m;
        //not proper for expiry date
        expiryDate = exp;
        pair[0]= u;
        pair[1]=null;
        status[0] = PENDING;
        status[1] = PENDING;
        satisfactionRating[0] = -1;
        satisfactionRating[1] = -1;
    }
    
    public abstract void execute(Object[] pool);
    public boolean isRejection(){
        return status[0]== REJECT && status[1]==REJECT;
    }
    public boolean isAcceptance(){
        return status[0]== ACCEPT && status[1]==ACCEPT;
    }
    public boolean isExpire
    */

}
