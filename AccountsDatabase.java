import java.io.*;
import java.time.LocalDate;
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
    public void addAccount(Account a){
        accounts.add(a);
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

    public boolean usernameExists(String u){
        if(searchByUsername(u) == null){
            return false;
        }
        return true;
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
                if(in.readLine().equals("Teen")){
                    accounts.add(new Teen(in.readLine(), in.readLine(), in.readLine(), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()))));
                }
                if(in.readLine().equals("Adult")){
                    accounts.add(new Adult(in.readLine(), in.readLine(), in.readLine(), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), in.readLine(), in.readLine(), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()))));
                }
                if(in.readLine().equals("Senior")){
                    accounts.add(new Senior(in.readLine(), in.readLine(), in.readLine(), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), in.readLine(), in.readLine(), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Boolean.parseBoolean(in.readLine()), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()))));
                }
            }
            in.close(); 
        } catch (IOException e){
            System.out.println("Error accessing file " + file);
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
