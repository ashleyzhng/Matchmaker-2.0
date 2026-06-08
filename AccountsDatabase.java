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
    /* 
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
    public boolean changePass(String curPass, String newPass){
        if(correctPass(curPass)){
            this.password = newPass;
            return true;
        }else{
            return false;
        }
    }
    /* 
    public boolean changeUsername(String pass, String newUsername){
        if(correctPass(pass)){ // and check if uesrname exists or not
            /* 
            if(AccountsDatabase.searchByUsername(newUsername) == null){
                this.username = newUsername;
                return true;
            }else{
                
                System.out.println("Username Already Exsits");
                return false;
            }
        }else{
            System.out.println("Incorrect Password");
            return false;
        }
    }
    
    public boolean resetPass(String email, String newPass){
        if(this.email == email && emailVerified){
            System.out.println(verifyCode);
            //idk how im gonna do this
            return true;
        }else{
            return false;
        }
    }
    public boolean verifyEmail(String pass){
        if(correctPass(pass)){
            emailVerified = true;
            verifyCode = (int)(Math.random() * 1000000);
            return true;
        }else{
            return false;
        }
    }
    public boolean inactivateAccount(String pass){
        if(correctPass(pass)){
            accountStatus = false;
            //cals id.logout()
            return true;
        }else{
            return false;
        }
    }
    public boolean activateAccount(String pass){
        if(correctPass(pass)){
            /*
            if ID in list of accounts
            if status is inactive
            checks if current password matches 
            if matches
            ^^ add all this stuff in a diff method in matchmaker
            
           accountStatus = true;
           return true;
        }else{
            return false;
        }
    }
    */
}
