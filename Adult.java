
/*
   File Name: Adult.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The Adult class is an subclass that extends User. It represents a Adult user account type in the matchmaker system.
*/
import java.time.LocalDate;
public class Adult extends User {
    public static final int MIN_AGE = 18;
    private String occupation;
    private String maritalStatus;
    private int preferredMaxAge;
    private int preferredMinAge;
    
    //constructors
    public Adult(String u, String p, String e, String n, LocalDate b, String pc, String c, String bi, char g, char pg, String o, String m, int max, int min, Tag t){
        super(u, p, e, n, b, pc, c, bi, g, pg, t);
        maritalStatus = m;
        preferredMaxAge = max;
        preferredMinAge = min;
    }

    public Adult(String u, String p, String e, boolean ev, String n, LocalDate b, String pc, String c, String bi, char g, char pg, String o, String m, int max, int min, Tag t){
        super(u, p, e, ev, n, b, pc, c, bi, g, pg, t);
        maritalStatus = m;
        preferredMaxAge = max;
        preferredMinAge = min;
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
    /* Displays this adults's profile information, overriding the one in user
    */
   @Override
    public void printProfile(){ 
        super.printProfile();
        System.out.println("Occupation: " + occupation);
        System.out.println("Marital Status: " + maritalStatus);
    }
    /* Returns a string representation of this adult
    * @return a string containing information about this adult
    */
    public String toString(){
        return super.toString() + "\nOccupation: " + occupation + "\nMarital Status: " + maritalStatus + "\nPreferred Max Age: " + preferredMaxAge + "\nPreferred Min Age: " + preferredMinAge;
    }

}
