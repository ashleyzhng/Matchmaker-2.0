/*
   File Name: Appeal.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The Appeal object class represents an Appeal users make to report a User.
*/

public class Appeal {
    private User user;
    private String reason;

    //constructor
    public Appeal(User u, String r){
        user = u;
        reason = r;
    }
    //---accessors and mutators as necessary---
    public User getUser(){
        return user;
    }
    public void setUser(User u){
        user = u;
    }
    public String getReason(){
        return reason;
    }
    public void setReason(String r){
        reason = r;
    }

    /* Includes the username of the user and reason for the report
    * @return as a String object of the infromation
    */
    public String toString(){
        return "User: " + user.getUsername() + "\nReason: " + reason;
    }
}
