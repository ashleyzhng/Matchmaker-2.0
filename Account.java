public abstract class Account{
    private String username;
    private String password;
    private String email;
    private boolean emailVerified;
    private int verifyCode;
    private boolean accountStatus;

    // contructors, accessors & mutators
    public boolean correctPass(String pass){
        if(pass == this.password){
            return true;
        }else{
            return false;
        }
    }
    public void logout(){
        // implement after matchmaker is made[
    }
    public boolean changePass(String curPass, String newPass){
        if(correctPass(curPass)){
            this.password = newPass;
            return true;
        }else{
            return false;
        }
    }
    public boolean changeUsername(String pass, String newUsername){
        if(correctPass(pass)){ // and check if uesrname exists or not
            this.username = newUsername;
            return true;
        }else{
            return false;
        }
    }
    public boolean resetPass(String email, String newPass){
        if(this.email == email && emailVerified){
            //print verifyication code
            //idk how im gonna do this
            return true;
        }else{
            return false;
        }
    }
    public boolean verifyEmail(String pass){
        if(correctPass(pass)){
            emailVerified = true;
            // set code to somethign random 
            return true;
        }else{
            return false;
        }
    }
    public boolean deleteAccount(String pass){
        if(correctPass(pass)){
            // remove account from user database array
            //call logout() method
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
            */
           accountStatus = true;
           return true;
        }else{
            return false;
        }
    }
    public String toString(){
        return "Username: " + username;
    }
}