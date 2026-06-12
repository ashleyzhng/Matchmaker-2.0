import java.time.*;
/*
   File Name: PersonMatch.java
   Name: Ashley Zhong
   Class: ICS4U1
   Date: June 2026

   Description:
   The PersonMatch class is an abstract subclass of Match. It stores 
   person-related information such as match type, expiry date, user pair, status, and ratings. It also
   defines abstract methods that subclasses must implement to matching alogrithims,
   calculate score, and manage person-based matches.
*/
public abstract class PersonMatch extends Match{
    // status constants
    public static final char PENDING = 'P';
    public static final char ACCEPT = 'A';
    public static final char REJECT = 'R';
    
    //match type: "Interest" or "Distance"
    private final String matchType;
    //expiry date for the match, set to 2 days after creation (11:59 PM on the 2nd day)
    private final LocalDate expiryDate;
    //match pair and status/rating arrays
    public static final int MATCH_LIFETIME = 1;
    // pair[0] is the owner of the match, pair[1] is the soulmate
    private User[] pair = new User [2];
    // status[0] is the owner's decision, status[1] is the soulmate's decision
    private char[] status = new char[2];
    //rating[0] is the owner's rating, rating[1] is the soulmate's rating, -1 if not rated yet
    private int[] rating = new int[2];

   
    /*
     * Constructor for the PersonMatch class
     * Parameters: m - the match type, u - the user for whom to create the match
     */
    public PersonMatch(String m, User u){
        super();
        matchType = m;
        expiryDate = LocalDate.now().plusDays(2);
        pair[0]= u;
        pair[1]=null;
        status[0] = PENDING;
        status[1] = PENDING;
        rating[0] = -1;
        rating[1] = -1;
    }
    /*
     * Constructor for the PersonMatch class - used for reading in file for User
     * Parameters: score - the match score, m - the match type, u - the owner of the match, 
     * s - the soulmate, exp - the expiry date, ownerStatus - the owner's decision, 
     * soulmateStatus - the soulmate's decision, ownerRating - the owner's rating, soulmateRating - the soulmate's rating
     */
    public PersonMatch(double score, String m, User u, User s, String exp, char ownerStatus, char soulmateStatus, int ownerRating, int soulmateRating){
        super(score);
        matchType = m;
        expiryDate = LocalDate.parse(exp);
        pair[0]= u;
        pair[1]= s;
        status[0] = ownerStatus;
        status[1] = soulmateStatus;
        rating[0] = ownerRating;
        rating[1] = soulmateRating;
    }

    public void completed(){
        status[0] = 'C';
        status[1] = 'C';
    }

    //---accessors and mutators---
    public User getOwner(){
        return pair[0];
    }
    public void setSoulmate(User u){
        pair[1]= u;
    }
    public User getSoulmate(){
        return pair[1];
    }
    public LocalDate getExpiryDate(){
        return expiryDate;
    }
    public void setRating(int r, User u){
        if (u == pair[0]){
            rating[0]=r;
        }else{
            rating[1]=r;
        }
    }
    public char getStatus(User u){
        if (u == pair[0]){
            return status[0];
        }else{
            return status[1];
        }
    }
    public int getRating(User u){
        if (u == pair[0]){
            return rating[0];
        }else{
            return rating[1];
        }
    }

    ///---abstract methods that subclasses must implement, refer to subclasses for further details---
    public abstract void execute(Object[] pool);
    
    /*
     * Checks if the match has been rejected by both users
     *Parameters: none
     * Returns true if both users have rejected the match, false otherwise
     */
    public boolean isRejection(){
        return status[0]== REJECT && status[1]==REJECT;
    }

    /*
     * Checks if the match has been accepted by both users
     * Parameters: none
     * Returns true if both users have accepted the match, false otherwise
     */
    @Override
    public boolean isAcceptance(){
        return status[0]== ACCEPT && status[1]==ACCEPT;
    }
    /*
     * Checks if the match has expired
     * Parameters: none
     * Returns true if the match has expired, false otherwise
     */
    @Override
    public boolean isExpire(){
        return expiryDate.isBefore(LocalDate.now());
    }
    /*
     * Deletes the match from both users
     * Parameters: none
     * Returns: none
     */
    public void deleteMatch(){
        pair[0].deleteMatch(this);
        pair[1].deleteMatch(this);
    }
    /*
     * Stores the match in both users' records
     * Parameters: none
     * Returns: none
     */
    public void storeMatch(){
        pair[0].storeMatch(this);
        pair[1].storeMatch(this);
    }
    /* 
     * Updates the decision of a user in the match
     * Parameters: u - the user whose decision to update, c - the new decision
     * Returns: none
     */
    public void updateUserDecision(User u, char c){
        if (c == 'R'){
            pair[0].deleteMatch(this);
            pair[1].deleteMatch(this);
        }else{
            if (u == pair[0]){
                status[0]=c;
            }else{
                status[1]=c;
            }
        }
    }
    /*
     * Gets the average rating of the match
     * Parameters: none
     * Returns: the average rating
     */
    public double getAverageRating(){
        return (rating[0]+rating[1])/2.0;
    }
    /*  
     * Checks the preliminary criteria for a user
     * Parameters: u - the user to check
     * Returns: true if the user meets the criteria, false otherwise
     */
    @Override
    public boolean prelimCheck(User u){
        return checkGenderCompatibilty(u) && checkAgeCompatibility(u);
    }
    /*
     * Checks gender compatibility between users
     * Parameters: u - the user to check
     * Returns: true if the users are compatible, false otherwise
     */
    @Override
    public boolean checkGenderCompatibilty(User u){
        return pair[0].getGender() == u.getPreferredGender() && u.getGender() == pair[0].getPreferredGender();
    }
    /*
     * Checks age compatibility between users
     * Parameters: u - the user to check
     * Returns: true if the users are compatible, false otherwise
     */
    @Override
    public boolean checkAgeCompatibility(User u){
        if (u instanceof Teen && pair[0] instanceof Teen){
            return true;
        }else if (pair[0] instanceof Teen){
            return false;
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

    /*
     * The string representation of the match
     * Returns: a string containing the match details
     */

    @Override
    public String toString(){
        String s = "=======================\n";
        s += "Match Type: " + matchType + "\n";
        s += "Expiry Date: " + expiryDate + "\n";
        s += "Compatibility Score: " + (this.getScore()) + "%\n";
        s += "-----------------------\n";
    
        // User 1 (Owner) Info
        s += "User 1: ";
        if (pair[0] != null) {
            s += pair[0].getName() + "\n";
            s += "User 1 Status: " + status[0] + "\n";
            if(rating[0] == -1){
                s+= "User 1 Rating: n/a \n";
            }else{
                 s += "User 1 Rating: " + rating[0] + "\n";
            }
        } else {
            s += "None\n";
        }
        
        s += "-----------------------\n";
    
        // User 2 (Soulmate) Info
        s += "User 2: ";
        if (pair[1] != null) {
            s += pair[1].getName() + "\n";
            s += "User 2 Status: " + status[1] + "\n";
             if(rating[1] == -1){
                s+= "User 2 Rating: n/a \n";
            }else{
                 s += "User 2 Rating: " + rating[1] + "\n";
            }
        } else {
            s += "Still Searching...\n";
        }
        
        s += "=======================\n";
        
        return s;
    }
}