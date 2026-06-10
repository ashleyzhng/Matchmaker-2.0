import java.time.*;
public abstract class PersonMatch extends Match{
    public static final char PENDING = 'P';
    public static final char ACCEPT = 'A';
    public static final char REJECT = 'R';
    private final String matchType;
    private final LocalDate expiryDate;

    public static final int MATCH_LIFETIME = 1;
    
    private User[] pair = new User [2];
    private char[] status = new char[2];
    private int[] rating = new int[2];

   
    public PersonMatch(String m, String exp, User u){
        super();
        matchType = m;
        //not proper for expiry date
        expiryDate = LocalDate.parse(exp);
        pair[0]= u;
        pair[1]=null;
        status[0] = PENDING;
        status[1] = PENDING;
        rating[0] = -1;
        rating[1] = -1;
    }
    
    public User getOwner(){
        return pair[0];
    }
    public void setSoulmate(User u){
        pair[1]=u;
    }
    public User getSoulmate(){
        return pair[1];
    }

    @Override
    public abstract void execute(Object[] pool);
    
    @Override
    public boolean isRejection(){
        return status[0]== REJECT && status[1]==REJECT;
    }
    @Override
    public boolean isAcceptance(){
        return status[0]== ACCEPT && status[1]==ACCEPT;
    }
    
    @Override
    public boolean isExpire(){
        return expiryDate.isBefore(LocalDate.now());
    }

    @Override
    public void deleteMatch(){
        pair[0].deleteMatch(this);

        //CANNOT DO THIS, need ot make a new copy, soulmate stuff is different
        pair[1].deleteMatch(this);
    }

    @Override
    public void storeMatch(){
        pair[0].storeMatch(this);
        pair[1].storeMatch(this);
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

    public boolean prelimCheck(User u){
        return checkGenderCompatibilty(u) && checkAgeCompatibility(u);
    }

    public boolean checkGenderCompatibilty(User u){
        return pair[0].getGender() == u.getPreferredGender() && u.getGender() == pair[0].getPreferredGender();
    }

    public boolean checkAgeCompatibility(User u){
        if (u instanceof Teen){
            return true;
        }
        if (u.getBirthday() == null){
            return false;
        }
        LocalDate now = LocalDate.now();
        Period elapse = Period.between(u.getBirthday(), now);
        int uAge = elapse.getYears();
        elapse = Period.between(pair[0].getBirthday(), now);
        int mainAge = elapse.getYears();
        
        if(u instanceof Adult){
            Adult user = (Adult)u;
            Adult mainUser = (Adult)pair[0];
            return uAge >= mainUser.getPreferredMinAge() && uAge <= mainUser.getPreferredMaxAge() && mainAge >= user.getPreferredMinAge() && mainAge <= user.getPreferredMaxAge();
        }

        Senior user = (Senior)u;
        Senior mainUser = (Senior)pair[0];
        return uAge >= mainUser.getPreferredMinAge() && uAge <= mainUser.getPreferredMaxAge() && mainAge >= user.getPreferredMinAge() && mainAge <= user.getPreferredMaxAge();
        
    }

    public void setRating(int r, User u){
        if (u == pair[0]){
            rating[0]=r;
        }else{
            rating[1]=r;
        }
    }

    @Override
    public String toString(){
        String s = "=======================\n";
        s += "Match Number: " + this.getMatchNum() + "\n";
        s += "Match Type: " + matchType + "\n";
        s += "Expiry Date: " + expiryDate + "\n";
        s += "Score: " + (this.getScore()*100) + "%\n";
        s += "-----------------------\n";
    
        // User 1 (Owner) Info
        s += "User 1: ";
        if (pair[0] != null) {
            s += pair[0].getName() + "\n";
        } else {
            s += "None\n";
        }
        s += "User 1 Status: " + status[0] + "\n";
        s += "User 1 Rating: " + rating[0] + "\n";
        s += "-----------------------\n";
    
        // User 2 (Soulmate) Info
        s += "User 2: ";
        if (pair[1] != null) {
            s += pair[1].getName() + "\n";
        } else {
            s += "Still Searching...\n";
        }
        s += "User 2 Status: " + status[1] + "\n";
        s += "User 2 Rating: " + rating[1] + "\n";
        s += "=======================\n";
        
        return s;
    }
}