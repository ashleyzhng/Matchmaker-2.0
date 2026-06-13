/*
   File Name: AccountDatabase.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The AccountDatabase class contains an Array List of accounts.
*/
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class AccountsDatabase {
    private ArrayList<Account> accounts;
    //constructors
    public AccountsDatabase(){
        accounts = new ArrayList<Account>();
    }
    public AccountsDatabase(Matchmaker mm){
        accounts = new ArrayList<>();
        loadFromFile("Accounts.txt", mm);
    }
    //---accessors and mutators as necessary---
    public ArrayList<Account> getAccounts(){
        return accounts;
    }
    public void setAccounts(ArrayList<Account> a){
        accounts = a;
    }
    
    /* Deletes an account if the provided password matches the account's password
    * @param Account the account to delete
    * @param String the password used for verification
    * @return true if the account was deleted, false otherwise
    */
    public boolean deleteAccount(Account a, String pass){
        if(a != null && a.correctPass(pass)){
            accounts.remove(a);
            //call logout() method
            return true;
        }else{
            return false;
        }
    }
    /* Adds an account to the account database
    * @param Account the account to add
    */
    public void addAccount(Account a){
        accounts.add(a);
    }
    /* Retrieves all user accounts in the database
    * @return an array containing all user accounts
    */
    public User[] getUsers(){
        User[] userArray = new User[accounts.size()];
        int index = 0;
        for(int i = 0; i<accounts.size(); i++){
            if(accounts.get(i) instanceof User){
                userArray[index] = (User)accounts.get(i);
                index++;
            }
        }
        return userArray;
    }
    /* Searches for a user account matching the given user
    * @param User the user to search for
    * @return the matching user, or null if not found
    */
    public User searchByUser(User u){
        boolean found = false;
        User foundUser = null;

        for(int i = 0; i<accounts.size() && !found; i++){
            if(accounts.get(i) == u){
                foundUser = (User)accounts.get(i);
                found = true;
            }
        }
        return foundUser;
    }
    /* Determines whether a username already exists in the database
    * @param String the username to search for
    * @return true if the username exists, false otherwise
    */
    public boolean usernameExists(String u){
        if(searchByUsername(u) == null){
            return false;
        }
        return true;
    }

    /* Loads account data from a file
    * @param String the name of the file to load from
    * @param Matchmaker the matchmaker associated with the accounts
    */
    public void loadFromFile(String file, Matchmaker mm){
        try{
            BufferedReader in = new BufferedReader(new FileReader(file));
            // take in first line for number of accounts
            int numAccounts = Integer.parseInt(in.readLine());
            int count = 0;
            for(int i = 0; i < numAccounts; i++){
                String accountType = in.readLine();
                if (accountType.equalsIgnoreCase("Admin")){
                    Admin admin = new Admin(in.readLine(), in.readLine(), in.readLine(), Boolean.parseBoolean(in.readLine()));
                    accounts.add(admin);
                } else if (accountType.equalsIgnoreCase("Teen")){
                    Teen teen = new Teen(in.readLine(), in.readLine(), in.readLine(), Boolean.parseBoolean(in.readLine()), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine())));
                    accounts.add(teen);
                }else if(accountType.equalsIgnoreCase("Adult")){
                    Adult adult = (new Adult(in.readLine(), in.readLine(), in.readLine(), Boolean.parseBoolean(in.readLine()), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), in.readLine(), in.readLine(), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()))));
                    accounts.add(adult);
                }else if(accountType.equalsIgnoreCase("Senior")){
                    Senior senior = new Senior(in.readLine(), in.readLine(), in.readLine(), Boolean.parseBoolean(in.readLine()), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), in.readLine(), in.readLine(), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Boolean.parseBoolean(in.readLine()), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine())));
                    accounts.add(senior);
                }
                in.readLine();
                count++;
                    
            }
            System.out.println(count + " account(s) created.");
            in.close(); 
        } catch (IOException e){
            System.out.println("Error accessing file " + file);
        } catch (NullPointerException e) {
            System.out.println("File format error or missing data");
        }
    }
    /* Loads match data for users
    * @param Matchmaker the matchmaker containing the match information
    */
    public void loadMatches(Matchmaker mm){
        MatchFileManager loader = new MatchFileManager(mm);
        for (int i = 0; i < accounts.size(); i ++){
            if (!(accounts.get(i) instanceof Admin)){
                User user = (User)(accounts.get(i));
                loader.loadMatches(user.getUsername());
            }
        }
    }
    /* Saves match data for users
    * @param Matchmaker the matchmaker containing the match information
    */
    public void saveMatches(Matchmaker mm){
        MatchFileManager loader = new MatchFileManager(mm);
        for (int i = 0; i < accounts.size(); i ++){
            if(!(accounts.get(i) instanceof Admin)){
                User user = (User) (accounts.get(i));
                loader.saveMatches(user.getUsername());
            }
        }
    }
    /* Searches for a user with a specific username
    * @param String the username to search for
    * @return the matching user, or null if not found
    */
    public User searchByUsername(String targetUsername){
        for (int i = 0; i < accounts.size(); i++){
            if(accounts.get(i) instanceof User){
                User u = (User)accounts.get(i);
                if(u.getUsername().equals(targetUsername)){
                    return u;
                }
            }
        }
        return null;
    }
    /* Searches for a user with a specific first name
    * @param String the first name to search for
    * @return the matching user, or null if not found
    */
    public User searchByFirstName(String firstName) {
        for (int i = 0; i<accounts.size(); i++) {
            if (accounts != null && ((User)accounts.get(i)).getName() != null) {
                String[] parts = ((User)accounts.get(i)).getName().split(" ");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(firstName)) {
                    return (User)accounts.get(i);
                }
            }
        }
        return null;
    }
    /* Searches for a user with a specific last name
    * @param String the last name to search for
    * @return the matching user, or null if not found
    */
    public User searchByLastName(String lastName) {
        for (int i = 0; i<accounts.size(); i++)  {
            if (accounts != null && ((User)accounts.get(i)).getName() != null) {
                String[] parts = ((User)accounts.get(i)).getName().split(" ");           
                if (parts.length > 1 && parts[parts.length - 1].equalsIgnoreCase(lastName)) {
                    return (User)accounts.get(i);
                }
            }
        }
        return null;
    }
    /* Sorts a list of users alphabetically by first name
    * @param ArrayList<User> the list of users to sort
    */
    public void sortByFirstName(ArrayList<User> accounts) {
        for (int i = 0; i < accounts.size() - 1; i++) {
            for (int j = i + 1; j < accounts.size(); j++) {
                String firstI = accounts.get(i).getName().split(" ")[0];
                String firstJ = accounts.get(j).getName().split(" ")[0];
                if (firstI.compareToIgnoreCase(firstJ) > 0) {
                    User temp = accounts.get(i);
                    accounts.set(i, accounts.get(j));
                    accounts.set(j, temp);
                }
            }
        }
    }
    /* Sorts accounts by user type and returns the sorted users
    * @param ArrayList<Account> the list of accounts to sort
    * @return an array of users sorted by user type
    */
    public User[] sortByUserType(ArrayList<Account> accounts) {
        User[] sorted = new User[accounts.size()];
            int index = 0;
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i) instanceof Teen) {
                    sorted[index] = (User)accounts.get(i);
                    index++;
                }
            }
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i) instanceof Adult) {
                    sorted[index] = (User)accounts.get(i);
                    index++;
                }
            }
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i) instanceof Senior) {
                    sorted[index] = (User)accounts.get(i);
                    index++;
                }
            }
            return sorted;
        }
}