import java.util.ArrayList;

public class AccountsDatabase {
    private ArrayList<User> users;

    public AccountsDatabase(){
        users = new ArrayList<User>();
    }
    public boolean deleteAccount(User u, String pass){
        if(u != null && u.correctPass(pass)){
            users.remove(u);
            //call logout() method
            return true;
        }else{
            return false;
        }
    }
    public User[] getUsers(){
        User[] userArray = new User[users.size()];
        int index = 0;
        for(int i = 0; i<users.size(); i++){
            if(users.get(i) instanceof User){
                userArray[index] = users.get(i);
                index++;
            }
        }
        return userArray;
    }
    // all the searching methods for accounts database
    public User searchByUser(User u){
        boolean found = false;
        User foundUser = null;

        for(int i = 0; i<users.size() && !found; i++){
            if(users.get(i) == u){
                foundUser = users.get(i);
                found = true;
            }
        }
        return foundUser;
    }
    // add user from file
    public User searchByUsername(String targetUsername){
        boolean found = false;
        User foundUser = null;

        for(int i = 0; i<users.size() && !found; i++){
            User u = users.get(i);

            if(u.getUsername().equals(targetUsername)){
                foundUser = u;
                found = true;
            }
        }
        return foundUser;
    }
    
}
