import java.io.*;
import java.util.*;

public class AccountsDatabase {
    private ArrayList<Account> accounts;

    public AccountsDatabase(){
        accounts = new ArrayList<Account>();
    }
    public boolean deleteAccount(Account a, String pass){
        if(a != null && a.correctPass(pass)){
            accounts.remove(a);
            //call logout() method
            return true;
        }else{
            return false;
        }
    }
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
    // all the searching methods for accounts database
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
    // add user from file
    public void loadFromFile(String file, Matchmaker mm){
        try{
            BufferedReader in = new BufferedReader(new FileReader(file));
            // take in first line for number of accounts
            int numAccounts = Integer.parseInt(in.readLine());
            for(int i = numAccounts; i > 0; i--){
                if (in.readLine().equals("Admin")){
                    accounts.add(new Admin(in.readLine(), in.readLine(), in.readLine(), Boolean.parseBoolean(in.readLine())));
                } 
                if(in.readLine().equals("Parent")){
                    // add/create parent account
                }
                if(in.readLine().equals("Teen")){
                    // add/create teen account
                }
                if(in.readLine().equals("Adult")){
                    // add/create adult account
                }
                if(in.readLine().equals("Senior")){
                    // add/create senior account
                }
            }
            System.out.println("Accounts created successfully."); 
            in.close(); 
        } catch (IOException e){
            System.out.println("Error accessing file");
        }
    }

    public User searchByUsername(String targetUsername){
        boolean found = false;
        User foundUser = null;

        for(int i = 0; i<accounts.size() && !found; i++){
            User u = (User)accounts.get(i);

            if(u.getUsername().equals(targetUsername)){
                foundUser = u;
                found = true;
            }
        }
        return foundUser;
    }
    
}
