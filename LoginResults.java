/*
   File Name: LoginResults.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The LoginResults object class represents the information for when an account is logged in to returns the proper menu
*/
public class LoginResults {
    private String nextMenu;
    private Account account;

    //constructor
    public LoginResults(String n, Account a) {
        nextMenu = n;
        account = a;
    }
    //---accessors and mutators as necessary---
    public String getNextMenu() {
        return nextMenu;
    }
    public void setNextMenu(String n) {
        nextMenu = n;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account a) {
        account = a;
    }
}
