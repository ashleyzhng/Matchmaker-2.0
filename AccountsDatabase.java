import java.util.ArrayList;

public class AccountsDatabase {
    private ArrayList<User> users;

    public AccountsDatabase(){
        users = new ArrayList<User>();
    }

    // add user from file
    public Account searchByUsername(String targetUsername){
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
