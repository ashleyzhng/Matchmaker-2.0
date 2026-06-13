/*
   File Name: Account.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The Account class is an abstract superclass. It represents a single account in the matchmaker system.
*/
public abstract class Account{
    private String username;
    private String password;
    private String email;
    private boolean emailVerified;

    //constructor for manual account creation
    public Account(String u, String p, String e){
        username = u;
        password = p;
        email = e;
        emailVerified = false;
    }
    
    //constructor for loading from file
    public Account(String u, String p, String e, boolean ev){
        username = u;
        password = p;
        email = e;
        emailVerified = ev;
    }

    //---accessors and mutators as necessary---
    public String getUsername(){
        return username;
    }
    public void setUsername(String u){
        username = u;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String p){
        password = p;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String e){
        email = e;
    }
    public boolean isEmailVerified(){
        return emailVerified;
    }
    public void setEmailVerified(boolean ev){
        emailVerified = ev;
    }

    /* Compares the password of this account with a given password
     * @param String the given/inputed password
     * @return true or false depending if the password matches
     */
    public boolean correctPass(String pass){
        if(pass == null){
            return false;
        }
        return pass.equals(this.password);
    }
    
    /* Returns a String value of all the Fields
     * @param N/A
     * @return true or false depending if the password matches
     */
    public String toString(){
        return "Username: " + username + "\nEmail: " + email + "\nEmail Verified: " + emailVerified;
    }
}