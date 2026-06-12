import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class AccountsDatabase {
    private ArrayList<Account> accounts;
    public AccountsDatabase(){
        accounts = new ArrayList<Account>();
    }
    public ArrayList<Account> getAccounts(){
        return accounts;
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
            Account user = null;
            for(int i = numAccounts; i > 0; i--){
                String accountType = in.readLine();
                if (accountType.equals("Admin")){
                    Admin admin = new Admin(in.readLine(), in.readLine(), in.readLine(), Boolean.parseBoolean(in.readLine()));
                    user = admin;
                    accounts.add(admin);
                } 
                if(accountType.equals("Teen")){
                    Teen teen = new Teen(in.readLine(), in.readLine(), in.readLine(), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine())));
                    user = teen;
                    accounts.add(teen);
                }
                if(accountType.equals("Adult")){
                    Adult adult = (new Adult(in.readLine(), in.readLine(), in.readLine(), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), in.readLine(), in.readLine(), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()))));
                    accounts.add(adult);
                    user = adult;
                }
                if(accountType.equals("Senior")){
                    Senior senior = new Senior(in.readLine(), in.readLine(), in.readLine(), in.readLine(), LocalDate.parse(in.readLine()), in.readLine(), in.readLine(), in.readLine(), in.readLine().charAt(0), in.readLine().charAt(0), in.readLine(), in.readLine(), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Boolean.parseBoolean(in.readLine()), new Tag(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine())));
                    user = senior;
                    accounts.add(senior);
                }
                if (!(user instanceof Admin)){
                    User u = (User)user;
                    MatchFileManager matchFileManager = new MatchFileManager(mm);
                    matchFileManager.loadMatches(u.getUsername());
                    u.setMatches(matchFileManager.getMatches());
                    u.setMatchHistory(matchFileManager.getMatchHistory());
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
        
        for (int i = 0; i < accounts.size() && !found; i++){
            User u = (User)accounts.get(i);

            if(u.getUsername().equals(targetUsername)){
                foundUser = u;
                found = true;
            }
        }
        return foundUser;
    }
    
}