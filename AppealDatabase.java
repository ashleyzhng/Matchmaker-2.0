/*
   File Name: AppealDatabase.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The AppealDatabase class holds an Array List of appeals
*/
import java.util.ArrayList;

public class AppealDatabase {
    private ArrayList<Appeal> appeals;

    //constructor
    public AppealDatabase(){
        appeals = new ArrayList<Appeal>();
    }
    //---accessors and mutators as necessary---
    public ArrayList<Appeal> getAppeals(){
        return appeals;
    }
    public void setAppeals(ArrayList<Appeal> a){
        appeals = a;
    }

    /* adds an appeal to the database
    * @param User   the user thats being reported
    * @param String the reason for the report
    */
    public void addAppeal(User u, String r){
        appeals.add(new Appeal(u, r));
    }
}
