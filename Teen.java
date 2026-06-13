
/*
   File Name: Teen.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The Teen class is an subclass that extends User. It represents a Teen user account type in the matchmaker system.
*/

import java.time.LocalDate;

public class Teen extends User {
    public static final int MIN_AGE = 13;

    //constructors
    public Teen(String u, String p, String e, boolean ev, String n,  LocalDate b, String pc, String c, String bi, char g, char pg, Tag t){
        super(u, p, e, ev, n, b, pc, c, bi, g, pg, t);

    }
    /* Returns a string representation of this teen
    * @return a string containing information about this teen
    */
    public String toString(){
        return super.toString();
    }
}
