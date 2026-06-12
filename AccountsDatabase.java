import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class AccountsDatabase {
    private ArrayList<Account> accounts;
    public AccountsDatabase(){
        accounts = new ArrayList<Account>();
    }
    public AccountsDatabase(Matchmaker mm){
        accounts = new ArrayList<>();
        loadFromFile("Accounts.txt", mm);
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

    public Account searchAccountByUsername(String targetUsername){
        for (int i = 0; i < accounts.size(); i++){
            if(accounts.get(i).getUsername().equals(targetUsername)){
                return accounts.get(i);
            }
        }
        return null;
    }
    // add user from file
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

    public void loadMatches(Matchmaker mm){
        MatchFileManager loader = new MatchFileManager(mm);
        for (int i = 0; i < accounts.size(); i ++){
            if (!(accounts.get(i) instanceof Admin)){
                User user = (User)(accounts.get(i));
                loader.loadMatches(user.getUsername());
            }
        }
    }

    public void saveMatches(Matchmaker mm){
        MatchFileManager loader = new MatchFileManager(mm);
        for (int i = 0; i < accounts.size(); i ++){
            if(!(accounts.get(i) instanceof Admin)){
                User user = (User) (accounts.get(i));
                loader.saveMatches(user.getUsername());
            }
        }
    }

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