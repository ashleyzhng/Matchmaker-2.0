
/*
   File Name: Senior.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The Senior class is an subclass that extends User. It represents a Senior user account type in the matchmaker system.
*/
import java.time.LocalDate;

public class Senior extends User{
    public static final int MIN_AGE = 65;
    private String occupation;
    private String maritalStatus;
    private int preferredMaxAge;
    private int preferredMinAge;
    private boolean hasMobilityLimitations;
    //constructors
    public Senior(String u, String p, String e, String n, LocalDate b, String pc, String c, String bi, char g, char pg, String o, String m, int max, int min, boolean h, Tag t){
        super(u, p, e, n, b, pc, c, bi, g, pg, t);
        occupation = o;
        maritalStatus = m;
        preferredMaxAge = max;
        preferredMinAge = min;
        hasMobilityLimitations = h;
    }
    public Senior(String u, String p, String e, boolean ev, String n, LocalDate b, String pc, String c, String bi, char g, char pg, String o, String m, int max, int min, boolean h, Tag t){
        super(u, p, e, ev, n, b, pc, c, bi, g, pg, t);
        occupation = o;
        maritalStatus = m;
        preferredMaxAge = max;
        preferredMinAge = min;
        hasMobilityLimitations = h;
    }
    //---accessors and mutators as necessary---
    public String getOccupation(){
        return occupation;
    }
    public void setOccupation(String o){
        occupation = o;
    }
    public String getMaritalStatus(){
        return maritalStatus;
    }
    public void setMaritalStatus(String m){
        maritalStatus = m;
    }
        public int getPreferredMaxAge(){
        return preferredMaxAge;
    }
    public void setPreferredMaxAge(int max){
        preferredMaxAge = max;
    }
    public int getPreferredMinAge(){
        return preferredMinAge;
    }
    public void setPreferredMinAge(int min){
        preferredMinAge = min;
    }
    public boolean hasMobilityLimitations(){
        return hasMobilityLimitations;
    }
    public void setHasMobilityLimitations(boolean h){
        hasMobilityLimitations = h;
    }
    /* Displays this seniors's profile information, overriding the one in user
    */
   @Override
    public void printProfile(){ 
        super.printProfile();
        System.out.println("Occupation: " + occupation);
        System.out.println("Marital Status: " + maritalStatus);
        System.out.println("Has Mobility Limitations: " + hasMobilityLimitations);
    }
    /* Returns a string representation of this senior
    * @return a string containing information about this senior
    */
    public String toString(){
        return super.toString() + "\nOccupation: " + occupation + "\nMarital Status: " + maritalStatus + "\nPreferred Max Age: " + preferredMaxAge + "\nPreferred Min Age: " + preferredMinAge + "\nHas Mobility Limitations: " + hasMobilityLimitations;
    }
}

